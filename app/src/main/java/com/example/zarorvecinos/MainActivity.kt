package com.example.zarorvecinos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zarorvecinos.ui.ZarorApp
import com.example.zarorvecinos.viewmodel.ZarorViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue



class MainActivity : ComponentActivity() {
    private var solicitud by mutableStateOf<Pair<String, Long>?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        recibir(intent)
        setContent {
            val vm: ZarorViewModel = viewModel()
            ZarorApp(vm, solicitud)
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        recibir(intent)
    }
    private fun recibir(intent: Intent?) {
        intent?.getStringExtra("destino")?.let { solicitud = it to System.nanoTime() }
        intent?.removeExtra("destino")
    }
}
