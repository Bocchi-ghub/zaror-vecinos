package com.example.zarorvecinos.ui.theme.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zarorvecinos.viewmodel.ReservaViewModel

@Composable
fun PruebaRoomScreen(
    modifier: Modifier = Modifier,
    viewModel: ReservaViewModel = viewModel()
) {
    val reservas by viewModel.reservas.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Prueba Room",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = {
                viewModel.onNombreChange("Prueba Backend")
                viewModel.onEspacioChange("Sede Vecinal")
                viewModel.onFechaChange("2026-10-20")
                viewModel.onHoraInicioChange("18:00")
                viewModel.onHoraTerminoChange("20:00")
                viewModel.onAsistentesChange("10")
                viewModel.onObservacionesChange("Prueba temporal")
                viewModel.guardarReserva()
            }
        ) {
            Text("Insertar reserva de prueba")
        }

        LazyColumn {
            items(reservas) { reserva ->
                Text(
                    text = "${reserva.id} - ${reserva.nombreSolicitante} - " +
                            "${reserva.espacio} - ${reserva.fecha} " +
                            "${reserva.horaInicio} a ${reserva.horaTermino}"
                )
            }
        }
    }
}