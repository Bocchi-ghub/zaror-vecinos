package com.example.zarorvecinos.repository

import android.content.Context
import com.example.zarorvecinos.local.ZarorDatabase
import com.example.zarorvecinos.model.Borrador
import com.example.zarorvecinos.model.LoginRequest
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.model.Reserva
import com.example.zarorvecinos.model.Usuario
import com.example.zarorvecinos.network.BackendRetrofitClient
import com.example.zarorvecinos.network.ExternalRetrofitClient
import com.example.zarorvecinos.network.IndicadoresResponse

class ZarorRepository(context: Context) {
    private val db = ZarorDatabase.obtener(context)
    private val reservasRepo = ReservaRepository(db.reservaDao())
    private val finanzasRepo = FinanzasRepository(db.movimientoDao())
    private val borradoresRepo = BorradorRepository(db.borradorDao())

    val reservas = reservasRepo.reservas
    val movimientos = finanzasRepo.movimientos
    val borradores = borradoresRepo.borradores

    // --- Sincronización inicial con Spring Boot ---
    suspend fun sincronizarConBackend() {
        reservasRepo.sincronizar()
        finanzasRepo.sincronizar()
    }

    // --- CRUD Reservas ---
    suspend fun guardar(r: Reserva) = reservasRepo.guardar(r)
    suspend fun actualizar(r: Reserva) = reservasRepo.actualizar(r)
    suspend fun cancelar(id: Int) = reservasRepo.cancelar(id)
    suspend fun eliminarReserva(id: Int) = reservasRepo.eliminar(id)

    // --- CRUD Movimientos (Finanzas) ---
    suspend fun guardar(m: Movimiento) = finanzasRepo.guardar(m)
    suspend fun actualizar(m: Movimiento) = finanzasRepo.actualizar(m)
    suspend fun eliminarMovimiento(id: Int) = finanzasRepo.eliminar(id)

    // --- Borradores locales ---
    suspend fun guardar(b: Borrador) = borradoresRepo.guardar(b)
    suspend fun leer(tipo: String) = borradoresRepo.leer(tipo)
    suspend fun descartar(tipo: String) = borradoresRepo.descartar(tipo)

    // --- Autenticación y Roles (Spring Boot) ---
    suspend fun login(email: String, password: String): Result<Usuario> = runCatching {
        val response = BackendRetrofitClient.api.login(LoginRequest(email.trim(), password))
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw IllegalArgumentException("Correo o contraseña incorrectos")
        }
    }

    suspend fun registrarUsuario(usuario: Usuario): Result<Usuario> = runCatching {
        val response = BackendRetrofitClient.api.registrar(usuario)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw IllegalStateException("No se pudo registrar el usuario")
        }
    }

    // --- API Externa (UF y Dólar) ---
    suspend fun obtenerIndicadores(): Result<IndicadoresResponse> = runCatching {
        ExternalRetrofitClient.api.obtenerIndicadores()
    }
}