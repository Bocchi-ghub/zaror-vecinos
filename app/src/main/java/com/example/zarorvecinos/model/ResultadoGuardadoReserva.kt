package com.example.zarorvecinos.model

sealed class  ResultadoGuardadoReserva{
    data class  Exitoso(
        val reserva : Reserva
    ): ResultadoGuardadoReserva()
    data class Rechazado(
        val motivo : String
    ): ResultadoGuardadoReserva()
    data class Error(
        val mensaje : String
    ): ResultadoGuardadoReserva()
}