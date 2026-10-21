package com.example.practika.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Creacion Uri temporal que necesita la app de cámara para escribir la foto (cacheDir/camera).
object CamaraUriFactory {
    fun crearUri(context: Context): Uri {
        val cameraDir = File(context.cacheDir, "camera")
        if (!cameraDir.exists() && !cameraDir.mkdirs()) {
            throw IllegalStateException("No se pudo crear el directorio de la cámara")
        }
        val photoFile = File.createTempFile(
            "perfil_",
            ".jpg",
            cameraDir
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }
}
