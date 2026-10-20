package com.example.zarorvecinos.ui.theme.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.ui.theme.model.ArriendoUi
import com.example.zarorvecinos.ui.theme.model.EstadoArriendoUi

@Composable
fun ArriendoCard(
    arriendo: ArriendoUi,
    modifier: Modifier = Modifier
) {
    val colorFondo = when (arriendo.estado) {
        EstadoArriendoUi.PENDIENTE ->
            MaterialTheme.colorScheme.tertiaryContainer

        EstadoArriendoUi.CONFIRMADO ->
            MaterialTheme.colorScheme.primaryContainer

        EstadoArriendoUi.CANCELADO ->
            MaterialTheme.colorScheme.errorContainer
    }

    val colorTexto = when (arriendo.estado) {
        EstadoArriendoUi.PENDIENTE ->
            MaterialTheme.colorScheme.onTertiaryContainer

        EstadoArriendoUi.CONFIRMADO ->
            MaterialTheme.colorScheme.onPrimaryContainer

        EstadoArriendoUi.CANCELADO ->
            MaterialTheme.colorScheme.onErrorContainer
    }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = arriendo.motivo,
                style = MaterialTheme.typography.titleMedium
            )

            Surface(
                color = colorFondo,
                contentColor = colorTexto,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = arriendo.estado.etiqueta,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
                )
            }

            Text(
                text = "Solicitante: ${arriendo.solicitante}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Fecha: ${arriendo.fecha}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Horario: ${arriendo.horaInicio} - " +
                        arriendo.horaTermino,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}