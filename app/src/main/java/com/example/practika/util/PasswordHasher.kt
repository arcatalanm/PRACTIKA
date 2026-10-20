package com.example.practika.util

import at.favre.lib.crypto.bcrypt.BCrypt

// Instancia unica que no guarda objetos
object PasswordHasher {

    // Convierte "password" -> "$2a$12$e8fH..."
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12,password.toCharArray())
    }

    // Compara la clave ingresada con el hash almacenado en SQLite
    fun verifyPassword(passwordIngresada: String, hashGuardado: String): Boolean {
        val result = BCrypt.verifyer().verify(passwordIngresada.toCharArray(), hashGuardado)
        return  result.verified
    }
}