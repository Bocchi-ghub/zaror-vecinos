package com.example.zarorvecinos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.repository.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReservaViewModel(application: Application): AndroidViewModel(application){

    private  val repository = ReservaRepository(application)

    private val _uiState= MutableStateFlow(ReservaUiState())
    val uiState: StateFlow<ReservaUiState> = _uiState.asStateFlow()

    val reservas: StateFlow<List<Reserva>> = repository
    .obtenerReservas()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
    fun onNombreChange(valor : String){
        _uiState.update { it.copy(nombreSolicitante = valor, nombreError = null) }
    }
    fun onEspacioChange(valor : String){
        _uiState.update { it.copy(espacio = valor, espacioError = null) }
    }

    fun onFechaChange(valor : String){
        _uiState.update { it.copy(fecha = valor, fechaError = null) }
    }

    fun onHoraInicioChange(valor : String){
        _uiState.update { it.copy(horaInicio = valor, horaInicioError = null) }
    }

    fun onHoraTerminoChange(valor : String){
        _uiState.update { it.copy(horaTermino = valor, horaTerminoError = null) }
    }

    fun onAsistentesChange(valor : String){
        _uiState.update { it.copy(asistentes = valor, asistentesError = null) }
    }
    fun onObservacionesChange(valor : String){
        _uiState.update { it.copy(observaciones = valor) }
    }

    fun guardarReserva(){
        val estado = _uiState.value
        val errores = validar(estado)

        if(errores.isNotEmpty()){
            _uiState.update {
                it.copy(
                    nombreError = errores["nombre"],
                    espacioError = errores["espacio"],
                    fechaError = errores["fecha"],
                    horaInicioError = errores["horaInicio"],
                    horaTerminoError = errores["horaTermino"],
                    asistentesError = errores["asistentes"]
                )
            }
            return
        }
        viewModelScope.launch {
            val reserva = Reserva(
                nombreSolicitante = estado.nombreSolicitante.trim(),
                espacio = estado.espacio,
                fecha = estado.fecha,
                horaInicio = estado.horaInicio,
                horaTermino = estado.horaTermino,
                asistentes = estado.asistentes.toInt(),
                observaciones = estado.observaciones.trim()

            )
            val conflicto = repository.existeConflicto(reserva)
            if(conflicto){
                _uiState.update {
                    it.copy(
                        errorGeneral = "Ya existe una reserva activa para ese espacio, fecha y horario."
                    )
                }
                return@launch
            }
            repository.guardarReserva(reserva)
            _uiState.update {
                ReservaUiState(reservaGuardada = true)
            }
        }
    }
    fun limpiarMensajeExito(){
        _uiState.update { it.copy(reservaGuardada = false) }
    }
    private fun validar(estado: ReservaUiState): Map<String, String>{
        val errores = mutableMapOf<String, String>()

        if (estado.nombreSolicitante.isBlank()){
            errores["nombre"]= "Ingresa el nombre del solicitante."
        }
        if (estado.espacio.isBlank()){
            errores["espacio"]= "Selecciona un espacio."
        }

        if (estado.fecha.isBlank()){
            errores["fecha"]= "Selecciona una fecha."
        }
        if (estado.horaInicio.isBlank()){
            errores["horaInicio"]= "Selecciona una hora de inicio."
        }
        if (estado.horaTermino.isBlank()){
            errores["horaTermino"]= "Selecciona una hora de termino."
        }
        if(estado.horaInicio.isNotBlank()&& estado.horaTermino.isNotBlank()){
            if(estado.horaTermino<=estado.horaInicio){
                errores["horaTermino"]=
                    "La hora de término debe ser posterior a la de inicio."
            }
        }
        val asistentes = estado.asistentes.toIntOrNull()

        if (estado.asistentes.isBlank()){
            errores["asistentes"] ="Ingresa la cantidad de asistentes."
        } else if (asistentes== null ||asistentes<=0){
            errores["asistentes"]="Ingresa un numero mayor a cero"
        }
        return  errores

    }

}