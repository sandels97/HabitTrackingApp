package com.santtuhyvarinen.habittracker.database

import android.content.Context

class DatabaseManager(context : Context) {
    private val db = AppDatabase.getDatabase(context)
    val habitRepository = HabitRepository(db.habitDao())
}