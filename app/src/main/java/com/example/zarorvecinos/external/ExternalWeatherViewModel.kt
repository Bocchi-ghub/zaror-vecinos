package com.example.zarorvecinos.external

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = false,
    val temperature: Double? = null,
    val windSpeed: Double? = null,
    val errorMessage: String? = null
)

class ExternalWeatherViewModel(
    private val repository: ExternalWeatherRepository = ExternalWeatherRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState(isLoading = true))
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun loadWeather() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            repository.fetchCurrentWeather()
                .onSuccess { weather ->
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        temperature = weather.temperature,
                        windSpeed = weather.windspeed,
                        errorMessage = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = WeatherUiState(
                        isLoading = false,
                        errorMessage = "Sin clima (${error.javaClass.simpleName})"
                    )
                }
        }
    }
}