package com.example.zarorvecinos.repository

import com.example.zarorvecinos.local.BorradorEntity
import com.example.zarorvecinos.local.MovimientoEntity
import com.example.zarorvecinos.local.ReservaEntity
import com.example.zarorvecinos.model.Borrador
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.model.Reserva

internal fun ReservaEntity.aModelo() = Reserva(id = id, nombreSolicitante = nombreSolicitante, espacio = espacio, fecha = fecha, horaInicio = horaInicio, horaTermino = horaTermino, asistentes = asistentes, observaciones = observaciones, activa = activa, uriFoto = uriFoto, latitud = latitud, longitud = longitud)

internal fun Reserva.aEntidad() = ReservaEntity(id = id, nombreSolicitante = nombreSolicitante, espacio = espacio, fecha = fecha, horaInicio = horaInicio, horaTermino = horaTermino, asistentes = asistentes, observaciones = observaciones, activa = activa, uriFoto = uriFoto, latitud = latitud, longitud = longitud)

internal fun MovimientoEntity.aModelo() = Movimiento(id = id, concepto = concepto, tipo = tipo, monto = monto, fecha = fecha, uriFoto = uriFoto)

internal fun Movimiento.aEntidad() = MovimientoEntity(id = id, concepto = concepto, tipo = tipo, monto = monto, fecha = fecha, uriFoto = uriFoto)

internal fun BorradorEntity.aModelo() = Borrador(tipo = tipo, contenido = contenido, actualizado = actualizado)

internal fun Borrador.aEntidad() = BorradorEntity(tipo = tipo, contenido = contenido, actualizado = actualizado)
