package com.example.zarorvecinos.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun InicioScreen(
    reservasActivas: Int, cantidadMovimientos: Int, cantidadBorradores: Int,
    ocupado: Boolean, habilitado: Boolean, onNuevaReserva: () -> Unit,
    onFinanzas: () -> Unit, onNotificaciones: () -> Unit, onAjustes: () -> Unit
) {
    Text("Tu comunidad, organizada", style = MaterialTheme.typography.headlineMedium)
    Text("MVP académico · Solo datos ficticios · Almacenamiento local")
    Text("${reservasActivas} reservas activas · ${cantidadMovimientos} movimientos · ${cantidadBorradores} borradores")
    Button(onClick = { onNuevaReserva() }, enabled = !ocupado) { Text("Reservar sede vecinal") }
    OutlinedButton(onClick = { onFinanzas() }) { Text("Consultar finanzas") }
    Text("Los avisos llegan solo a este dispositivo, no a todos los vecinos.")

    Text(if (habilitado) "Notificaciones habilitadas." else "Notificaciones deshabilitadas. Puedes seguir usando la app.")
    Button(onClick = { onNotificaciones() }) { Text(if (habilitado) "Configurar notificaciones" else "Habilitar notificaciones") }
    TextButton(onClick = { onAjustes() }) { Text("Abrir ajustes de avisos") }
}
