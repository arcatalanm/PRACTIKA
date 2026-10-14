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
    val items = listOf(
        Triple("asignaturas", "Asignaturas", Icons.Default.Book),
        Triple("home", "Inicio", Icons.Default.Home),
        Triple("perfil", "Perfil", Icons.Default.Person)
    )

    NavigationBar {
        items.forEach { (clave, titulo, icono) ->
            NavigationBarItem(
                selected = seccionActual == clave,
                onClick = { onSeccionSeleccionada(clave) },
                icon = { Icon(icono, contentDescription = titulo) },
                label = { Text(titulo) }
            )
        }
    }
}
