package com.example.practika.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practika.ui.components.AppBottomBar
import com.example.practika.ui.components.HomeActionCard
import com.example.practika.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    var seccionActual by remember { mutableStateOf("home") }
    val usuario by viewModel.usuarioActivo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (seccionActual) {
                            "asignaturas" -> "Asignaturas"
                            "perfil" -> "Mi Perfil"
                            else -> "PRACTIKA"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            AppBottomBar(
                seccionActual = seccionActual,
                onSeccionSeleccionada = { seccionActual = it }
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (seccionActual) {
                // 1. ASIGNATURAS (2° Básico)
                "asignaturas" -> {
                    Text(
                        text = "Asignaturas de 2° Básico",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    val materias = listOf(
                        Triple("Lenguaje y Comunicación", "Lectura comprensiva y escritura", Icons.Default.Book),
                        Triple("Matemática", "Números, operaciones y patrones", Icons.Default.Calculate),
                        Triple("Ciencias Naturales", "Animales, plantas y el cuerpo humano", Icons.Default.Eco),
                        Triple("Historia y Geografía", "Comunidad, país y tradiciones", Icons.Default.Public)
                    )
                    materias.forEach { (nombre, desc, icono) ->
                        HomeActionCard(title = nombre, description = desc, icon = icono)
                    }
                }

                // 2. INICIO
                "home" -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "¡Hola, ${usuario?.nombreCompleto?.ifBlank { "Estudiante" } ?: "Estudiante"}!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bienvenido a MPAZ PRO 2.°. Selecciona una opción para comenzar.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Text("Módulos de práctica", fontWeight = FontWeight.SemiBold)
                    HomeActionCard(title = "Mis Tareas", description = "Revisa las actividades pendientes", icon = Icons.Default.CheckCircle)
                    HomeActionCard(title = "Logros", description = "Metas y medallas alcanzadas", icon = Icons.Default.Star)
                    HomeActionCard(title = "Mi Perfil", description = "Datos de tu cuenta", icon = Icons.Default.Person)
                }

                // 3. PERFIL
                "perfil" -> {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Perfil del Estudiante", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Estudiante: ${usuario?.nombreCompleto?.ifBlank { "Sin registrar" } ?: "Sin registrar"}")
                            Text("RUT: ${usuario?.rut?.ifBlank { "Sin RUT" } ?: "Sin RUT"}")
                            Text("Correo: ${usuario?.email?.ifBlank { "Sin Correo" } ?: "Sin Correo"}")
                            Text("Colegio: Escuela Marcela Paz (2° Básico)")

                            Spacer(modifier = Modifier.padding(top = 8.dp))

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
        }
    }
}