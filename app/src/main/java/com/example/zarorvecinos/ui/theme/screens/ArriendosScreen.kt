package com.example.zarorvecinos.ui.theme.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.ui.theme.components.ArriendoCard
import com.example.zarorvecinos.ui.theme.model.ArriendoUi
import com.example.zarorvecinos.ui.theme.model.EstadoArriendoUi
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme

@Composable
fun ArriendosScreen(
    onVolver: () -> Unit,
    arriendos: List<ArriendoUi>,
    modifier: Modifier = Modifier,
    mostrarAvisoDemo: Boolean = false
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Arriendos de la sede",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Text(
                text = "Consulta las reservas de la comunidad.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (mostrarAvisoDemo) {
            item {
                Text(
                    text = "Datos de demostración · No están guardados",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (arriendos.isEmpty()) {
            item {
                Text(
                    text = "No hay arriendos registrados.",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Text(
                    text = "Las reservas aparecerán aquí " +
                            "cuando estén disponibles.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(
                items = arriendos,
                key = { arriendo -> arriendo.id }
            ) { arriendo ->
                ArriendoCard(
                    arriendo = arriendo
                )
            }
        }

        item {
            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArriendosScreenPreview() {
    ZarorVecinosTheme {
        ArriendosScreen(
            onVolver = {},
            arriendos = listOf(
                ArriendoUi(
                    id = 1,
                    motivo = "Reunión comunitaria",
                    solicitante = "Vecino Demo 1",
                    fecha = "24/10/2026",
                    horaInicio = "16:00",
                    horaTermino = "18:00",
                    estado = EstadoArriendoUi.CONFIRMADO
                )
            ),
            mostrarAvisoDemo = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ArriendosVaciosPreview() {
    ZarorVecinosTheme {
        ArriendosScreen(
            onVolver = {},
            arriendos = emptyList()
        )
    }
}
