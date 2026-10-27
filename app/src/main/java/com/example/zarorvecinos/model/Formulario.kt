package com.example.zarorvecinos.model



data class Formulario(
    val tipo: String = "RESERVA", val id: Int = 0, val nombre: String = "",
    val concepto: String = "", val fecha: String = "", val inicio: String = "",
    val fin: String = "", val asistentes: String = "", val monto: String = "",
    val movimiento: String = "INGRESO", val foto: String? = null
) {
    val tieneDatos: Boolean get() = listOf(nombre, concepto, fecha, inicio, fin, asistentes, monto).any { it.isNotBlank() } || foto != null
}
