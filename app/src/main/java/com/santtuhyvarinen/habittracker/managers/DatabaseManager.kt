package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import com.santtuhyvarinen.habittracker.database.AppDatabase
import com.santtuhyvarinen.habittracker.database.HabitRepository

class DatabaseManager(context : Context) {
    private val db = AppDatabase.getDatabase(context)
    val habitRepository = HabitRepository(db.habitDao())
}