package com.example.zarorvecinos.viewmodel

import androidx.lifecycle.ViewModel
import com.example.zarorvecinos.validation.ErroresReserva
import com.example.zarorvecinos.validation.ReservaValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NuevaReservaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NuevaReservaUiState())

    val uiState: StateFlow<NuevaReservaUiState> =
        _uiState.asStateFlow()

    fun actualizarCampo(
        campo: CampoReserva,
        valor: String
    ) {
        _uiState.update { actual ->
            val actualizado = when (campo) {
                CampoReserva.SOLICITANTE ->
                    actual.copy(solicitante = valor)

                CampoReserva.MOTIVO ->
                    actual.copy(motivo = valor)

                CampoReserva.FECHA ->
                    actual.copy(fecha = valor)

                CampoReserva.HORA_INICIO ->
                    actual.copy(horaInicio = valor)

                CampoReserva.HORA_TERMINO ->
                    actual.copy(horaTermino = valor)
            }

            actualizado.copy(
                errores = if (actualizado.intentoRevisar) {
                    validar(actualizado)
                } else {
                    ErroresReserva()
                },
                mostrarResumen = false
            )
        }
    }

    fun revisarDatos() {
        _uiState.update { actual ->
            val errores = validar(actual)

            actual.copy(
                errores = errores,
                intentoRevisar = true,
                mostrarResumen = errores.esValido
            )
        }
    }

    fun cerrarResumen() {
        _uiState.update { actual ->
            actual.copy(mostrarResumen = false)
        }
    }

    private fun validar(
        estado: NuevaReservaUiState
    ): ErroresReserva {
        return ReservaValidator.validar(
            solicitante = estado.solicitante,
            motivo = estado.motivo,
            fecha = estado.fecha,
            horaInicio = estado.horaInicio,
            horaTermino = estado.horaTermino
        )
    }
}