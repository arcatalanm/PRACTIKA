package com.example.practika.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val rut: String,
    val nombreCompleto: String,
    val email: String,
    val password: String,
    val rol: String = "Estudiante",
    val imagenPerfil: String? = null
)
