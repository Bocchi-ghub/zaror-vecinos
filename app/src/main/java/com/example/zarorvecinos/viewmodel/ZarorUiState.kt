package com.example.zarorvecinos.viewmodel

import com.example.zarorvecinos.model.Formulario
import com.example.zarorvecinos.model.Usuario

data class Pantalla(
    val seccion: String = "INICIO",
    val formulario: Formulario? = null,
    val errores: Map<String, String> = emptyMap(),
    val revisado: Boolean = false,
    val resumen: Boolean = false,
    val ocupado: Boolean = false,
    val capturando: Boolean = false,
    val error: String? = null,
    val mensaje: String? = null,
    // --- Nuevos campos para Roles (Spring Boot) y API Externa ---
    val usuarioActual: Usuario? = null,
    val rolUsuario: String = "VECINO", // "ADMIN" o "VECINO"
    val valorUf: String = "Cargando...",
    val valorDolar: String = ""
)