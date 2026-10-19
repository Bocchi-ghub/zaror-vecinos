package com.example.zarorvecinos.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorFechaReserva(
    valor: String,
    error: String?,
    onFechaSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarSelector by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = valor,
        onValueChange = {},
        readOnly = true,
        label = {
            Text("Fecha")
        },
        placeholder = {
            Text("DD/MM/AAAA")
        },
        isError = error != null,
        supportingText = {
            Text(error ?: "Selecciona la fecha de la reserva.")
        },
        trailingIcon = {
            TextButton(
                onClick = {
                    mostrarSelector = true
                }
            ) {
                Text("Elegir")
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )

    if (mostrarSelector) {
        val estadoFecha = rememberDatePickerState(
            initialSelectedDateMillis = fechaAMillis(valor)
        )

        DatePickerDialog(
            onDismissRequest = {
                mostrarSelector = false
            },
            confirmButton = {
                TextButton(
                    enabled = estadoFecha.selectedDateMillis != null,
                    onClick = {
                        estadoFecha.selectedDateMillis?.let { millis ->
                            onFechaSeleccionada(
                                formatoFechaUtc().format(Date(millis))
                            )
                        }

                        mostrarSelector = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarSelector = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = estadoFecha
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorHoraReserva(
    etiqueta: String,
    valor: String,
    error: String?,
    onHoraSeleccionada: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarSelector by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = valor,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(etiqueta)
        },
        placeholder = {
            Text("HH:mm")
        },
        isError = error != null,
        supportingText = {
            Text(error ?: "Selecciona una hora en formato de 24 horas.")
        },
        trailingIcon = {
            TextButton(
                onClick = {
                    mostrarSelector = true
                }
            ) {
                Text("Elegir")
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )

    if (mostrarSelector) {
        val partes = valor.split(":")
        val horaInicial = partes.getOrNull(0)
            ?.toIntOrNull()
            ?.takeIf { it in 0..23 }
            ?: 9

        val minutoInicial = partes.getOrNull(1)
            ?.toIntOrNull()
            ?.takeIf { it in 0..59 }
            ?: 0

        val estadoHora = rememberTimePickerState(
            initialHour = horaInicial,
            initialMinute = minutoInicial,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = {
                mostrarSelector = false
            },
            title = {
                Text(etiqueta)
            },
            text = {
                TimeInput(
                    state = estadoHora,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val horaFormateada = String.format(
                            Locale.ROOT,
                            "%02d:%02d",
                            estadoHora.hour,
                            estadoHora.minute
                        )

                        onHoraSeleccionada(horaFormateada)
                        mostrarSelector = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarSelector = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun formatoFechaUtc(): SimpleDateFormat {
    return SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.ROOT
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
        isLenient = false
    }
}

private fun fechaAMillis(valor: String): Long? {
    if (!Regex("[0-9]{2}/[0-9]{2}/[0-9]{4}").matches(valor)) {
        return null
    }

    val posicion = ParsePosition(0)
    val fecha = formatoFechaUtc().parse(valor, posicion)

    return if (fecha != null && posicion.index == valor.length) {
        fecha.time
    } else {
        null
    }
}
