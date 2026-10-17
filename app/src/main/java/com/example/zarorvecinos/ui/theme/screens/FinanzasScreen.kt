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
fun FinanzasScreen(
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
            text = "Transparencia financiera",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Aquí podrás consultar los ingresos y gastos " +
                    "de la junta de vecinos.",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Prototipo académico: todos los movimientos " +
                    "y montos serán ficticios.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onVolver
        ) {
            Text("Volver al inicio")
        }
    }
}
