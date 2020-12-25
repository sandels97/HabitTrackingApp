package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.santtuhyvarinen.habittracker.database.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HabitViewModel : ViewModel() {
    private var initialized = false
    private var habitId : Long = -1

    lateinit var databaseManager : DatabaseManager
    val iconManager = IconManager()

    val habit : MutableLiveData<Habit> by lazy {
        MutableLiveData<Habit>()
    }


    fun initialize(context: Context, id : Long) {
        if (initialized) return

        habitId = id
        databaseManager = DatabaseManager(context)
        iconManager.loadIcons(context)

        viewModelScope.launch {
            habit.value = getHabit()
        }

        initialized = true
    }

    private suspend fun getHabit() : Habit {
        return databaseManager.habitRepository.getHabitById(habitId)
    }
}