package com.example.zarorvecinos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.zarorvecinos.navigation.ZarorNavHost
import com.example.zarorvecinos.ui.theme.ZarorVecinosTheme
import com.example.zarorvecinos.ui.theme.screens.PruebaRoomScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ZarorVecinosTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ZarorNavHost(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}