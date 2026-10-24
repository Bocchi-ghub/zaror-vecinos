package com.example.zarorvecinos.repository

import android.content.Context
import com.example.zarorvecinos.local.ReservaEntity
import com.example.zarorvecinos.local.ZarorDatabase
import com.example.zarorvecinos.model.Reserva
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.location.Location
import com.example.zarorvecinos.model.ResultadoGuardadoReserva
import com.example.zarorvecinos.model.UbicacionReserva

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

    suspend fun guardarReservaConEvidencia(
        reserva: Reserva,
        uriFoto : String?,
        ubicacion : UbicacionReserva?
    ): ResultadoGuardadoReserva{
    if (ubicacion == null){
        return  ResultadoGuardadoReserva.Error(
            mensaje ="No se pudo obtener la ubicacion del dispositivo"
        )
    }
        val distancia = calcularDistanciaMetros(
            latitudUsuario = ubicacion.latitud,
            longitudUsuario = ubicacion.longitud,
            latitudSede =  LATITUD_SEDE,
            longitudSede = LONGITUD_SEDE
        )
        if (distancia > RADIO_PERMITIDO_METROS){
            return  ResultadoGuardadoReserva.Rechazado(
                motivo ="Debes encontrarte cerca de la sede vecinal"
            )
        }
        val reservaConEvidencia = reserva.copy(
            uriFoto= uriFoto,
            latitud = ubicacion.latitud,
            longitud = ubicacion.longitud
        )
        return try {
            guardarReserva(reservaConEvidencia)

            ResultadoGuardadoReserva.Exitoso(
                reserva = reservaConEvidencia
            )
        }catch (exception : Exception){
            ResultadoGuardadoReserva.Error(
                mensaje = "No se pudo guardar la reserva"
            )
        }
    }

    private fun calcularDistanciaMetros(
        latitudUsuario : Double,
        longitudUsuario : Double,
        latitudSede : Double,
        longitudSede : Double
    ): Double {
        val resultados = FloatArray(1)
        Location.distanceBetween(
            latitudUsuario ,
            longitudUsuario ,
            latitudSede,
            longitudSede,
            resultados

        )
        return  resultados[0].toDouble()
    }
    companion object {
        private const val LATITUD_SEDE =  -33.456900
        private const val LONGITUD_SEDE =  -70.648300
        private const val RADIO_PERMITIDO_METROS =  200.0
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
        activa = activa,
        uriFoto = uriFoto,
        latitud = latitud,
        longitud = longitud
    )
}

private fun Reserva.aEntity(): ReservaEntity {
    return ReservaEntity(
        id = id,
        nombreSolicitante = nombreSolicitante,
        espacio = espacio,
        fecha = fecha,
        horaInicio = horaInicio,
        horaTermino = horaTermino,
        asistentes = asistentes,
        observaciones = observaciones,
        activa = activa,
        uriFoto = uriFoto,
        latitud = latitud,
        longitud = longitud
    )
}




}




