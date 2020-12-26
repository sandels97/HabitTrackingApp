package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import com.santtuhyvarinen.habittracker.database.AppDatabase
import com.santtuhyvarinen.habittracker.database.repositories.HabitRepository
import com.santtuhyvarinen.habittracker.database.repositories.TaskLogRepository

class DatabaseManager(context : Context) {
    private val db = AppDatabase.getDatabase(context)
    val habitRepository = HabitRepository(db.habitDao())
    val taskLogRepository = TaskLogRepository(db.taskLogDao())
}