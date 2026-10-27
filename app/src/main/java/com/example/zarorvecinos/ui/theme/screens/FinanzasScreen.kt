package com.example.zarorvecinos.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.validation.Validacion
import com.example.zarorvecinos.ui.theme.components.FotoVista
import com.example.zarorvecinos.ui.theme.components.pesos

@Composable
fun FinanzasScreen(movimientos: List<Movimiento>, ocupado: Boolean, onNuevoMovimiento: () -> Unit) {
    Text("Transparencia financiera", style = MaterialTheme.typography.headlineMedium)
    val ingresos = movimientos.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    val gastos = movimientos.filter { it.tipo == "GASTO" }.sumOf { it.monto }
    Text("Ingresos: ${pesos(ingresos)}")
    Text("Gastos: ${pesos(gastos)}")
    Text("Saldo: ${pesos(ingresos - gastos)}", style = MaterialTheme.typography.titleLarge)
    Button(onClick = { onNuevoMovimiento() }, enabled = !ocupado) { Text("Registrar movimiento / continuar borrador") }
    Text("Registro informativo. No realiza cobros ni transferencias.")
    if (movimientos.isEmpty()) Text("Aún no hay movimientos registrados.")
    movimientos.forEach { m ->
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(m.concepto, style = MaterialTheme.typography.titleMedium)
                Text("${m.tipo} · ${pesos(m.monto)}")
                Text(Validacion.mostrarFecha(m.fecha))
                m.uriFoto?.let { FotoVista(it) }
            }
        }
    }
}
