package com.example.zarorvecinos.repository

import com.example.zarorvecinos.local.ReservaDao
import com.example.zarorvecinos.model.ConflictoReserva
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.network.BackendRetrofitClient
import kotlinx.coroutines.flow.map

class ReservaRepository(private val dao: ReservaDao) {
    val reservas = dao.reservas().map { lista -> lista.map { it.aModelo() } }

    suspend fun sincronizar() {
        runCatching {
            val remotas = BackendRetrofitClient.api.obtenerReservas()
            remotas.forEach { dao.insertar(it.aEntidad()) }
        }
    }

    suspend fun guardar(reserva: Reserva) {
        if (dao.conflictos(reserva.id, reserva.espacio, reserva.fecha, reserva.horaInicio, reserva.horaTermino) > 0) {
            throw ConflictoReserva()
        }
        val guardada = runCatching {
            BackendRetrofitClient.api.crearReserva(reserva)
        }.onFailure {
            android.util.Log.e("ZarorBackend", "Error al enviar reserva", it)
        }.getOrDefault(reserva)
        dao.registrar(guardada.aEntidad())
    }

    suspend fun actualizar(reserva: Reserva) {
        val actualizada = runCatching {
            BackendRetrofitClient.api.actualizarReserva(reserva.id, reserva)
        }.getOrDefault(reserva)
        dao.insertar(actualizada.aEntidad())
    }

    suspend fun cancelar(id: Int) {
        dao.cancelar(id)
        val actual = dao.obtenerPorId(id)?.aModelo()
        if (actual != null) {
            runCatching {
                BackendRetrofitClient.api.actualizarReserva(id, actual.copy(activa = false))
            }
        }
    }

    suspend fun eliminar(id: Int) {
        runCatching { BackendRetrofitClient.api.eliminarReserva(id) }
        dao.eliminarPorId(id)
    }
}