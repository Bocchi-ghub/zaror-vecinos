package com.example.zarorvecinos.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zarorvecinos.ui.theme.screens.ArriendosScreen
import com.example.zarorvecinos.ui.theme.screens.FinanzasScreen
import com.example.zarorvecinos.ui.theme.screens.InicioScreen


import com.example.zarorvecinos.ui.theme.screens.NuevaReservaScreen

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zarorvecinos.viewmodel.NuevaReservaViewModel
import com.example.zarorvecinos.viewmodel.ReservaViewModel
import androidx.compose.runtime.LaunchedEffect

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
            val reservasViewModel: ReservaViewModel = viewModel()

            val reservas by reservasViewModel.reservas
                .collectAsStateWithLifecycle()

            ArriendosScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onNuevaReserva = {
                    navController.navigate(Rutas.NUEVA_RESERVA) {
                        launchSingleTop = true
                    }
                },
                reservas = reservas
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

            LaunchedEffect(uiState.reservaGuardada) {
                if (uiState.reservaGuardada) {
                    navController.popBackStack()
                }
            }

            NuevaReservaScreen(
                uiState = uiState,
                onCampoChange = reservaViewModel::actualizarCampo,
                onRevisarDatos = reservaViewModel::revisarDatos,
                onGuardarReserva = reservaViewModel::guardarReserva,
                onCerrarResumen = reservaViewModel::cerrarResumen,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        }
    }
