package com.example.zarorvecinos.viewmodel

data class ReservaUiState(
    val nombreSolicitante: String = "",
    val espacio: String = "",
    val fecha: String = "",
    val horaInicio: String = "",
    val horaTermino: String = "",
    val asistentes: String = "",
    val observaciones: String = "",
    val nombreError: String? = null,
    val espacioError: String? = null,
    val fechaError: String? = null,
    val horaInicioError: String? = null,
    val horaTerminoError: String? = null,
    val asistentesError: String? = null,
    val errorGeneral: String? = null,
    val reservaGuardada: Boolean = false

)