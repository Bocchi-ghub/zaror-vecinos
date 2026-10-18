package com.example.zarorvecinos.ui.theme.screens



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme


@Composable
fun NuevaReservaScreen(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    var solicitante by rememberSaveable { mutableStateOf("") }
    var motivo by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var horaInicio by rememberSaveable { mutableStateOf("") }
    var horaTermino by rememberSaveable { mutableStateOf("") }

    var mostrarResumen by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nueva reserva",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Completa los datos para solicitar el uso " +
                    "de la sede vecinal.",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Usa únicamente información ficticia.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = solicitante,
            onValueChange = { solicitante = it },
            label = { Text("Nombre del solicitante") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo del arriendo") },
            placeholder = { Text("Ej.: Taller comunitario") },
            minLines = 2,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            placeholder = { Text("DD/MM/AAAA") },
            supportingText = {
                Text("Ejemplo: 25/10/2026")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = horaInicio,
            onValueChange = { horaInicio = it },
            label = { Text("Hora de inicio") },
            placeholder = { Text("HH:mm") },
            supportingText = {
                Text("Formato de 24 horas. Ejemplo: 16:00")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = horaTermino,
            onValueChange = { horaTermino = it },
            label = { Text("Hora de término") },
            placeholder = { Text("HH:mm") },
            supportingText = {
                Text("Formato de 24 horas. Ejemplo: 18:00")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Esta versión permite revisar los datos. " +
                    "Todavía no valida ni guarda la reserva.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = { mostrarResumen = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Revisar datos")
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a arriendos")
        }
    }

    if (mostrarResumen) {
        AlertDialog(
            onDismissRequest = {
                mostrarResumen = false
            },
            title = {
                Text("Revisión de datos")
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Solicitante: " +
                                solicitante.ifBlank { "Sin completar" }
                    )

                    Text(
                        text = "Motivo: " +
                                motivo.ifBlank { "Sin completar" }
                    )

                    Text(
                        text = "Fecha: " +
                                fecha.ifBlank { "Sin completar" }
                    )

                    Text(
                        text = "Inicio: " +
                                horaInicio.ifBlank { "Sin completar" }
                    )

                    Text(
                        text = "Término: " +
                                horaTermino.ifBlank { "Sin completar" }
                    )

                    Text(
                        text = "Vista previa: la reserva no se ha guardado.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarResumen = false
                    }
                ) {
                    Text("Seguir editando")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NuevaReservaScreenPreview() {
    ZarorVecinosTheme {
        NuevaReservaScreen(
            onVolver = {}
        )
    }
}