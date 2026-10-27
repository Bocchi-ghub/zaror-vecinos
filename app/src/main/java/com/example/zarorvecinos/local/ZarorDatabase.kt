package com.example.zarorvecinos.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ReservaEntity::class, MovimientoEntity::class, BorradorEntity::class], version = 3, exportSchema = false)
abstract class ZarorDatabase : RoomDatabase() {
    abstract fun reservaDao(): ReservaDao
    abstract fun movimientoDao(): MovimientoDao
    abstract fun borradorDao(): BorradorDao
    companion object {
        @Volatile private var instance: ZarorDatabase? = null
        private val unoDos = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE reservas ADD COLUMN uriFoto TEXT")
                db.execSQL("ALTER TABLE reservas ADD COLUMN latitud REAL")
                db.execSQL("ALTER TABLE reservas ADD COLUMN longitud REAL")
            }
        }
        private val dosTres = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS movimientos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, concepto TEXT NOT NULL, tipo TEXT NOT NULL, monto INTEGER NOT NULL, fecha TEXT NOT NULL, uriFoto TEXT)")
                db.execSQL("CREATE TABLE IF NOT EXISTS borradores (tipo TEXT NOT NULL PRIMARY KEY, contenido TEXT NOT NULL, actualizado INTEGER NOT NULL)")
                db.execSQL("UPDATE reservas SET fecha = substr(fecha,7,4) || '-' || substr(fecha,4,2) || '-' || substr(fecha,1,2) WHERE length(fecha)=10 AND substr(fecha,3,1)='/' AND substr(fecha,6,1)='/'")
            }
        }
        fun obtener(context: Context): ZarorDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, ZarorDatabase::class.java, "zaror_vecinos.db")
                .addMigrations(unoDos, dosTres).build().also { instance = it }
        }
    }
}
