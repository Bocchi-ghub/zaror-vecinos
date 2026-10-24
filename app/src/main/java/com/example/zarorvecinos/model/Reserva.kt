package com.example.zarorvecinos.model

data class Reserva(
    val id: Int=0,
    val nombreSolicitante: String,
    val espacio: String,
    val fecha: String,
    val horaInicio: String,
    val horaTermino: String,
    val asistentes: Int,
    val observaciones: String="",
    val activa: Boolean=true,

    val uriFoto: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
)