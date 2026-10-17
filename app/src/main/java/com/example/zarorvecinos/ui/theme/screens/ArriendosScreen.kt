package com.example.zarorvecinos.ui.theme.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArriendosScreen(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Arriendos de la sede",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Aquí podrás consultar y gestionar las reservas " +
                    "de la sede vecinal.",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "El listado de reservas se implementará " +
                    "en la siguiente etapa.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onVolver
        ) {
            Text("Volver al inicio")
        }
    }
}
