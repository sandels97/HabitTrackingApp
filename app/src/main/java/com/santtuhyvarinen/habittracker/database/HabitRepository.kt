package com.santtuhyvarinen.habittracker.database

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.santtuhyvarinen.habittracker.database.AppDatabase.Companion.DATABASE_LOG_TAG
import com.santtuhyvarinen.habittracker.models.Habit
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("RedundantSuspendModifier")

class HabitRepository(private val habitDao: HabitDao) {
    val habits = habitDao.getAll()

    @WorkerThread
    suspend fun createHabit(habit : Habit) : Long {
        val id = habitDao.create(habit)
        Log.d(DATABASE_LOG_TAG, "Habit inserted to database with id $id")

        return id
    }

    @WorkerThread
    suspend fun deleteHabit(habit : Habit) {
        habitDao.delete(habit)

        Log.d(DATABASE_LOG_TAG, "Habit deleted from database")
    }

    @WorkerThread
    suspend fun getHabitById(id : Long) : Habit {
        return habitDao.getById(id)
    }
}