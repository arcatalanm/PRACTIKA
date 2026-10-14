package com.example.practika.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.practika.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios WHERE rut = :identificador OR email = :identificador LIMIT 1")
    suspend fun buscarPorIdentificador(identificador: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE rut = :rut LIMIT 1")
    suspend fun buscarPorRut(rut: String): UsuarioEntity?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int
}
