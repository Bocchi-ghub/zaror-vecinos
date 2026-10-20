package com.example.zarorvecinos.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zarorvecinos.ui.theme.screens.ArriendosScreen
import com.example.zarorvecinos.ui.theme.screens.FinanzasScreen
import com.example.zarorvecinos.ui.theme.screens.InicioScreen

import com.example.zarorvecinos.ui.theme.model.ArriendoUi
import com.example.zarorvecinos.ui.theme.model.EstadoArriendoUi
import com.example.zarorvecinos.ui.theme.screens.NuevaReservaScreen

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zarorvecinos.viewmodel.NuevaReservaViewModel

private object Rutas {
    const val INICIO = "inicio"
    const val ARRIENDOS = "arriendos"
    const val FINANZAS = "finanzas"
    const val NUEVA_RESERVA = "nueva_reserva"
}
@Composable
fun ZarorNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.INICIO,
        modifier = modifier
    ) {
        composable(route = Rutas.INICIO) {
            InicioScreen(
                onArriendosClick = {
                    navController.navigate(Rutas.ARRIENDOS) {
                        launchSingleTop = true
                    }
                },
                onFinanzasClick = {
                    navController.navigate(Rutas.FINANZAS) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = Rutas.ARRIENDOS) {
            ArriendosScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onNuevaReserva = {
                    navController.navigate(Rutas.NUEVA_RESERVA) {
                        launchSingleTop = true
                    }
                },
                arriendos = listOf(
                    ArriendoUi(
                        id = 1,
                        motivo = "Reunión comunitaria",
                        solicitante = "Vecino Demo 1",
                        fecha = "24/10/2026",
                        horaInicio = "16:00",
                        horaTermino = "18:00",
                        estado = EstadoArriendoUi.CONFIRMADO
                    ),
                    ArriendoUi(
                        id = 2,
                        motivo = "Taller vecinal",
                        solicitante = "Vecino Demo 2",
                        fecha = "25/10/2026",
                        horaInicio = "10:00",
                        horaTermino = "12:00",
                        estado = EstadoArriendoUi.PENDIENTE
                    ),
                    ArriendoUi(
                        id = 3,
                        motivo = "Actividad recreativa",
                        solicitante = "Vecino Demo 3",
                        fecha = "26/10/2026",
                        horaInicio = "15:00",
                        horaTermino = "17:00",
                        estado = EstadoArriendoUi.CANCELADO
                    )
                ),
                mostrarAvisoDemo = true
            )
        }

        composable(route = Rutas.FINANZAS) {
            FinanzasScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Rutas.NUEVA_RESERVA) {
            val reservaViewModel: NuevaReservaViewModel = viewModel()

            val uiState by reservaViewModel.uiState
                .collectAsStateWithLifecycle()

            NuevaReservaScreen(
                uiState = uiState,
                onCampoChange = reservaViewModel::actualizarCampo,
                onRevisarDatos = reservaViewModel::revisarDatos,
                onCerrarResumen = reservaViewModel::cerrarResumen,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        }
    }
