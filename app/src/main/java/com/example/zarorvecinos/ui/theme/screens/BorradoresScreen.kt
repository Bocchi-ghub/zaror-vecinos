package com.example.zarorvecinos.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.model.Borrador
import com.example.zarorvecinos.model.Movimiento

@Composable
fun BorradoresScreen(borradores: List<Borrador>, ocupado: Boolean, onContinuar: (String) -> Unit, onDescartar: (String) -> Unit) {
    Text("Borradores", style = MaterialTheme.typography.headlineMedium)
    Text("Un borrador por tipo de formulario. Se guarda automáticamente mientras editas.")
    if (borradores.isEmpty()) Text("No hay borradores pendientes.")
    borradores.forEach { b ->
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(if (b.tipo == "RESERVA") "Solicitud de reserva" else "Movimiento financiero")
                Text("Guardado: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(b.actualizado))}")
                Button(onClick = { onContinuar(b.tipo) }, enabled = !ocupado) { Text("Continuar") }
                TextButton(onClick = { onDescartar(b.tipo) }, enabled = !ocupado) { Text("Descartar borrador") }
            }
        }
    }
}
