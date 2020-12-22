package com.santtuhyvarinen.habittracker.database

import android.content.Context
import androidx.room.Room

class DatabaseManager {
    companion object {
        private const val DATABASE_NAME = "database_app"

        private var database : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            var db = database
            if(db == null) {
                db = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
            }

            database = db

            return db
        }
    }
}