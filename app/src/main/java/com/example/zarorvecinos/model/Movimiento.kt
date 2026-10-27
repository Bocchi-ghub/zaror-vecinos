package com.example.zarorvecinos.model



data class Movimiento(
    val id: Int = 0,
    val concepto: String, val tipo: String, val monto: Long, val fecha: String,
    val uriFoto: String? = null
)
