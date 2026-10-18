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

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val usuarioDao = AppDatabase.getDatabase(application).usuarioDao()
    private val storage = ImagenPerfilStorage(application)

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    private var rutActual: String? = null

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

    private fun guardar(origen: Uri) {
        val rut = rutActual ?: return
        viewModelScope.launch {
            val ruta = withContext(Dispatchers.IO) { storage.guardar(origen, rut) } ?: return@launch
            usuarioDao.actualizarImagenPerfil(rut, ruta)
            _imagenUri.value = Uri.fromFile(File(ruta))
        }
    }
}
