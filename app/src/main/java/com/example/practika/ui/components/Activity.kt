package com.example.practika.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.practika.viewmodel.Actividad
import com.example.practika.viewmodel.Alternativa
import com.example.practika.viewmodel.Materia
import com.example.practika.viewmodel.TipoPregunta
import com.example.practika.viewmodel.letraAlternativa

/**
 * Formulario base de una actividad. Componente "stateless": recibe el estado
 * en [actividad] y notifica cada cambio a través de [onActividadChange].
 * El [id][Actividad.id] es generado automáticamente por el ViewModel.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActivityForm(
    actividad: Actividad,
    onActividadChange: (Actividad) -> Unit,
    onAgregar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Formulario de Actividad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "ID de actividad: #${actividad.id} (automático)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text("Materia", style = MaterialTheme.typography.labelLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Materia.entries.forEach { materia ->
                    FilterChip(
                        selected = actividad.materia == materia,
                        onClick = { onActividadChange(actividad.copy(materia = materia)) },
                        label = { Text(materia.etiqueta) }
                    )
                }
            }

            OutlinedTextField(
                value = actividad.enunciado,
                onValueChange = { onActividadChange(actividad.copy(enunciado = it)) },
                label = { Text("Pregunta") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tipo de pregunta", style = MaterialTheme.typography.labelLarge)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoPregunta.entries.forEach { tipo ->
                    FilterChip(
                        selected = actividad.tipo == tipo,
                        onClick = { onActividadChange(actividad.copy(tipo = tipo)) },
                        label = { Text(tipo.etiqueta) }
                    )
                }
            }

            when (actividad.tipo) {
                TipoPregunta.ESCRITURA -> {
                    OutlinedTextField(
                        value = actividad.respuestaEscrita,
                        onValueChange = { onActividadChange(actividad.copy(respuestaEscrita = it)) },
                        label = { Text("Respuesta esperada") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                TipoPregunta.VERDADERO_FALSO -> {
                    Text("Respuesta correcta", style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = actividad.respuestaVerdaderoFalso,
                            onClick = { onActividadChange(actividad.copy(respuestaVerdaderoFalso = true)) },
                            label = { Text("Verdadero") }
                        )
                        FilterChip(
                            selected = !actividad.respuestaVerdaderoFalso,
                            onClick = { onActividadChange(actividad.copy(respuestaVerdaderoFalso = false)) },
                            label = { Text("Falso") }
                        )
                    }
                }

                TipoPregunta.ALTERNATIVAS -> {
                    Text(
                        text = "Marca con el check la alternativa correcta",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    actividad.alternativas.forEachIndexed { index, alternativa ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = letraAlternativa(index),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(36.dp)
                            )
                            OutlinedTextField(
                                value = alternativa.texto,
                                onValueChange = { nuevoTexto ->
                                    val lista = actividad.alternativas.toMutableList()
                                    lista[index] = alternativa.copy(texto = nuevoTexto)
                                    onActividadChange(actividad.copy(alternativas = lista))
                                },
                                label = { Text("Alternativa") },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = alternativa.esCorrecta,
                                onCheckedChange = { marcada ->
                                    val lista = actividad.alternativas.toMutableList()
                                    lista[index] = alternativa.copy(esCorrecta = marcada)
                                    onActividadChange(actividad.copy(alternativas = lista))
                                }
                            )
                            IconButton(
                                onClick = {
                                    val lista = actividad.alternativas.toMutableList()
                                    lista.removeAt(index)
                                    onActividadChange(actividad.copy(alternativas = lista))
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar alternativa")
                            }
                        }
                    }

                    TextButton(
                        onClick = {
                            onActividadChange(
                                actividad.copy(alternativas = actividad.alternativas + Alternativa())
                            )
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Agregar alternativa")
                    }
                }
            }

            Button(
                onClick = onAgregar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar actividad")
            }
        }
    }
}
