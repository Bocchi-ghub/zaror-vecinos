package com.example.zarorvecinos.model

data class Usuario(
    val id: Int = 0,
    val nombre: String = "",
    val email: String,
    val password: String = "",
    val rol: String = "VECINO" // "ADMIN" o "VECINO"
)

data class LoginRequest(
    val email: String,
    val password: String
)