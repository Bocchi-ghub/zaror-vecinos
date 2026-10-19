package com.example.zarorvecinos.viewmodel


import com.example.zarorvecinos.validation.ErroresReserva

enum class CampoReserva {
    SOLICITANTE,
    MOTIVO,
    FECHA,
    HORA_INICIO,
    HORA_TERMINO
}

data class NuevaReservaUiState(
    val solicitante: String = "",
    val motivo: String = "",
    val fecha: String = "",
    val horaInicio: String = "",
    val horaTermino: String = "",
    val errores: ErroresReserva = ErroresReserva(),
    val intentoRevisar: Boolean = false,
    val mostrarResumen: Boolean = false
)