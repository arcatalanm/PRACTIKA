package com.example.practika.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practika.data.local.AppDatabase
import com.example.practika.data.local.entity.UsuarioEntity
import com.example.practika.model.LoginModel
import com.example.practika.model.RegistroModel
import com.example.practika.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
    private val prefs = application.getSharedPreferences("auth", Context.MODE_PRIVATE)

    // Obtiene estado registro via MutableStateFlow
    private val _registroForm = MutableStateFlow(RegistroModel())
    // Expone los datos hacia las vistas (read only)
    val registroForm = _registroForm.asStateFlow()

    private val _loginForm = MutableStateFlow(LoginModel())
    val loginForm = _loginForm.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError = _mensajeError.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo = _usuarioActivo.asStateFlow()

    private val rutRegex = Regex("""^(\d{1,2}\.\d{3}\.\d{3}-[\dkK]|\d{7,8}-[\dkK])$""") // 12345678-9 o 12.345.678-k
    private val nombreRegex = Regex("""^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]{3,}$""") // tildes, ñ, lenght>=3
    private val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""") // user@dom.ex
    private val passwordRegex = Regex("""^.{6,}$""") // lenght>=6

    // Recuperador de Sesión
    init {
        // Almacenamiento clave valor donde se consulta el estado de rut_activo
        prefs.getString("rut_activo", null)?.let { rut ->
            // Se lanza una corrutina vinculada al ciclo del viewmodel (por sentencias SQL por parte del DAO)
            viewModelScope.launch {
                usuarioDao.buscarPorRut(rut)?.let {
                    // Mapeo datos crudos hacia Usuario y actualiza el estado reactivo de _usuarioActivo
                    _usuarioActivo.value = Usuario(it.rut, it.nombreCompleto, it.email, it.rol, it.imagenPerfil)
                }
            }
        }
    }
    // Funcion de estado activo de Usuario
    fun estaUsuarioLogueado(): Boolean = prefs.contains("rut_activo")
    // Funciones de acutalizacion --> actualizando columna del registroPriv (Patch )
    fun actualizarRut(v: String) { _registroForm.value = _registroForm.value.copy(rut = v) }
    fun actualizarNombre(v: String) { _registroForm.value = _registroForm.value.copy(nombreCompleto = v) }
    fun actualizarEmail(v: String) { _registroForm.value = _registroForm.value.copy(email = v) }
    fun actualizarPassword(v: String) { _registroForm.value = _registroForm.value.copy(password = v) }
    fun actualizarAceptaCondiciones(v: Boolean) { _registroForm.value = _registroForm.value.copy(aceptaCondiciones = v) }

    fun actualizarLoginIdentificador(v: String) { _loginForm.value = _loginForm.value.copy(identificador = v) }
    fun actualizarLoginPassword(v: String) { _loginForm.value = _loginForm.value.copy(password = v) }
    fun limpiarError() { _mensajeError.value = null }

    fun registrar(onSuccess: () -> Unit) {
        // Validaciones con regex en el form proveniente del Entity crudo
        val d = _registroForm.value
        val error = when {
            !rutRegex.matches(d.rut.trim()) -> "RUT inválido (ej: 12345678-9)"
            !nombreRegex.matches(d.nombreCompleto.trim()) -> "El nombre debe tener solo letras (mín. 3)"
            !emailRegex.matches(d.email.trim()) -> "Correo electrónico inválido"
            !passwordRegex.matches(d.password) -> "Contraseña debe tener al menos 6 caracteres"
            !d.aceptaCondiciones -> "Debes aceptar los términos y condiciones"
            else -> null
        }

        if (error != null) {
            _mensajeError.value = error
            return
        }

        viewModelScope.launch {
            if (usuarioDao.buscarPorRut(d.rut.trim()) != null) {
                _mensajeError.value = "Este RUT ya está registrado"
                return@launch
            }
            // Persiste el usuario en la BD limpiando datos
            val nuevo = UsuarioEntity(d.rut.trim(), d.nombreCompleto.trim(), d.email.trim(), d.password)
            usuarioDao.insertarUsuario(nuevo)

            prefs.edit().putString("rut_activo", nuevo.rut).apply()
            // Mapea datos directamente en los componentes (UI)
            _usuarioActivo.value = Usuario(nuevo.rut, nuevo.nombreCompleto, nuevo.email, nuevo.rol, nuevo.imagenPerfil)
            _registroForm.value = RegistroModel()
            _mensajeError.value = null
            onSuccess()
        }
    }

    fun iniciarSesion(onSuccess: () -> Unit) {
        // Validación previa persistencia
        val l = _loginForm.value
        if (l.identificador.isBlank() || l.password.isBlank()) {
            _mensajeError.value = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            // Validación para persistencia
            val u = usuarioDao.buscarPorIdentificador(l.identificador.trim())
            if (u == null || u.password != l.password) {
                _mensajeError.value = "Credenciales incorrectas o usuario no registrado"
                return@launch
            }

            // Persistencia de la sesión
            prefs.edit().putString("rut_activo", u.rut).apply()
            // Setear usuario activo mediante columnas del UsuarioEntity
            _usuarioActivo.value = Usuario(u.rut, u.nombreCompleto, u.email, u.rol, u.imagenPerfil)
            _mensajeError.value = null
            onSuccess()
        }
    }

    fun cerrarSesion() {
        // Limpieza de persistencia
        prefs.edit().clear().apply()
        _usuarioActivo.value = null
        // Seteo '', null, []
        _loginForm.value = LoginModel()
        _mensajeError.value = null
    }
}
