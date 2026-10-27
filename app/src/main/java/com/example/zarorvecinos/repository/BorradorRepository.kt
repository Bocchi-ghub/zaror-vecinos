package com.example.zarorvecinos.repository

import com.example.zarorvecinos.local.BorradorDao
import com.example.zarorvecinos.model.Borrador
import kotlinx.coroutines.flow.map

class BorradorRepository(private val dao: BorradorDao) {
    val borradores = dao.borradores().map { lista -> lista.map { it.aModelo() } }
    suspend fun guardar(borrador: Borrador) = dao.guardar(borrador.aEntidad())
    suspend fun leer(tipo: String) = dao.leer(tipo)?.aModelo()
    suspend fun descartar(tipo: String) = dao.descartar(tipo)
}
