package com.example.zarorvecinos.ui.theme.components

import java.text.NumberFormat
import java.util.Locale

internal fun pesos(valor: Long): String = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }.format(valor)
