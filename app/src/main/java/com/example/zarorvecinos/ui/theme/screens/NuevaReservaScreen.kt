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
import com.example.zarorvecinos.validation.ErroresReserva
import com.example.zarorvecinos.validation.ReservaValidator

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

    var intentoRevisar by rememberSaveable {
        mutableStateOf(false)
    }

    var mostrarResumen by rememberSaveable {
        mutableStateOf(false)
    }

    val errores = if (intentoRevisar) {
        ReservaValidator.validar(
            solicitante = solicitante,
            motivo = motivo,
            fecha = fecha,
            horaInicio = horaInicio,
            horaTermino = horaTermino
        )
    } else {
        ErroresReserva()
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
            text = "Todos los campos son obligatorios. " +
                    "Usa únicamente información ficticia.",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoReserva(
            valor = solicitante,
            onValorChange = { solicitante = it },
            etiqueta = "Nombre del solicitante",
            error = errores.solicitante
        )

        CampoReserva(
            valor = motivo,
            onValorChange = { motivo = it },
            etiqueta = "Motivo del arriendo",
            ayuda = "Ejemplo: Taller comunitario",
            error = errores.motivo,
            multilinea = true
        )

        CampoReserva(
            valor = fecha,
            onValorChange = { fecha = it },
            etiqueta = "Fecha",
            ayuda = "Formato DD/MM/AAAA. Ejemplo: 25/10/2026",
            error = errores.fecha
        )

        CampoReserva(
            valor = horaInicio,
            onValorChange = { horaInicio = it },
            etiqueta = "Hora de inicio",
            ayuda = "Formato de 24 horas. Ejemplo: 16:00",
            error = errores.horaInicio
        )

        CampoReserva(
            valor = horaTermino,
            onValorChange = { horaTermino = it },
            etiqueta = "Hora de término",
            ayuda = "Debe ser posterior al inicio. Ejemplo: 18:00",
            error = errores.horaTermino
        )

        if (intentoRevisar && !errores.esValido) {
            Text(
                text = "Revisa los campos marcados antes de continuar.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                intentoRevisar = true

                val resultado = ReservaValidator.validar(
                    solicitante = solicitante,
                    motivo = motivo,
                    fecha = fecha,
                    horaInicio = horaInicio,
                    horaTermino = horaTermino
                )

                mostrarResumen = resultado.esValido
            },
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

    if (mostrarResumen) {
        AlertDialog(
            onDismissRequest = {
                mostrarResumen = false
            },
            title = {
                Text("Revisión de la reserva")
            },
            text = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Solicitante: ${solicitante.trim()}")
                    Text("Motivo: ${motivo.trim()}")
                    Text("Fecha: ${fecha.trim()}")
                    Text("Inicio: ${horaInicio.trim()}")
                    Text("Término: ${horaTermino.trim()}")

                    Text(
                        text = "Los datos superaron la validación " +
                                "del formulario. La reserva aún no se ha guardado.",
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

@Composable
private fun CampoReserva(
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
            onVolver = {}
        )
    }
}