package com.example.practika.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

/**
 * Crea una [Uri] temporal (con FileProvider) para almacenar una fotografía
 * tomada con la cámara dentro del directorio de caché de la aplicación.
 */
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
