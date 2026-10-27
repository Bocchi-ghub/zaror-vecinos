package com.example.zarorvecinos.ui.theme.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale
import com.example.zarorvecinos.model.Formulario
import com.example.zarorvecinos.viewmodel.Pantalla
import com.example.zarorvecinos.validation.Validacion
import com.example.zarorvecinos.native.FotoManager
import com.example.zarorvecinos.ui.theme.components.FormularioAcciones
import com.example.zarorvecinos.ui.theme.components.Campo
import com.example.zarorvecinos.ui.theme.components.FotoVista
import com.example.zarorvecinos.ui.theme.components.Selector
import com.example.zarorvecinos.ui.theme.components.pesos

@Composable
internal fun FormularioScreen(p: Pantalla, acciones: FormularioAcciones) {
    val f = p.formulario ?: return
    val reserva = f.tipo == "RESERVA"
    val context = LocalContext.current
    var pendiente by rememberSaveable { mutableStateOf<String?>(null) }
    var errorCamara by rememberSaveable { mutableStateOf<String?>(null) }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        acciones.onCaptura(false)
        if (ok && pendiente != null) acciones.onFoto(pendiente)
        else { FotoManager.eliminarTemporal(context, pendiente); errorCamara = "No se capturó una foto. Puedes continuar sin comprobante." }
        pendiente = null
    }
    Text(if (reserva) "Nueva reserva" else "Registrar movimiento", style = MaterialTheme.typography.headlineMedium)
    Text("Usa datos ficticios. Los datos se guardan como borrador mientras editas.")
    if (reserva) {
        Text("Espacio: Sede vecinal")
        Campo("Solicitante", f.nombre, p.errores["nombre"], !p.ocupado) { valor -> acciones.onCambiar { it.copy(nombre = valor) } }
        Campo("Motivo del arriendo", f.concepto, p.errores["concepto"], !p.ocupado) { valor -> acciones.onCambiar { it.copy(concepto = valor) } }
        Campo("Asistentes", f.asistentes, p.errores["asistentes"], !p.ocupado, true) { valor -> acciones.onCambiar { it.copy(asistentes = valor) } }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = f.movimiento == "INGRESO", enabled = !p.ocupado, onClick = { acciones.onCambiar { it.copy(movimiento = "INGRESO") } }, label = { Text("Ingreso") })
            FilterChip(selected = f.movimiento == "GASTO", enabled = !p.ocupado, onClick = { acciones.onCambiar { it.copy(movimiento = "GASTO") } }, label = { Text("Gasto") })
        }
        Campo("Concepto", f.concepto, p.errores["concepto"], !p.ocupado) { valor -> acciones.onCambiar { it.copy(concepto = valor) } }
        Campo("Monto en pesos chilenos (sin puntos)", f.monto, p.errores["monto"], !p.ocupado, true) { valor -> acciones.onCambiar { it.copy(monto = valor) } }
    }
    Selector("Fecha", f.fecha, p.errores["fecha"], !p.ocupado) {
        val c = Calendar.getInstance()
        Validacion.fechaIso(f.fecha)?.let { iso -> c.set(iso.substring(0,4).toInt(), iso.substring(5,7).toInt()-1, iso.substring(8).toInt()) }
        DatePickerDialog(context, { _, y, m, d ->
            acciones.onCambiar { it.copy(fecha = String.format(Locale.ROOT, "%02d/%02d/%04d", d, m+1, y)) }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }
    if (reserva) {
        Selector("Hora de inicio", f.inicio, p.errores["inicio"], !p.ocupado) {
            val partes = f.inicio.split(":")
            TimePickerDialog(context, { _, h, m -> acciones.onCambiar { it.copy(inicio = String.format(Locale.ROOT, "%02d:%02d", h,m)) } }, partes.getOrNull(0)?.toIntOrNull() ?: 9, partes.getOrNull(1)?.toIntOrNull() ?: 0, true).show()
        }
        Selector("Hora de término", f.fin, p.errores["fin"], !p.ocupado) {
            val partes = f.fin.split(":")
            TimePickerDialog(context, { _, h, m -> acciones.onCambiar { it.copy(fin = String.format(Locale.ROOT, "%02d:%02d", h,m)) } }, partes.getOrNull(0)?.toIntOrNull() ?: 10, partes.getOrNull(1)?.toIntOrNull() ?: 0, true).show()
        }
    }
    Text("Comprobante o evidencia fotográfica (opcional)")
    OutlinedButton(enabled = !p.ocupado, onClick = {
        errorCamara = null
        try {
            val uri = FotoManager.nueva(context)
            pendiente = uri.toString()
            acciones.onCaptura(true)
            camara.launch(uri)
        } catch (_: Exception) {
            acciones.onCaptura(false)
            FotoManager.eliminarTemporal(context, pendiente)
            pendiente = null
            errorCamara = "No se pudo abrir una aplicación de cámara. Puedes continuar sin foto."
        }
    }) {
        Icon(painterResource(android.R.drawable.ic_menu_camera), contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(if (f.foto == null) "Tomar foto" else "Reemplazar foto")
    }
    f.foto?.let { uri ->
        FotoVista(uri)
        TextButton(onClick = { acciones.onFoto(null) }, enabled = !p.ocupado) { Text("Quitar foto del formulario") }
    }
    errorCamara?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    AnimatedVisibility(p.revisado && p.errores.isNotEmpty()) { Text("Corrige los campos marcados.", color = MaterialTheme.colorScheme.error) }
    Button(onClick = acciones.onRevisar, enabled = !p.ocupado, modifier = Modifier.fillMaxWidth()) { Text("Revisar datos") }
    OutlinedButton(onClick = { acciones.onSalir() }, enabled = !p.ocupado, modifier = Modifier.fillMaxWidth()) { Text("Guardar borrador y volver") }
    if (p.resumen) {
        AlertDialog(onDismissRequest = acciones.onCerrarResumen, title = { Text("Confirmar registro") },
            text = {
                Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (reserva) Text("${f.nombre} · Sede vecinal · ${f.asistentes} asistentes")
                    else Text("${f.movimiento} · ${pesos(f.monto.toLongOrNull() ?: 0)}")
                    Text(f.concepto)
                    Text(f.fecha)
                    if (reserva) Text("${f.inicio} a ${f.fin}")
                    Text(if (f.foto == null) "Sin foto adjunta" else "Con foto adjunta")
                    Text("Se guardará solo en este dispositivo.")
                    p.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            },
            confirmButton = { TextButton(onClick = acciones.onGuardar, enabled = !p.ocupado) { Text(if (p.ocupado) "Guardando..." else "Confirmar y guardar") } },
            dismissButton = { TextButton(onClick = acciones.onCerrarResumen, enabled = !p.ocupado) { Text("Seguir editando") } })
    }
}
