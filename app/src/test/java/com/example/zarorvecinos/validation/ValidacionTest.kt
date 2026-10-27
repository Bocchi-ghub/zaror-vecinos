package com.example.zarorvecinos.validation

import com.example.zarorvecinos.model.Formulario
import org.junit.Assert.*
import org.junit.Test

class ValidacionTest {
    private fun reserva() = Formulario(nombre = "Vecino Demo", concepto = "Taller comunitario", fecha = "25/10/2099", inicio = "16:00", fin = "18:00", asistentes = "10")
    private fun finanzas() = Formulario(tipo = "FINANZAS", concepto = "Arriendo de sede", fecha = Validacion.mostrarFecha(Validacion.hoy()), monto = "15000")
    @Test fun reservaCorrecta() { assertTrue(Validacion.errores(reserva()).isEmpty()) }
    @Test fun camposVacios() { assertEquals(6, Validacion.errores(Formulario()).size) }
    @Test fun fechaImposible() { assertNull(Validacion.fechaIso("31/02/2026")) }
    @Test fun bisiesto() { assertEquals("2028-02-29", Validacion.fechaIso("29/02/2028")) }
    @Test fun rechazaFormatoDistinto() { assertNull(Validacion.fechaIso("2026-10-25")) }
    @Test fun fechaNormalizada() { assertEquals("2099-10-25", Validacion.fechaIso("25/10/2099")) }
    @Test fun horasIguales() { assertNotNull(Validacion.errores(reserva().copy(fin = "16:00"))["fin"]) }
    @Test fun horaInvalida() { assertNotNull(Validacion.errores(reserva().copy(inicio = "25:00"))["inicio"]) }
    @Test fun asistentesInvalidos() { listOf("0", "-1", "abc", "1.5").forEach { assertNotNull(Validacion.errores(reserva().copy(asistentes = it))["asistentes"]) } }
    @Test fun finanzasCorrectas() { assertTrue(Validacion.errores(finanzas()).isEmpty()) }
    @Test fun montoInvalido() { listOf("0", "-2", "15.000", "abc", "1000000001").forEach { assertNotNull(Validacion.errores(finanzas().copy(monto = it))["monto"]) } }
    @Test fun tipoInvalido() { assertNotNull(Validacion.errores(finanzas().copy(movimiento = "OTRO"))["movimiento"]) }
    @Test fun reservaPasada() { assertNotNull(Validacion.errores(reserva().copy(fecha = "01/01/2000"))["fecha"]) }
    @Test fun borradorDetectaDatos() { assertFalse(Formulario().tieneDatos); assertTrue(Formulario(nombre = "A").tieneDatos) }
}
