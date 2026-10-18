package com.example.practika.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.practika.ui.navigation.HomeSection

@Composable
fun AppBottomBar(
    seccionActual: HomeSection,
    onSeccionSeleccionada: (HomeSection) -> Unit
) {
    NavigationBar {
        HomeSection.entries.forEach { seccion ->
            NavigationBarItem(
                selected = seccionActual == seccion,
                onClick = { onSeccionSeleccionada(seccion) },
                icon = { Icon(seccion.icono, contentDescription = seccion.titulo) },
                label = { Text(seccion.titulo) }
            )
        }
    }
}
