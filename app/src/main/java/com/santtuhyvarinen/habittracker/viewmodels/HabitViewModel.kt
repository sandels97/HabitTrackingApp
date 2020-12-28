package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.HabitInfoManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private var initialized = false
    private var habitId : Long = -1

    private lateinit var databaseManager : DatabaseManager
    val habitInfoManager = HabitInfoManager()
    val iconManager = IconManager()

    val habit : MutableLiveData<Habit> by lazy {
        MutableLiveData<Habit>()
    }

    fun initialize(id : Long) {
        if (initialized) return

        habitId = id
        databaseManager = DatabaseManager(getApplication())

        iconManager.loadIcons(getApplication())

        viewModelScope.launch {
            habit.value = getHabit()
        }

        initialized = true
    }

    fun getRecurrenceText(context: Context, habit: Habit) : String {
        val weekDaysSelectionModel = WeekDaysSelectionModel()
        CalendarUtil.parseRRULEtoWeekDaysSelectionModel(context, habit.taskRecurrence, weekDaysSelectionModel)

        return habitInfoManager.getRecurrenceHeader(context, weekDaysSelectionModel)
    }

    fun deleteHabit(context: Context) {
        val habitToDelete = habit.value

        if(habitToDelete != null) {

            val habitName = habitToDelete.name

            viewModelScope.launch {
                val rows = databaseManager.habitRepository.deleteHabit(habitToDelete)

                if(rows > 0) {
                    habit.value = null
                    Toast.makeText(context, context.getString(R.string.deleted, habitName), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.error_delete_habit), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private suspend fun getHabit() : Habit? {
        return databaseManager.habitRepository.getHabitById(habitId)
    }
}