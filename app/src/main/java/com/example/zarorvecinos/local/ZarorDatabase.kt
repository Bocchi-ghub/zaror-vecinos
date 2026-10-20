package com.example.zarorvecinos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities =[ReservaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZarorDatabase: RoomDatabase(){
    abstract fun reservaDao(): ReservaDao

    companion object{
        @Volatile
        private var instancia: ZarorDatabase?=null

        fun obtenerInstancia(context: Context): ZarorDatabase{
            return  instancia ?: synchronized(this){
                val nuevaInstancia = Room.databaseBuilder(
                    context.applicationContext,
                    ZarorDatabase::class.java,
                    "zaror_vecinos.db"
                ).build()
                instancia=nuevaInstancia
                nuevaInstancia
            }
        }
    }
}