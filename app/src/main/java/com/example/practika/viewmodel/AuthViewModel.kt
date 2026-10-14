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

    // Estados
    private val _registroForm = MutableStateFlow(RegistroModel())
    val registroForm = _registroForm.asStateFlow()

    private val _loginForm = MutableStateFlow(LoginModel())
    val loginForm = _loginForm.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError = _mensajeError.asStateFlow()

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo = _usuarioActivo.asStateFlow()

    // Expresiones regulares
    private val rutRegex = Regex("""^(\d{1,2}\.\d{3}\.\d{3}-[\dkK]|\d{7,8}-[\dkK])$""")
    private val nombreRegex = Regex("""^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\s]{3,}$""")
    private val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
    private val passwordRegex = Regex("""^.{6,}$""")

    init {
        // Cargar sesión si había un usuario guardado
        prefs.getString("rut_activo", null)?.let { rut ->
            viewModelScope.launch {
                usuarioDao.buscarPorRut(rut)?.let {
                    _usuarioActivo.value = Usuario(it.rut, it.nombreCompleto, it.email)
                }
            }
        }
    }

    fun estaUsuarioLogueado(): Boolean = prefs.contains("rut_activo")

    // Actualizadores del formulario de registro
    fun actualizarRut(v: String) { _registroForm.value = _registroForm.value.copy(rut = v) }
    fun actualizarNombre(v: String) { _registroForm.value = _registroForm.value.copy(nombreCompleto = v) }
    fun actualizarEmail(v: String) { _registroForm.value = _registroForm.value.copy(email = v) }
    fun actualizarPassword(v: String) { _registroForm.value = _registroForm.value.copy(password = v) }
    fun actualizarAceptaCondiciones(v: Boolean) { _registroForm.value = _registroForm.value.copy(aceptaCondiciones = v) }

    // Actualizadores del formulario de login
    fun actualizarLoginIdentificador(v: String) { _loginForm.value = _loginForm.value.copy(identificador = v) }
    fun actualizarLoginPassword(v: String) { _loginForm.value = _loginForm.value.copy(password = v) }
    fun limpiarError() { _mensajeError.value = null }

    // REGISTRO
    fun registrar(onSuccess: () -> Unit) {
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

            val nuevo = UsuarioEntity(d.rut.trim(), d.nombreCompleto.trim(), d.email.trim(), d.password)
            usuarioDao.insertarUsuario(nuevo)

            prefs.edit().putString("rut_activo", nuevo.rut).apply()
            _usuarioActivo.value = Usuario(nuevo.rut, nuevo.nombreCompleto, nuevo.email)
            _registroForm.value = RegistroModel()
            _mensajeError.value = null
            onSuccess()
        }
    }

    // LOGIN
    fun iniciarSesion(onSuccess: () -> Unit) {
        val l = _loginForm.value
        if (l.identificador.isBlank() || l.password.isBlank()) {
            _mensajeError.value = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            val u = usuarioDao.buscarPorIdentificador(l.identificador.trim())
            if (u == null || u.password != l.password) {
                _mensajeError.value = "Credenciales incorrectas o usuario no registrado"
                return@launch
            }

            prefs.edit().putString("rut_activo", u.rut).apply()
            _usuarioActivo.value = Usuario(u.rut, u.nombreCompleto, u.email)
            _mensajeError.value = null
            onSuccess()
        }
    }

    // LOGOUT
    fun cerrarSesion() {
        prefs.edit().clear().apply()
        _usuarioActivo.value = null
        _loginForm.value = LoginModel()
        _mensajeError.value = null
    }
}
