package com.example.practika.model

data class Usuario(
    val rut: String = "",
    val nombreCompleto: String = "",
    val email: String = "",
    val rol: String = "Estudiante",
    val imagenPerfil: String? = null
)
