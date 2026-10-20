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
import com.example.zarorvecinos.ui.theme.components.SelectorFechaReserva
import com.example.zarorvecinos.ui.theme.components.SelectorHoraReserva
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically

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

        SelectorFechaReserva(
            valor = uiState.fecha,
            error = uiState.errores.fecha,
            onFechaSeleccionada = { fechaSeleccionada ->
                onCampoChange(
                    CampoReserva.FECHA,
                    fechaSeleccionada
                )
            }
        )

        SelectorHoraReserva(
            etiqueta = "Hora de inicio",
            valor = uiState.horaInicio,
            error = uiState.errores.horaInicio,
            onHoraSeleccionada = { horaSeleccionada ->
                onCampoChange(
                    CampoReserva.HORA_INICIO,
                    horaSeleccionada
                )
            }
        )

        SelectorHoraReserva(
            etiqueta = "Hora de término",
            valor = uiState.horaTermino,
            error = uiState.errores.horaTermino,
            onHoraSeleccionada = { horaSeleccionada ->
                onCampoChange(
                    CampoReserva.HORA_TERMINO,
                    horaSeleccionada
                )
            }
        )

        AnimatedVisibility(
            visible = uiState.intentoRevisar &&
                    !uiState.errores.esValido,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 200)
            ) + expandVertically(
                animationSpec = tween(durationMillis = 200)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 150)
            ) + shrinkVertically(
                animationSpec = tween(durationMillis = 150)
            )
        ) {
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
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
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