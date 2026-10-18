package com.example.practika.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practika.data.CamaraUriFactory
import com.example.practika.model.Usuario
import com.example.practika.ui.components.ImagenInteligente
import com.example.practika.viewmodel.PerfilViewModel

@Composable
fun PerfilScreen(
    usuario: Usuario?,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val imagenUri by perfilViewModel.imagenUri.collectAsState()

    var uriPendienteCamara by remember { mutableStateOf<Uri?>(null) }

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uriSeleccionado ->
        perfilViewModel.actualizarImagenDesdeGaleria(uriSeleccionado)
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        perfilViewModel.actualizarImagenDesdeCamara(uriPendienteCamara, exito)
        if (!exito) uriPendienteCamara = null
    }

    val permisoCamaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            abrirCamara(context, camaraLauncher) { uriPendienteCamara = it }
        } else {
            Toast.makeText(
                context,
                "Debes conceder el permiso de cámara en Ajustes.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun tomarFotografia() {
        val permisoConcedido = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permisoConcedido) {
            abrirCamara(context, camaraLauncher) { uriPendienteCamara = it }
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Foto de perfil",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                ImagenInteligente(imagenUri = imagenUri)

                Text(
                    text = if (imagenUri == null) {
                        "Aún no has seleccionado una imagen"
                    } else {
                        "Imagen de perfil actualizada"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = { galeriaLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Collections, contentDescription = "Galería")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Elegir desde galería")
                }

                OutlinedButton(
                    onClick = { tomarFotografia() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = "Cámara")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tomar fotografía")
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Perfil del Estudiante",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text("Estudiante: ${usuario?.nombreCompleto?.ifBlank { "Sin registrar" } ?: "Sin registrar"}")
                Text("RUT: ${usuario?.rut?.ifBlank { "Sin RUT" } ?: "Sin RUT"}")
                Text("Correo: ${usuario?.email?.ifBlank { "Sin Correo" } ?: "Sin Correo"}")

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }
    }
}

private fun abrirCamara(
    context: Context,
    camaraLauncher: ActivityResultLauncher<Uri>,
    onUriCreada: (Uri) -> Unit
) {
    try {
        val uri = CamaraUriFactory.crearUri(context)
        onUriCreada(uri)
        camaraLauncher.launch(uri)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error al abrir la cámara: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}
