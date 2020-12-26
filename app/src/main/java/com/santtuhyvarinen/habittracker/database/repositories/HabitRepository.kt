package com.santtuhyvarinen.habittracker.database.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import com.santtuhyvarinen.habittracker.database.AppDatabase
import com.santtuhyvarinen.habittracker.database.dao.HabitDao
import com.santtuhyvarinen.habittracker.models.Habit

@Suppress("RedundantSuspendModifier")

class HabitRepository(private val habitDao: HabitDao) {
    val habits = habitDao.getAll()

    @WorkerThread
    suspend fun createHabit(habit : Habit) : Long {
        val id = habitDao.create(habit)
        Log.d(AppDatabase.DATABASE_LOG_TAG, "Habit inserted to database with id $id")

        return id
    }

    @WorkerThread
    suspend fun deleteHabit(habit : Habit) : Int {
        val rows = habitDao.delete(habit)

        Log.d(AppDatabase.DATABASE_LOG_TAG, "$rows rows deleted from the database")

        return rows
    }

    @WorkerThread
    suspend fun updateHabit(habit : Habit) : Int {
        val rows = habitDao.update(habit)

        Log.d(AppDatabase.DATABASE_LOG_TAG, "$rows rows updated in the database")

        return rows
    }

    @WorkerThread
    suspend fun getHabitById(id : Long) : Habit? {
        return habitDao.getById(id)
    }
}