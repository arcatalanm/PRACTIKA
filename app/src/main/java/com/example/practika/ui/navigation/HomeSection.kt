package com.example.practika.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeSection(
    val titulo: String,
    val icono: ImageVector
) {
    ASIGNATURAS("Asignaturas", Icons.Default.Book),
    INICIO("Inicio", Icons.Default.Home),
    PERFIL("Perfil", Icons.Default.Person)
}
