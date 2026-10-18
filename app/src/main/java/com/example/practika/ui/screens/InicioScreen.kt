package com.example.practika.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practika.model.Usuario
import com.example.practika.ui.components.ActivityForm
import com.example.practika.ui.components.ActivityList
import com.example.practika.ui.components.Confeti
import com.example.practika.ui.components.ImagenInteligente
import com.example.practika.viewmodel.ActividadViewModel

@Composable
fun InicioScreen(
    usuario: Usuario?,
    imagenUri: Uri?,
    modifier: Modifier = Modifier,
    dispararConfeti: Boolean = false,
    actividadViewModel: ActividadViewModel = viewModel()
) {
    val formulario by actividadViewModel.formulario.collectAsState()
    val actividades by actividadViewModel.actividades.collectAsState()

    var origenCaja by remember { mutableStateOf(Offset.Zero) }
    var centroAvatar by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { origenCaja = it.positionInRoot() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier.onGloballyPositioned { coords ->
                            val pos = coords.positionInRoot()
                            centroAvatar = Offset(
                                pos.x + coords.size.width / 2f,
                                pos.y + coords.size.height / 2f
                            )
                        }
                    ) {
                        ImagenInteligente(imagenUri = imagenUri, size = 72.dp)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "¡Hola, ${usuario?.nombreCompleto?.ifBlank { "Estudiante" } ?: "Estudiante"}!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bienvenido a PRACTIKA Selecciona una opción para comenzar.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            ActivityForm(
                actividad = formulario,
                onActividadChange = actividadViewModel::actualizarFormulario,
                onAgregar = actividadViewModel::agregarActividad
            )

            ActivityList(
                actividades = actividades,
                onEliminar = actividadViewModel::eliminarActividad
            )
        }

        Confeti(
            disparar = dispararConfeti,
            origen = centroAvatar - origenCaja,
            radioInicialDp = 36f,
            modifier = Modifier.matchParentSize()
        )
    }
}
