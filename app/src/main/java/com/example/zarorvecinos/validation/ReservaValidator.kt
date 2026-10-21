package com.example.zarorvecinos.validation


import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale

data class ErroresReserva(
    val solicitante: String? = null,
    val motivo: String? = null,
    val fecha: String? = null,
    val horaInicio: String? = null,
    val horaTermino: String? = null
) {
    val esValido: Boolean
        get() = solicitante == null &&
                motivo == null &&
                fecha == null &&
                horaInicio == null &&
                horaTermino == null
}

object ReservaValidator {

    fun validarAsistentes(valor: String): String? {
        val cantidad = valor.trim().toIntOrNull()

        return when {
            valor.isBlank() ->
                "Ingresa la cantidad de asistentes."

            cantidad == null || cantidad <= 0 ->
                "Ingresa un número entero mayor a cero."

            else -> null
        }
    }

    fun validar(
        solicitante: String,
        motivo: String,
        fecha: String,
        horaInicio: String,
        horaTermino: String
    ): ErroresReserva {
        val nombreLimpio = solicitante.trim()
        val motivoLimpio = motivo.trim()
        val fechaLimpia = fecha.trim()

        val inicioEnMinutos = convertirHoraAMinutos(horaInicio)
        val terminoEnMinutos = convertirHoraAMinutos(horaTermino)

        val errorSolicitante = when {
            nombreLimpio.isBlank() ->
                "Ingresa el nombre del solicitante."

            nombreLimpio.length < 3 ->
                "El nombre debe tener al menos 3 caracteres."

            else -> null
        }

        val errorMotivo = when {
            motivoLimpio.isBlank() ->
                "Ingresa el motivo del arriendo."

            motivoLimpio.length < 5 ->
                "El motivo debe tener al menos 5 caracteres."

            else -> null
        }

        val errorFecha = when {
            fechaLimpia.isBlank() ->
                "Ingresa la fecha de la reserva."

            !esFechaValida(fechaLimpia) ->
                "Ingresa una fecha real con formato DD/MM/AAAA."

            else -> null
        }

        val errorInicio = when {
            horaInicio.isBlank() ->
                "Ingresa la hora de inicio."

            inicioEnMinutos == null ->
                "Usa el formato HH:mm, entre 00:00 y 23:59."

            else -> null
        }

        val errorTermino = when {
            horaTermino.isBlank() ->
                "Ingresa la hora de término."

            terminoEnMinutos == null ->
                "Usa el formato HH:mm, entre 00:00 y 23:59."

            inicioEnMinutos != null &&
                    terminoEnMinutos <= inicioEnMinutos ->
                "La hora de término debe ser posterior al inicio."

            else -> null
        }

        return ErroresReserva(
            solicitante = errorSolicitante,
            motivo = errorMotivo,
            fecha = errorFecha,
            horaInicio = errorInicio,
            horaTermino = errorTermino
        )
    }

    private fun esFechaValida(fecha: String): Boolean {
        val patron = Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}")

        if (!patron.matches(fecha)) {
            return false
        }

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.ROOT
        ).apply {
            isLenient = false
        }

        val posicion = ParsePosition(0)
        val resultado = formato.parse(fecha, posicion)

        return resultado != null &&
                posicion.index == fecha.length
    }

    private fun convertirHoraAMinutos(hora: String): Int? {
        val horaLimpia = hora.trim()
        val patron = Regex("([01][0-9]|2[0-3]):[0-5][0-9]")

        if (!patron.matches(horaLimpia)) {
            return null
        }

        val partes = horaLimpia.split(":")
        val horas = partes[0].toInt()
        val minutos = partes[1].toInt()

        return horas * 60 + minutos
    }
}