package com.example.zarorvecinos.repository

import com.example.zarorvecinos.local.ReservaDao
import com.example.zarorvecinos.model.Reserva
import kotlinx.coroutines.flow.map

class ReservaRepository(private val dao: ReservaDao) {
    val reservas = dao.reservas().map { lista -> lista.map { it.aModelo() } }
    suspend fun guardar(reserva: Reserva) = dao.registrar(reserva.aEntidad())
    suspend fun cancelar(id: Int) = dao.cancelar(id)
}
