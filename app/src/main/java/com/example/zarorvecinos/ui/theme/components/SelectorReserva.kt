package com.example.zarorvecinos.ui.theme.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
internal fun Selector(etiqueta: String, valor: String, error: String?, enabled: Boolean, abrir: () -> Unit) {
    OutlinedTextField(value = valor, onValueChange = {}, readOnly = true, enabled = enabled,
        label = { Text(etiqueta) }, isError = error != null, singleLine = true,
        leadingIcon = { Icon(painterResource(if (error != null) android.R.drawable.ic_dialog_alert else android.R.drawable.ic_menu_today), if (error != null) "Error" else "Fecha u hora") },
        supportingText = { if (error != null) Text(error) },
        trailingIcon = { TextButton(onClick = abrir, enabled = enabled) { Text("Elegir") } },
        modifier = Modifier.fillMaxWidth().animateContentSize())
}
