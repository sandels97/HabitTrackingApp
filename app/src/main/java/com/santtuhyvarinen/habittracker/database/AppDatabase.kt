package com.santtuhyvarinen.habittracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.santtuhyvarinen.habittracker.models.Habit

@Database(entities = arrayOf(Habit::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao() : HabitDao

    companion object {
        private const val DATABASE_NAME = "database_app"
        const val DATABASE_LOG_TAG = "database_log"

        @Volatile
        private var DATABASE_INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            return DATABASE_INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME).build()

                DATABASE_INSTANCE = instance

                return@synchronized instance
            }
        }
    }
}