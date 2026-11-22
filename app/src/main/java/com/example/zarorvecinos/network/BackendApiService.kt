package com.example.zarorvecinos.network

import com.example.zarorvecinos.model.LoginRequest
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface BackendApiService {

    // --- Usuarios y Roles ---
    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<Usuario>

    @POST("api/usuarios/registro")
    suspend fun registrar(@Body usuario: Usuario): Response<Usuario>

    // --- CRUD Reservas ---
    @GET("api/reservas")
    suspend fun obtenerReservas(): List<Reserva>

    @POST("api/reservas")
    suspend fun crearReserva(@Body reserva: Reserva): Reserva

    @PUT("api/reservas/{id}")
    suspend fun actualizarReserva(@Path("id") id: Int, @Body reserva: Reserva): Reserva

    @DELETE("api/reservas/{id}")
    suspend fun eliminarReserva(@Path("id") id: Int): Response<Unit>

    // --- CRUD Movimientos (Finanzas) ---
    @GET("api/movimientos")
    suspend fun obtenerMovimientos(): List<Movimiento>

    @POST("api/movimientos")
    suspend fun crearMovimiento(@Body movimiento: Movimiento): Movimiento

    @PUT("api/movimientos/{id}")
    suspend fun actualizarMovimiento(@Path("id") id: Int, @Body movimiento: Movimiento): Movimiento

    @DELETE("api/movimientos/{id}")
    suspend fun eliminarMovimiento(@Path("id") id: Int): Response<Unit>
}