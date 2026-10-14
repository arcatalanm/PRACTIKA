package com.example.practika.model

data class RegistroModel(
    val rut: String = "",
    val nombreCompleto: String = "",
    val email: String = "",
    val password: String = "",
    val aceptaCondiciones: Boolean = false
)
