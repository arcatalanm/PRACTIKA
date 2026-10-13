package com.example.practika.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AppBottomBar(
    seccionActual: String,
    onSeccionSeleccionada: (String) -> Unit
) {
    NavigationBar {
        // Asignaturas
        NavigationBarItem(
            selected = seccionActual == "asignaturas",
            onClick = { onSeccionSeleccionada("asignaturas") },
            icon = { Icon(imageVector = Icons.Default.Book, contentDescription = "Asignaturas") },
            label = { Text("Asignaturas") }
        )

        // Incio
        NavigationBarItem(
            selected = seccionActual == "home",
            onClick = { onSeccionSeleccionada("home") },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )

        // Perfil
        NavigationBarItem(
            selected = seccionActual == "perfil",
            onClick = { onSeccionSeleccionada("perfil") },
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}
