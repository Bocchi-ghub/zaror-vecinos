package com.example.zarorvecinos.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovimientoDao {
    @Query("SELECT * FROM movimientos ORDER BY fecha DESC, id DESC")
    abstract fun movimientos(): Flow<List<MovimientoEntity>>
    @Insert abstract suspend fun insertar(movimiento: MovimientoEntity): Long
    @Query("DELETE FROM borradores WHERE tipo = 'FINANZAS'")
    abstract suspend fun borrarBorradorFinanzas()
    @Transaction
    open suspend fun registrar(movimiento: MovimientoEntity) {
        insertar(movimiento)
        borrarBorradorFinanzas()
    }
}
