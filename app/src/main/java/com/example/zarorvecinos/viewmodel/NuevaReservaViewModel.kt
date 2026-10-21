package com.example.zarorvecinos.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.repository.ReservaRepository
import com.example.zarorvecinos.validation.ErroresReserva
import com.example.zarorvecinos.validation.ReservaValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NuevaReservaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = ReservaRepository(application)

    private val _uiState = MutableStateFlow(NuevaReservaUiState())

    val uiState: StateFlow<NuevaReservaUiState> =
        _uiState.asStateFlow()

    fun actualizarCampo(
        campo: CampoReserva,
        valor: String
    ) {
        if (_uiState.value.guardando ||
            _uiState.value.reservaGuardada
        ) {
            return
        }

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

                CampoReserva.ASISTENTES ->
                    actual.copy(asistentes = valor)
            }

            val conErrores = if (actualizado.intentoRevisar) {
                validarEstado(actualizado)
            } else {
                actualizado.copy(
                    errores = ErroresReserva(),
                    asistentesError = null
                )
            }

            conErrores.copy(
                mostrarResumen = false,
                errorGuardado = null
            )
        }
    }

    fun revisarDatos() {
        if (_uiState.value.guardando ||
            _uiState.value.reservaGuardada
        ) {
            return
        }

        _uiState.update { actual ->
            val validado = validarEstado(actual)

            validado.copy(
                intentoRevisar = true,
                mostrarResumen = validado.formularioValido,
                errorGuardado = null
            )
        }
    }

    fun cerrarResumen() {
        if (_uiState.value.guardando) {
            return
        }

        _uiState.update { actual ->
            actual.copy(
                mostrarResumen = false,
                errorGuardado = null
            )
        }
    }

    fun guardarReserva() {
        val actual = _uiState.value

        if (actual.guardando ||
            actual.reservaGuardada ||
            !actual.mostrarResumen
        ) {
            return
        }

        val validado = validarEstado(actual)

        if (!validado.formularioValido) {
            _uiState.value = validado.copy(
                intentoRevisar = true,
                mostrarResumen = false
            )
            return
        }

        val reserva = Reserva(
            nombreSolicitante = validado.solicitante.trim(),
            espacio = "Sede vecinal",
            fecha = validado.fecha.trim(),
            horaInicio = validado.horaInicio.trim(),
            horaTermino = validado.horaTermino.trim(),
            asistentes = validado.asistentes.trim().toInt(),
            observaciones = validado.motivo.trim(),
            activa = true
        )

        _uiState.value = validado.copy(
            guardando = true,
            errorGuardado = null
        )

        viewModelScope.launch {
            try {
                if (repository.existeConflicto(reserva)) {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            errorGuardado = "La sede ya tiene una reserva " +
                                    "activa en esa fecha y horario."
                        )
                    }
                    return@launch
                }

                repository.guardarReserva(reserva)

                _uiState.update {
                    it.copy(
                        guardando = false,
                        mostrarResumen = false,
                        reservaGuardada = true,
                        errorGuardado = null
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "NuevaReservaViewModel",
                    "No se pudo guardar la reserva",
                    exception
                )

                _uiState.update {
                    it.copy(
                        guardando = false,
                        errorGuardado = "No se pudo guardar la reserva. " +
                                "Inténtalo nuevamente."
                    )
                }
            }
        }
    }

    private fun validarEstado(
        estado: NuevaReservaUiState
    ): NuevaReservaUiState {
        return estado.copy(
            errores = ReservaValidator.validar(
                solicitante = estado.solicitante,
                motivo = estado.motivo,
                fecha = estado.fecha,
                horaInicio = estado.horaInicio,
                horaTermino = estado.horaTermino
            ),
            asistentesError = ReservaValidator.validarAsistentes(
                estado.asistentes
            )
        )
    }
}