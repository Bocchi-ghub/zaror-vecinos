package com.example.zarorvecinos.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zarorvecinos.ui.theme.screens.ArriendosScreen
import com.example.zarorvecinos.ui.theme.screens.FinanzasScreen
import com.example.zarorvecinos.ui.theme.screens.InicioScreen

private object Rutas {
    const val INICIO = "inicio"
    const val ARRIENDOS = "arriendos"
    const val FINANZAS = "finanzas"
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
                }
            )
        }

        composable(route = Rutas.FINANZAS) {
            FinanzasScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}