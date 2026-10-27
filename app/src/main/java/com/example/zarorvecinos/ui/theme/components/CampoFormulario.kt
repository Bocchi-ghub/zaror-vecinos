package com.example.zarorvecinos.ui.theme.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType

@Composable
internal fun Campo(etiqueta: String, valor: String, error: String?, enabled: Boolean, numerico: Boolean = false, onChange: (String) -> Unit) {
    OutlinedTextField(value = valor, onValueChange = onChange, enabled = enabled,
        label = { Text(etiqueta) }, singleLine = true, isError = error != null,
        supportingText = { if (error != null) Text(error) },
        trailingIcon = { if (error != null) Icon(painterResource(android.R.drawable.ic_dialog_alert), "Error") },
        keyboardOptions = KeyboardOptions(keyboardType = if (numerico) KeyboardType.Number else KeyboardType.Text),
        modifier = Modifier.fillMaxWidth().animateContentSize())
}
