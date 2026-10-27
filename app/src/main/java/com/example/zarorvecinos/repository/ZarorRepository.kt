package com.example.zarorvecinos.repository

import android.content.Context
import com.example.zarorvecinos.local.ZarorDatabase
import com.example.zarorvecinos.model.Borrador
import com.example.zarorvecinos.model.Movimiento
import com.example.zarorvecinos.model.Reserva

/** Fachada de compatibilidad: conserva el contrato que ya utiliza el ViewModel. */
class ZarorRepository(context: Context) {
    private val db = ZarorDatabase.obtener(context)
    private val reservasRepo = ReservaRepository(db.reservaDao())
    private val finanzasRepo = FinanzasRepository(db.movimientoDao())
    private val borradoresRepo = BorradorRepository(db.borradorDao())
    val reservas = reservasRepo.reservas
    val movimientos = finanzasRepo.movimientos
    val borradores = borradoresRepo.borradores
    suspend fun guardar(r: Reserva) = reservasRepo.guardar(r)
    suspend fun guardar(m: Movimiento) = finanzasRepo.guardar(m)
    suspend fun guardar(b: Borrador) = borradoresRepo.guardar(b)
    suspend fun cancelar(id: Int) = reservasRepo.cancelar(id)
    suspend fun leer(tipo: String) = borradoresRepo.leer(tipo)
    suspend fun descartar(tipo: String) = borradoresRepo.descartar(tipo)
}
