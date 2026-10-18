package com.example.practika.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.practika.viewmodel.Actividad
import com.example.practika.viewmodel.TipoPregunta
import com.example.practika.viewmodel.letraAlternativa

/**
 * Visor de las actividades creadas. Componente "stateless": recibe la lista
 * y notifica la eliminación a través de [onEliminar].
 */
@Composable
fun ActivityList(
    actividades: List<Actividad>,
    onEliminar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Actividades creadas (${actividades.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (actividades.isEmpty()) {
            Text(
                text = "Aún no hay actividades. Crea una en el formulario de arriba.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            actividades.forEach { actividad ->
                ActivityItem(
                    actividad = actividad,
                    onEliminar = { onEliminar(actividad.id) }
                )
            }
        }
    }
}

@Composable
private fun ActivityItem(
    actividad: Actividad,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Actividad #${actividad.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar actividad")
                }
            }

            Text(
                text = "${actividad.materia.etiqueta} · ${actividad.tipo.etiqueta}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = actividad.enunciado.ifBlank { "(sin enunciado)" },
                style = MaterialTheme.typography.bodyMedium
            )

            when (actividad.tipo) {
                TipoPregunta.ESCRITURA -> {
                    Text(
                        text = "Respuesta: ${actividad.respuestaEscrita.ifBlank { "—" }}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TipoPregunta.VERDADERO_FALSO -> {
                    Text(
                        text = "Respuesta: ${if (actividad.respuestaVerdaderoFalso) "Verdadero" else "Falso"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TipoPregunta.ALTERNATIVAS -> {
                    actividad.alternativas.forEachIndexed { index, alternativa ->
                        Text(
                            text = "${letraAlternativa(index)} " +
                                alternativa.texto.ifBlank { "(vacía)" } +
                                if (alternativa.esCorrecta) "  ✓" else "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
