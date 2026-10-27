package com.example.zarorvecinos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val concepto: String, val tipo: String, val monto: Long, val fecha: String,
    val uriFoto: String? = null
)
