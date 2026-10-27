package com.example.zarorvecinos.repository

import com.example.zarorvecinos.local.MovimientoDao
import com.example.zarorvecinos.model.Movimiento
import kotlinx.coroutines.flow.map

class FinanzasRepository(private val dao: MovimientoDao) {
    val movimientos = dao.movimientos().map { lista -> lista.map { it.aModelo() } }
    suspend fun guardar(movimiento: Movimiento) = dao.registrar(movimiento.aEntidad())
}
