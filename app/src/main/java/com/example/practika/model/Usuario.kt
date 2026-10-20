package com.example.practika.model

// No necesita password ya que los comps visuales solo necesitan consumir sus datos públicos (menor exposición)
data class Usuario(
    val rut: String = "",
    val nombreCompleto: String = "",
    val email: String = "",
    val rol: String = "Estudiante",
    val imagenPerfil: String? = null
)
