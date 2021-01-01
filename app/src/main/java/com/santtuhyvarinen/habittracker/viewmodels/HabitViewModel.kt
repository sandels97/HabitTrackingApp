package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.HabitInfoUtil
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private var initialized = false

    private val databaseManager = DatabaseManager(getApplication())
    val iconManager = IconManager(application)

    private val habit : MutableLiveData<Habit> by lazy {
        MutableLiveData<Habit>()
    }

    //Set true to exit the fragment
    private val shouldExitView : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun initialize(id : Long) {
        if (initialized) return

        viewModelScope.launch {
            habit.value = fetchHabit(id)
        }

        initialized = true
    }

    fun getRecurrenceText(context: Context, habit: Habit) : String {
        val weekDaysSelectionModel = WeekDaysSelectionModel()
        CalendarUtil.parseRRULEtoWeekDaysSelectionModel(context, habit.taskRecurrence, weekDaysSelectionModel)

        return HabitInfoUtil.getRecurrenceHeader(context, weekDaysSelectionModel)
    }

    fun deleteHabit(context: Context) {
        val habitToDelete = habit.value

        if(habitToDelete != null) {

            val habitName = habitToDelete.name

            viewModelScope.launch {
                val rows = databaseManager.habitRepository.deleteHabit(habitToDelete)

                if(rows > 0) {
                    shouldExitView.value = true
                    Toast.makeText(context, context.getString(R.string.deleted, habitName), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.error_delete_habit), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getHabit() : LiveData<Habit> {
        return habit
    }

    fun getShouldExitView() : LiveData<Boolean> {
        return shouldExitView
    }

    private suspend fun fetchHabit(habitId : Long) : Habit? {
        return databaseManager.habitRepository.getHabitById(habitId)
    }
}