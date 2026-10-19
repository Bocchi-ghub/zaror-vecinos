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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme
import com.example.zarorvecinos.viewmodel.CampoReserva
import com.example.zarorvecinos.viewmodel.NuevaReservaUiState

@Composable
fun NuevaReservaScreen(
    uiState: NuevaReservaUiState,
    onCampoChange: (CampoReserva, String) -> Unit,
    onRevisarDatos: () -> Unit,
    onCerrarResumen: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            text = "Todos los campos son obligatorios. " +
                    "Usa únicamente información ficticia.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoReservaFormulario(
            valor = uiState.solicitante,
            onValorChange = {
                onCampoChange(CampoReserva.SOLICITANTE, it)
            },
            etiqueta = "Nombre del solicitante",
            error = uiState.errores.solicitante
        )

        CampoReservaFormulario(
            valor = uiState.motivo,
            onValorChange = {
                onCampoChange(CampoReserva.MOTIVO, it)
            },
            etiqueta = "Motivo del arriendo",
            ayuda = "Ejemplo: Taller comunitario",
            error = uiState.errores.motivo,
            multilinea = true
        )

        CampoReservaFormulario(
            valor = uiState.fecha,
            onValorChange = {
                onCampoChange(CampoReserva.FECHA, it)
            },
            etiqueta = "Fecha",
            ayuda = "Formato DD/MM/AAAA. Ejemplo: 25/10/2026",
            error = uiState.errores.fecha
        )

        CampoReservaFormulario(
            valor = uiState.horaInicio,
            onValorChange = {
                onCampoChange(CampoReserva.HORA_INICIO, it)
            },
            etiqueta = "Hora de inicio",
            ayuda = "Formato de 24 horas. Ejemplo: 16:00",
            error = uiState.errores.horaInicio
        )

        CampoReservaFormulario(
            valor = uiState.horaTermino,
            onValorChange = {
                onCampoChange(CampoReserva.HORA_TERMINO, it)
            },
            etiqueta = "Hora de término",
            ayuda = "Debe ser posterior al inicio. Ejemplo: 18:00",
            error = uiState.errores.horaTermino
        )

        if (uiState.intentoRevisar && !uiState.errores.esValido) {
            Text(
                text = "Revisa los campos marcados antes de continuar.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = onRevisarDatos,
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

        Text(
            text = "Prototipo: revisar los datos todavía no guarda " +
                    "la reserva ni comprueba disponibilidad.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (uiState.mostrarResumen) {
        AlertDialog(
            onDismissRequest = onCerrarResumen,
            title = {
                Text("Revisión de la reserva")
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Solicitante: ${uiState.solicitante.trim()}"
                    )

                    Text(
                        "Motivo: ${uiState.motivo.trim()}"
                    )

                    Text(
                        "Fecha: ${uiState.fecha.trim()}"
                    )

                    Text(
                        "Inicio: ${uiState.horaInicio.trim()}"
                    )

                    Text(
                        "Término: ${uiState.horaTermino.trim()}"
                    )

                    Text(
                        text = "Los datos superaron la validación " +
                                "del formulario. La reserva aún no se ha guardado.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onCerrarResumen
                ) {
                    Text("Seguir editando")
                }
            }
        )
    }
}

@Composable
private fun CampoReservaFormulario(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    ayuda: String? = null,
    multilinea: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = {
            Text(etiqueta)
        },
        isError = error != null,
        supportingText = {
            val mensaje = error ?: ayuda

            if (mensaje != null) {
                Text(mensaje)
            }
        },
        singleLine = !multilinea,
        minLines = if (multilinea) 2 else 1,
        maxLines = if (multilinea) 4 else 1,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun NuevaReservaScreenPreview() {
    ZarorVecinosTheme {
        NuevaReservaScreen(
            uiState = NuevaReservaUiState(),
            onCampoChange = { _, _ -> },
            onRevisarDatos = {},
            onCerrarResumen = {},
            onVolver = {}
        )
    }
}