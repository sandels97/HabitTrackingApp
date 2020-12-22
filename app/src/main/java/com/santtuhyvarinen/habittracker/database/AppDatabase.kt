package com.santtuhyvarinen.habittracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.santtuhyvarinen.habittracker.models.Habit

@Database(entities = arrayOf(Habit::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao() : HabitDao
}