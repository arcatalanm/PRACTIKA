package com.example.practika.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private class Particula(
    val angulo: Float,
    val velocidadDp: Float,
    val color: Color,
    val giro: Float,
    val tamanoDp: Float,
    val vida: Float
)

private val COLORES_CONFETI = listOf(
    Color(0xFF0284C7), // azul (primary)
    Color(0xFF06B6D4), // cyan (secondary)
    Color(0xFFF59E0B), // ámbar (tertiary)
    Color(0xFFEF4444), // rojo
    Color(0xFF22C55E), // verde
    Color(0xFFA855F7)  // violeta
)

/**
 * Confeti que sale en abanico desde [origen]. Cuando [disparar] pasa a true
 * reproduce la animación una vez. No requiere dependencias externas.
 *
 * @param radioInicialDp distancia desde [origen] donde nace cada partícula
 *        (útil para que salgan desde el borde del avatar y no desde su centro).
 */
@Composable
fun Confeti(
    disparar: Boolean,
    origen: Offset,
    modifier: Modifier = Modifier,
    radioInicialDp: Float = 0f,
    duracionMs: Int = 2200,
    cantidad: Int = 48
) {
    val progreso = remember { Animatable(0f) }
    val particulas = remember(cantidad) { generarParticulas(cantidad) }

    LaunchedEffect(disparar) {
        if (disparar) {
            progreso.snapTo(0f)
            progreso.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = duracionMs, easing = LinearEasing)
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val p = progreso.value
        if (p <= 0f) return@Canvas

        val tiempo = p * (duracionMs / 1000f)
        val gravedad = 500.dp.toPx()
        val radio = radioInicialDp.dp.toPx()

        particulas.forEach { particula ->
            if (p > particula.vida) return@forEach

            val velocidad = particula.velocidadDp.dp.toPx()
            val inicioX = origen.x + cos(particula.angulo) * radio
            val inicioY = origen.y + sin(particula.angulo) * radio

            val x = inicioX + velocidad * cos(particula.angulo) * tiempo
            val y = inicioY +
                velocidad * sin(particula.angulo) * tiempo +
                0.5f * gravedad * tiempo * tiempo

            val alpha = (1f - p / particula.vida).coerceIn(0f, 1f)
            val ancho = particula.tamanoDp.dp.toPx()

            rotate(degrees = particula.giro * tiempo * 360f, pivot = Offset(x, y)) {
                drawRect(
                    color = particula.color.copy(alpha = alpha),
                    topLeft = Offset(x - ancho / 2f, y - ancho * 0.3f),
                    size = Size(ancho, ancho * 0.6f)
                )
            }
        }
    }
}

private fun generarParticulas(cantidad: Int): List<Particula> {
    val random = Random(System.currentTimeMillis())
    return List(cantidad) {
        // Ángulo entre -150° y -30° (hacia arriba y a los costados), en radianes.
        val anguloGrados = random.nextFloat() * 120f - 150f
        Particula(
            angulo = (anguloGrados * PI / 180.0).toFloat(),
            velocidadDp = 160f + random.nextFloat() * 200f,
            color = COLORES_CONFETI[random.nextInt(COLORES_CONFETI.size)],
            giro = (random.nextFloat() * 2f - 1f) * 2f,
            tamanoDp = 8f + random.nextFloat() * 8f,
            vida = 0.6f + random.nextFloat() * 0.4f
        )
    }
}
