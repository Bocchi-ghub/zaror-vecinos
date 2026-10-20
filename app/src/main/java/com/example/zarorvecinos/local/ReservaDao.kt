package com.example.zarorvecinos.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao{
    @Query("SELECT * FROM reservas ORDER BY fecha DESC")
    fun obtenerReservas(): Flow<List<ReservaEntity>>
    @Query("SELECT * FROM reservas WHERE id = :id")
    fun obtenerReservaPorId(id: Int): ReservaEntity?

    @Insert
    suspend fun insertarReserva(reserva: ReservaEntity)

    @Update
    suspend fun actualizarReserva(reserva: ReservaEntity)

    @Delete
    suspend fun eliminarReserva(reserva: ReservaEntity)

    @Query(
        """
            SELECT COUNT (*) FROM reservas
            WHERE espacio = :espacio
            AND fecha = :fecha
            AND activa = 1
            AND(
                :horaInicio < horaTermino
                AND :horaTermino > horaInicio
            )
            """
    )
    suspend fun  contarReservasEnConflicto(
        espacio: String,
        fecha  : String,
        horaInicio: String,
        horaTermino: String
    ): Int
}