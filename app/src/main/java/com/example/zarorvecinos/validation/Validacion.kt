package com.example.zarorvecinos.validation

import java.text.SimpleDateFormat
import java.text.ParsePosition
import java.util.Locale
import java.util.Date
import com.example.zarorvecinos.model.Formulario

object Validacion {
    fun fechaIso(texto: String): String? {
        if (!Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}").matches(texto.trim())) return null
        val f = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).apply { isLenient = false }
        val p = ParsePosition(0)
        val d = f.parse(texto.trim(), p) ?: return null
        return if (p.index == texto.trim().length) SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(d) else null
    }
    fun mostrarFecha(iso: String): String = if (Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}").matches(iso)) "${iso.substring(8)}/${iso.substring(5,7)}/${iso.substring(0,4)}" else iso
    fun hoy() = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date())
    fun hora(valor: String) = Regex("([01][0-9]|2[0-3]):[0-5][0-9]").matches(valor)
    fun errores(f: Formulario): Map<String, String> = buildMap {
        val fecha = fechaIso(f.fecha)
        if (fecha == null) put("fecha", "Selecciona una fecha válida.")
        if (f.concepto.trim().length < 5) put("concepto", "Ingresa al menos 5 caracteres.")
        if (f.tipo == "RESERVA") {
            if (f.nombre.trim().length < 3) put("nombre", "Ingresa al menos 3 caracteres.")
            if (fecha != null && fecha < hoy()) put("fecha", "No puedes reservar en una fecha pasada.")
            if (!hora(f.inicio)) put("inicio", "Selecciona una hora válida.")
            if (!hora(f.fin)) put("fin", "Selecciona una hora válida.")
            else if (hora(f.inicio) && f.fin <= f.inicio) put("fin", "El término debe ser posterior al inicio.")
            if ((f.asistentes.toIntOrNull() ?: 0) <= 0) put("asistentes", "Ingresa un entero mayor a cero.")
        } else {
            if (f.movimiento !in listOf("INGRESO", "GASTO")) put("movimiento", "Selecciona ingreso o gasto.")
            val monto = f.monto.toLongOrNull()
            if (monto == null || monto !in 1L..1_000_000_000L) put("monto", "Ingresa pesos enteros entre 1 y 1.000.000.000.")
            if (fecha != null && fecha > hoy()) put("fecha", "El movimiento no puede tener fecha futura.")
        }
    }
}
