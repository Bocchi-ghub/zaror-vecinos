package com.example.zarorvecinos.external

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherBannerCard(
    modifier: Modifier = Modifier,
    weatherViewModel: ExternalWeatherViewModel = viewModel()
) {
    val state by weatherViewModel.uiState.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when {
                state.isLoading -> {
                    Text(
                        text = "Cargando clima del sector...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                }
                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage ?: "Error",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { weatherViewModel.loadWeather() }) {
                        Text("Reintentar")
                    }
                }
                else -> {
                    Column {
                        Text(
                            text = "Clima en la comunidad",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = "${state.temperature}°C  |  Viento: ${state.windSpeed} km/h",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(onClick = { weatherViewModel.loadWeather() }) {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}