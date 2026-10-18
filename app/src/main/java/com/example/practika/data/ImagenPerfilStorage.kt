package com.example.practika.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

/**
 * Copia la imagen de perfil al almacenamiento interno de la app (filesDir),
 * redimensionándola, y devuelve la ruta del archivo para persistirla en la BD.
 */
class ImagenPerfilStorage(private val context: Context) {

    /**
     * Guarda [origen] como imagen del usuario [rut] y devuelve la ruta absoluta,
     * o null si no se pudo procesar.
     */
    fun guardar(origen: Uri, rut: String): String? = try {
        val bitmap = decodificar(origen) ?: return null

        val escalado = escalar(bitmap)
        val archivo = File(context.filesDir, "perfil_${rut.replace(Regex("[^A-Za-z0-9]"), "")}.jpg")

        FileOutputStream(archivo).use { salida ->
            escalado.compress(Bitmap.CompressFormat.JPEG, 85, salida)
        }

        if (escalado !== bitmap) bitmap.recycle()
        escalado.recycle()

        archivo.absolutePath
    } catch (e: Exception) {
        null
    }

    /** Decodifica el bitmap aplicando [BitmapFactory.Options.inSampleSize] para no cargar la foto completa en memoria. */
    private fun decodificar(origen: Uri): Bitmap? {
        val resolver = context.contentResolver

        val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        resolver.openInputStream(origen)?.use { BitmapFactory.decodeStream(it, null, limites) }
        if (limites.outWidth <= 0 || limites.outHeight <= 0) return null

        val opciones = BitmapFactory.Options().apply {
            inSampleSize = calcularInSampleSize(limites.outWidth, limites.outHeight, MAX_LADO)
        }
        return resolver.openInputStream(origen)?.use { BitmapFactory.decodeStream(it, null, opciones) }
    }

    /** Factor de reducción (potencia de 2) para acercarse a [maxLado] sin pasarse. */
    private fun calcularInSampleSize(ancho: Int, alto: Int, maxLado: Int): Int {
        var sample = 1
        while (ancho / (sample * 2) >= maxLado || alto / (sample * 2) >= maxLado) {
            sample *= 2
        }
        return sample
    }

    /** Ajuste fino: reduce la imagen para que su lado mayor no supere [MAX_LADO]. */
    private fun escalar(bitmap: Bitmap): Bitmap {
        val mayor = maxOf(bitmap.width, bitmap.height)
        if (mayor <= MAX_LADO) return bitmap

        val factor = MAX_LADO.toFloat() / mayor
        val ancho = (bitmap.width * factor).roundToInt().coerceAtLeast(1)
        val alto = (bitmap.height * factor).roundToInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, ancho, alto, true)
    }

    companion object {
        private const val MAX_LADO = 512
    }
}
