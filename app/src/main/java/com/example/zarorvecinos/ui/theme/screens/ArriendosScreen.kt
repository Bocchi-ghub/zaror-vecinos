package com.example.zarorvecinos.ui.theme.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.validation.Validacion
import com.example.zarorvecinos.ui.theme.components.FotoVista

@Composable
fun ArriendosScreen(reservas: List<Reserva>, ocupado: Boolean, onNuevaReserva: () -> Unit, onCancelar: (Reserva) -> Unit) {
    Text("Arriendos de la sede", style = MaterialTheme.typography.headlineMedium)
    Button(onClick = { onNuevaReserva() }, enabled = !ocupado) { Text("Nueva reserva / continuar borrador") }
    if (reservas.isEmpty()) Text("No hay reservas. Si hubo un error de lectura, se indicará abajo.")
    reservas.forEach { r ->
        Card(Modifier.fillMaxWidth().animateContentSize()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(r.espacio, style = MaterialTheme.typography.titleLarge)
                Text(if (r.activa) "Activa" else "Cancelada", color = if (r.activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                Text("Solicitante: ${r.nombreSolicitante}")
                Text("${Validacion.mostrarFecha(r.fecha)} · ${r.horaInicio} a ${r.horaTermino}")
                Text("Asistentes: ${r.asistentes}")
                Text(r.observaciones)
                r.uriFoto?.let { FotoVista(it) }
                if (r.activa) OutlinedButton(onClick = { onCancelar(r) }, enabled = !ocupado) { Text("Cancelar reserva") }
            }
        }
    }
}
