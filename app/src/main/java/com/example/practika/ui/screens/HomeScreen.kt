package com.example.practika.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.practika.ui.components.AppBottomBar
import com.example.practika.ui.components.HomeActionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Estado para controlar qué sección está seleccionada en la Bottom Bar
    var seccionActual by remember { mutableStateOf("home") }

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
                onSeccionSeleccionada = { nuevaSeccion ->
                    seccionActual = nuevaSeccion
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (seccionActual) {
                // SECCIÓN 1: ASIGNATURAS (Izquierda)
                "asignaturas" -> {
                    Text(
                        text = "Asignaturas de 2° Básico",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    HomeActionCard(
                        title = "Lenguaje y Comunicación",
                        description = "Lectura comprensiva y escritura",
                        icon = Icons.Default.Book
                    )
                    HomeActionCard(
                        title = "Matemática",
                        description = "Números, operaciones y patrones",
                        icon = Icons.Default.Calculate
                    )
                    HomeActionCard(
                        title = "Ciencias Naturales",
                        description = "Animales, plantas y el cuerpo humano",
                        icon = Icons.Default.Eco
                    )
                    HomeActionCard(
                        title = "Historia y Geografía",
                        description = "Comunidad, país y tradiciones",
                        icon = Icons.Default.Public
                    )
                }

                // SECCIÓN 2: INICIO / HOME (Centro)
                "home" -> {
                    // Tarjeta de Bienvenida
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "¡Hola de nuevo!",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bienvenido a tu panel principal. Aquí podrás ver tus actividades y progresos.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Text(
                        text = "Módulos de práctica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    HomeActionCard(
                        title = "Mis Tareas",
                        description = "Revisa las actividades pendientes de hoy",
                        icon = Icons.Default.CheckCircle
                    )

                    HomeActionCard(
                        title = "Logros",
                        description = "Consulta tus metas y medallas alcanzadas",
                        icon = Icons.Default.Star
                    )

                    HomeActionCard(
                        title = "Mi Perfil",
                        description = "Configuración y datos de tu cuenta",
                        icon = Icons.Default.Person
                    )
                }

                // SECCIÓN 3: PERFIL (Derecha)
                "perfil" -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Perfil del Estudiante",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Estudiante: Alumno Ficticio", style = MaterialTheme.typography.bodyMedium)
                            Text("Curso: 2° Básico A", style = MaterialTheme.typography.bodyMedium)
                            Text("Colegio: Escuela Marcela Paz", style = MaterialTheme.typography.bodyMedium)

                            Spacer(modifier = Modifier.height(24.dp))

                            // Botón explícito para cerrar sesión dentro del perfil
                            Button(
                                onClick = onLogout,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = "Cerrar sesión"
                                )
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