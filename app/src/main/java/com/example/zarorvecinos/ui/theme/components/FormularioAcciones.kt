package com.example.zarorvecinos.ui.theme.components

import com.example.zarorvecinos.model.Formulario

/** Eventos del formulario; no almacena datos ni contiene reglas de negocio. */
data class FormularioAcciones(
    val onCambiar: ((Formulario) -> Formulario) -> Unit,
    val onCaptura: (Boolean) -> Unit,
    val onFoto: (String?) -> Unit,
    val onRevisar: () -> Unit,
    val onSalir: () -> Unit,
    val onCerrarResumen: () -> Unit,
    val onGuardar: () -> Unit
)
