package com.example.zarorvecinos.repository

import com.example.zarorvecinos.local.MovimientoDao
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.network.BackendRetrofitClient
import kotlinx.coroutines.flow.map

class FinanzasRepository(private val dao: MovimientoDao) {
    val movimientos = dao.movimientos().map { lista -> lista.map { it.aModelo() } }

    suspend fun sincronizar() {
        runCatching {
            val remotos = BackendRetrofitClient.api.obtenerMovimientos()
            remotos.forEach { dao.insertar(it.aEntidad()) }
        }
    }

    suspend fun guardar(movimiento: Movimiento) {
        val guardado = runCatching {
            BackendRetrofitClient.api.crearMovimiento(movimiento)
        }.getOrDefault(movimiento)
        dao.registrar(guardado.aEntidad())
    }

    suspend fun actualizar(movimiento: Movimiento) {
        val actualizado = runCatching {
            BackendRetrofitClient.api.actualizarMovimiento(movimiento.id, movimiento)
        }.getOrDefault(movimiento)
        dao.insertar(actualizado.aEntidad())
    }

    suspend fun eliminar(id: Int) {
        runCatching { BackendRetrofitClient.api.eliminarMovimiento(id) }
        dao.eliminarPorId(id)
    }
}