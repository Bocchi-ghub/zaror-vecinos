package com.example.zarorvecinos.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme

@Composable
fun ArriendosScreen(
    onVolver: () -> Unit,
    onNuevaReserva: () -> Unit,
    reservas: List<Reserva>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Arriendos de la sede",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Text(
                text = "Consulta las reservas registradas " +
                        "en este dispositivo.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            Button(
                onClick = onNuevaReserva,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nueva reserva")
            }
        }

        if (reservas.isEmpty()) {
            item {
                Text(
                    text = "No hay arriendos registrados.",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Text(
                    text = "Las reservas guardadas aparecerán aquí.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(
                items = reservas,
                key = { reserva -> reserva.id }
            ) { reserva ->
                ReservaRegistradaCard(
                    reserva = reserva
                )
            }
        }

        item {
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}

@Composable
private fun ReservaRegistradaCard(
    reserva: Reserva,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = reserva.espacio,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = if (reserva.activa) {
                    "Activa"
                } else {
                    "Cancelada"
                },
                style = MaterialTheme.typography.labelLarge,
                color = if (reserva.activa) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            Text(
                text = "Solicitante: ${reserva.nombreSolicitante}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Fecha: ${reserva.fecha}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Horario: ${reserva.horaInicio} - " +
                        reserva.horaTermino,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Asistentes: ${reserva.asistentes}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (reserva.observaciones.isNotBlank()) {
                Text(
                    text = "Observaciones: ${reserva.observaciones}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArriendosScreenPreview() {
    ZarorVecinosTheme {
        ArriendosScreen(
            onVolver = {},
            onNuevaReserva = {},
            reservas = listOf(
                Reserva(
                    id = 1,
                    nombreSolicitante = "Vecino Demo",
                    espacio = "Sede vecinal",
                    fecha = "25/10/2026",
                    horaInicio = "16:00",
                    horaTermino = "18:00",
                    asistentes = 15,
                    observaciones = "Taller comunitario"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArriendosVaciosPreview() {
    ZarorVecinosTheme {
        ArriendosScreen(
            onVolver = {},
            onNuevaReserva = {},
            reservas = emptyList()
        )
    }
}