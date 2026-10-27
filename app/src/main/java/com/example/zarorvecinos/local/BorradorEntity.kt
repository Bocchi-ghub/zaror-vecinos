package com.example.zarorvecinos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borradores")
data class BorradorEntity(@PrimaryKey val tipo: String, val contenido: String, val actualizado: Long)
