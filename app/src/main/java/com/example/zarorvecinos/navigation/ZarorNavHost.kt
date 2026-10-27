package com.example.zarorvecinos.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.zarorvecinos.model.Borrador
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.viewmodel.Pantalla
import com.example.zarorvecinos.viewmodel.ZarorViewModel
import com.example.zarorvecinos.ui.theme.screens.ArriendosScreen
import com.example.zarorvecinos.ui.theme.screens.BorradoresScreen
import com.example.zarorvecinos.ui.theme.screens.FinanzasScreen
import com.example.zarorvecinos.ui.theme.components.FormularioAcciones
import com.example.zarorvecinos.ui.theme.screens.NuevaReservaScreen
import com.example.zarorvecinos.ui.theme.screens.NuevoMovimientoScreen
import com.example.zarorvecinos.ui.theme.screens.InicioScreen

@Composable
fun ZarorNavHost(
    p: Pantalla,
    reservas: List<Reserva>, movimientos: List<Movimiento>, borradores: List<Borrador>,
    vm: ZarorViewModel, habilitado: Boolean,
    onNotificaciones: () -> Unit, onAjustes: () -> Unit,
    onCancelar: (Reserva) -> Unit, onDescartar: (String) -> Unit
) {
    if (p.formulario != null) {
        val acciones = FormularioAcciones(
            onCambiar = vm::cambiar, onCaptura = vm::captura,
            onFoto = vm::foto, onRevisar = vm::revisar, onSalir = { vm.salir() },
            onCerrarResumen = vm::cerrarResumen, onGuardar = vm::guardar
        )
        if (p.formulario.tipo == "RESERVA") NuevaReservaScreen(p, acciones)
        else NuevoMovimientoScreen(p, acciones)
    } else when (p.seccion) {
        "INICIO" -> InicioScreen(
            reservasActivas = reservas.count { it.activa }, cantidadMovimientos = movimientos.size,
            cantidadBorradores = borradores.size, ocupado = p.ocupado, habilitado = habilitado,
            onNuevaReserva = { vm.abrir("RESERVA") }, onFinanzas = { vm.seccion("FINANZAS") },
            onNotificaciones = onNotificaciones, onAjustes = onAjustes
        )
        "RESERVAS" -> ArriendosScreen(reservas, p.ocupado, { vm.abrir("RESERVA") }, onCancelar)
        "FINANZAS" -> FinanzasScreen(movimientos, p.ocupado, { vm.abrir("FINANZAS") })
        "BORRADORES" -> BorradoresScreen(borradores, p.ocupado, { vm.abrir(it) }, onDescartar)
    }
}
