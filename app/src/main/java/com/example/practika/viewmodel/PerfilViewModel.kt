package com.example.practika.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practika.data.ImagenPerfilStorage
import com.example.practika.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Estado de la imagen de perfil del usuario logueado. La imagen se copia al
 * almacenamiento interno y su ruta se persiste en la base de datos, por lo que
 * sobrevive al cierre de la aplicación.
 */
class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
    private val storage = ImagenPerfilStorage(application)

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    private var rutActual: String? = null

    /** Carga la imagen guardada del [rut] (ruta en la BD → archivo en disco). */
    fun cargarImagen(rut: String?) {
        rutActual = rut
        if (rut == null) {
            _imagenUri.value = null
            return
        }
        viewModelScope.launch {
            val ruta = usuarioDao.buscarPorRut(rut)?.imagenPerfil
            _imagenUri.value = ruta
                ?.takeIf { File(it).exists() }
                ?.let { Uri.fromFile(File(it)) }
        }
    }

    fun actualizarImagenDesdeGaleria(uri: Uri?) {
        if (uri != null) guardar(uri)
    }

    fun actualizarImagenDesdeCamara(uri: Uri?, capturaCorrecta: Boolean) {
        if (capturaCorrecta && uri != null) guardar(uri)
    }

    /** Copia la imagen al almacenamiento interno y persiste su ruta en la BD. */
    private fun guardar(origen: Uri) {
        val rut = rutActual ?: return
        viewModelScope.launch {
            val ruta = withContext(Dispatchers.IO) { storage.guardar(origen, rut) } ?: return@launch
            usuarioDao.actualizarImagenPerfil(rut, ruta)
            _imagenUri.value = Uri.fromFile(File(ruta))
        }
    }
}
