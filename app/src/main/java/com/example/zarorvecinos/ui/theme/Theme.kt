package com.example.zarorvecinos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ZarorVecinosTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) darkColorScheme(primary = VerdeOscuro)
        else lightColorScheme(primary = VerdePrincipal, secondary = VerdeSecundario)
    MaterialTheme(colorScheme = colors, typography = ZarorTypography, content = content)
}
