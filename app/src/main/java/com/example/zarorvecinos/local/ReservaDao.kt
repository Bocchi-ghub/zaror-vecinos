package com.example.zarorvecinos.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.zarorvecinos.model.ConflictoReserva

@Dao
abstract class ReservaDao {
    @Query("SELECT * FROM reservas ORDER BY fecha DESC, horaInicio DESC")
    abstract fun reservas(): Flow<List<ReservaEntity>>
    @Query("SELECT COUNT(*) FROM reservas WHERE id != :id AND activa = 1 AND trim(espacio) = trim(:espacio) COLLATE NOCASE AND fecha = :fecha AND :inicio < horaTermino AND :fin > horaInicio")
    abstract suspend fun conflictos(id: Int, espacio: String, fecha: String, inicio: String, fin: String): Int
    @Insert abstract suspend fun insertar(reserva: ReservaEntity): Long
    @Update abstract suspend fun actualizar(reserva: ReservaEntity)
    @Query("UPDATE reservas SET activa = 0 WHERE id = :id")
    abstract suspend fun cancelar(id: Int)
    @Query("DELETE FROM borradores WHERE tipo = 'RESERVA'")
    abstract suspend fun borrarBorradorReserva()
    @Transaction
    open suspend fun registrar(reserva: ReservaEntity) {
        if (conflictos(reserva.id, reserva.espacio, reserva.fecha, reserva.horaInicio, reserva.horaTermino) > 0) throw ConflictoReserva()
        if (reserva.id == 0) insertar(reserva) else actualizar(reserva)
        borrarBorradorReserva()
    }
}
