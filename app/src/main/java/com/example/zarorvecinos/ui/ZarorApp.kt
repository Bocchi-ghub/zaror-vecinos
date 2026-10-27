package com.example.zarorvecinos.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.viewmodel.ZarorViewModel
import com.example.zarorvecinos.native.NotificacionesManager
import com.example.zarorvecinos.navigation.ZarorNavHost
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarorApp(vm: ZarorViewModel, solicitud: Pair<String, Long>?) {
    val p by vm.pantalla.collectAsStateWithLifecycle()
    val reservas by vm.reservas.collectAsStateWithLifecycle()
    val movimientos by vm.movimientos.collectAsStateWithLifecycle()
    val borradores by vm.borradores.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    var versionPermiso by remember { mutableIntStateOf(0) }
    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permitido ->
        versionPermiso++
        if (!permitido) android.widget.Toast.makeText(context, "Los registros se guardarán aunque no autorices notificaciones.", android.widget.Toast.LENGTH_LONG).show()
    }
    DisposableEffect(context, vm) {
        val owner = context as? LifecycleOwner
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) vm.segundoPlano()
            if (event == Lifecycle.Event.ON_RESUME) versionPermiso++
        }
        owner?.lifecycle?.addObserver(observer)
        onDispose { owner?.lifecycle?.removeObserver(observer) }
    }
    LaunchedEffect(solicitud) {
        solicitud?.first?.takeIf { it in listOf("FINANZAS", "BORRADORES") }?.let(vm::seccion)
    }
    LaunchedEffect(p.mensaje) { p.mensaje?.let { snackbar.showSnackbar(it); vm.mensajeConsumido() } }
    BackHandler(enabled = p.formulario != null || p.seccion != "INICIO") {
        if (!p.ocupado) {
            if (p.formulario != null) vm.salir() else vm.seccion("INICIO")
        }
    }
    var confirmarCancelacion by remember { mutableStateOf<Reserva?>(null) }
    var confirmarBorrado by remember { mutableStateOf<String?>(null) }
    ZarorVecinosTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Zaror Vecinos") }) },
            snackbarHost = { SnackbarHost(snackbar) }
        ) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                if (p.formulario == null) {
                    val secciones = listOf("INICIO", "RESERVAS", "FINANZAS", "BORRADORES")
                    TabRow(selectedTabIndex = secciones.indexOf(p.seccion).coerceAtLeast(0)) {
                        secciones.forEach { seccion ->
                            Tab(selected = p.seccion == seccion, enabled = !p.ocupado,
                                onClick = { vm.seccion(seccion) }, text = {
                                    Text(when(seccion) { "INICIO" -> "Inicio"; "RESERVAS" -> "Reservas"; "FINANZAS" -> "Finanzas"; else -> "Borradores" }, maxLines = 1)
                                })
                        }
                    }
                }
                if (p.ocupado) LinearProgressIndicator(Modifier.fillMaxWidth())
                Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ZarorNavHost(
                        p = p, reservas = reservas, movimientos = movimientos, borradores = borradores,
                        vm = vm, habilitado = remember(versionPermiso) { NotificacionesManager(context).permitido() },
                        onNotificaciones = {
                            val habilitado = NotificacionesManager(context).permitido()
                            if (Build.VERSION.SDK_INT >= 33 && !habilitado) permiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                            else context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName))
                        },
                        onAjustes = { context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)) },
                        onCancelar = { confirmarCancelacion = it }, onDescartar = { confirmarBorrado = it }
                    )
                    AnimatedVisibility(p.error != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(painterResource(android.R.drawable.ic_dialog_alert), "Error", tint = MaterialTheme.colorScheme.error)
                                Text(p.error.orEmpty(), color = MaterialTheme.colorScheme.error)
                            }
                            TextButton(onClick = vm::limpiarError) { Text("Cerrar aviso") }
                        }
                    }
                    if (p.formulario == null && p.seccion != "INICIO") OutlinedButton(onClick = { vm.seccion("INICIO") }) { Text("Volver al inicio") }
                }
            }
        }
        confirmarCancelacion?.let { reserva ->
            AlertDialog(onDismissRequest = { confirmarCancelacion = null }, title = { Text("¿Cancelar reserva?") },
                text = { Text("Se conservará el registro y se liberará el horario.") },
                confirmButton = { TextButton(onClick = { vm.cancelar(reserva.id); confirmarCancelacion = null }) { Text("Cancelar reserva") } },
                dismissButton = { TextButton(onClick = { confirmarCancelacion = null }) { Text("Mantener") } })
        }
        confirmarBorrado?.let { tipo ->
            AlertDialog(onDismissRequest = { confirmarBorrado = null }, title = { Text("¿Descartar borrador?") },
                text = { Text("Se eliminarán los datos de este borrador.") },
                confirmButton = { TextButton(onClick = { vm.descartar(tipo); confirmarBorrado = null }) { Text("Descartar") } },
                dismissButton = { TextButton(onClick = { confirmarBorrado = null }) { Text("Conservar") } })
        }
    }
}
