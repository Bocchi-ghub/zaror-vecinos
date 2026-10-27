package com.example.zarorvecinos.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BorradorDao {
    @Query("SELECT * FROM borradores ORDER BY actualizado DESC")
    fun borradores(): Flow<List<BorradorEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(borrador: BorradorEntity)
    @Query("DELETE FROM borradores WHERE tipo = :tipo")
    suspend fun descartar(tipo: String)
    @Query("SELECT * FROM borradores WHERE tipo = :tipo LIMIT 1")
    suspend fun leer(tipo: String): BorradorEntity?
}
