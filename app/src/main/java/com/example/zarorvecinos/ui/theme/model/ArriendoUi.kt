package com.example.zarorvecinos.ui.theme.model


enum class EstadoArriendoUi(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADO("Confirmada"),
    CANCELADO("Cancelada")
}

data class ArriendoUi(
    val id: Int,
    val motivo: String,
    val solicitante: String,
    val fecha: String,
    val horaInicio: String,
    val horaTermino: String,
    val estado: EstadoArriendoUi
)