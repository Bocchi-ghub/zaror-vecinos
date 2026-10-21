package com.example.zarorvecinos.viewmodel

import com.example.zarorvecinos.validation.ErroresReserva

enum class CampoReserva {
    SOLICITANTE,
    MOTIVO,
    FECHA,
    HORA_INICIO,
    HORA_TERMINO,
    ASISTENTES
}

data class NuevaReservaUiState(
    val solicitante: String = "",
    val motivo: String = "",
    val fecha: String = "",
    val horaInicio: String = "",
    val horaTermino: String = "",
    val asistentes: String = "",

    val errores: ErroresReserva = ErroresReserva(),
    val asistentesError: String? = null,

    val intentoRevisar: Boolean = false,
    val mostrarResumen: Boolean = false,

    val guardando: Boolean = false,
    val reservaGuardada: Boolean = false,
    val errorGuardado: String? = null
) {
    val formularioValido: Boolean
        get() = errores.esValido && asistentesError == null
}