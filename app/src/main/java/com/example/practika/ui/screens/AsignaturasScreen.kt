package com.example.practika.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.practika.ui.components.HomeActionCard

@Composable
fun AsignaturasScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Creacion de tuplas triple
        val materias = listOf(
            Triple("Lenguaje y Comunicación", "Lectura comprensiva y escritura", Icons.Default.Book),
            Triple("Matemática", "Números, operaciones y patrones", Icons.Default.Calculate),
            Triple("Ciencias Naturales", "Animales, plantas y el cuerpo humano", Icons.Default.Eco),
            Triple("Historia y Geografía", "Comunidad, país y tradiciones", Icons.Default.Public)
        )
        materias.forEach { (nombre, descripcion, icono) ->
            // Se inyecta los datos en la tarjeta de HomeActionCard
            HomeActionCard(title = nombre, description = descripcion, icon = icono)
        }
    }
}
