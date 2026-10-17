package com.example.zarorvecinos.ui.theme.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme

@Composable
fun InicioScreen(
    onArriendosClick: () -> Unit,
    onFinanzasClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Zaror Vecinos",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Bienvenido a tu comunidad",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Gestiona el uso de la sede vecinal y consulta " +
                    "los movimientos de la organización.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Arriendos de la sede",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Consulta y administra las reservas.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onArriendosClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver arriendos")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Transparencia financiera",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Consulta los ingresos y gastos registrados.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = onFinanzasClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver finanzas")
                }
            }
        }

        Text(
            text = "Prototipo académico · Datos ficticios",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    ZarorVecinosTheme {
        InicioScreen(
            onArriendosClick = {},
            onFinanzasClick = {}
        )
    }
}
