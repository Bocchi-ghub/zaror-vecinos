package com.example.zarorvecinos.repository

import android.content.Context
import com.example.zarorvecinos.local.ReservaEntity
import com.example.zarorvecinos.local.ZarorDatabase
import com.example.zarorvecinos.model.Reserva
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReservaRepository(context: Context){
    private val dao= ZarorDatabase
        .obtenerInstancia(context)
        .reservaDao()

    fun obtenerReservas(): Flow<List<Reserva>>{
        return dao.obtenerReservas().map { entidades ->
                entidades.map{it.aReserva()}
        }
    }
    suspend fun obtenerReservaPorId(id:Int):Reserva?{
        return dao.obtenerReservaPorId(id)?.aReserva()
    }
    suspend fun guardarReserva(reserva: Reserva){
        dao.insertarReserva(reserva.aEntity())
    }
    suspend fun actualizarReserva(reserva: Reserva){
        dao.actualizarReserva(reserva.aEntity())
    }
    suspend fun cancelarReserva(reserva: Reserva){
        dao.actualizarReserva(reserva.copy(activa =false).aEntity())
    }
    suspend fun eliminarReserva(reserva: Reserva){
        dao.eliminarReserva(reserva.aEntity())
    }

    suspend fun existeConflicto(reserva: Reserva): Boolean{
        return dao.contarReservasEnConflicto(
            espacio = reserva.espacio,
            fecha =reserva.fecha,
            horaInicio = reserva.horaInicio,
            horaTermino = reserva.horaTermino
        )>0

    }
}
private fun ReservaEntity.aReserva(): Reserva{
    return Reserva(
        id = id,
        nombreSolicitante =nombreSolicitante,
        espacio =espacio,
        fecha= fecha,
        horaInicio = horaInicio,
        horaTermino = horaTermino,
        asistentes = asistentes,
        observaciones = observaciones,
        activa = activa
    )
}

private fun Reserva.aEntity(): ReservaEntity{
    return ReservaEntity(
        id = id,
        nombreSolicitante = nombreSolicitante,
        espacio = espacio,
        fecha = fecha,
        horaInicio = horaInicio,
        horaTermino = horaTermino,
        asistentes = asistentes,
        observaciones = observaciones,
        activa = activa
    )
}