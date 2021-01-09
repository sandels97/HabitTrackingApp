package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.IconModel
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.HabitInfoUtil
import kotlinx.coroutines.launch

class HabitFormViewModel(application: Application) : AndroidViewModel(application) {

    private var initialized = false

    var loading = false

    var habitId : Long = -1L
    val habitData : MutableLiveData<Habit> = MutableLiveData<Habit>()

    val habitDataSaved : MutableLiveData<Long> = MutableLiveData<Long>()

    lateinit var databaseManager : DatabaseManager
    val iconManager = IconManager(application)

    var habitName = ""
    var priorityValue = 0
    var weekDaysSelectionModel = WeekDaysSelectionModel()
    var selectedIconModel : IconModel? = null


    fun initialize(id : Long) {
        if(initialized) return

        databaseManager = DatabaseManager(getApplication())

        habitId = id

        if(isEditingExistingHabit()) {
            loading = true
            viewModelScope.launch {
                //Load existing habit
                val loadedHabit = getHabit()

                //Load values
                if(loadedHabit != null) {
                    val selectedModel = iconManager.getIconModelByKey(loadedHabit.iconKey)
                    selectedIconModel = selectedModel
                    CalendarUtil.parseRRULEtoWeekDaysSelectionModel(loadedHabit.taskRecurrence, weekDaysSelectionModel)
                }

                habitData.value = loadedHabit
            }
        }

        initialized = true
    }

    fun saveHabit(context: Context) {
        if(habitName.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_name_empty), Toast.LENGTH_LONG).show()
            return
        }

        val currentTime = System.currentTimeMillis()

        val habit : Habit
        if(isEditingExistingHabit()) {
            habit = habitData.value as Habit
        } else {
            habit = Habit()
            habit.creationDate = currentTime
        }

        habit.name = habitName

        habit.modificationDate = currentTime

        habit.taskRecurrence = CalendarUtil.getRRuleFromWeekDaysSelectionModel(weekDaysSelectionModel)

        val selectedIconModel = selectedIconModel
        if(selectedIconModel == null) {
            habit.iconKey = null
        } else {
            habit.iconKey = selectedIconModel.key
        }

        habit.priority = priorityValue

        viewModelScope.launch {
            if(isEditingExistingHabit()) {
                updateHabit(habit)
                habitDataSaved.value = habitId
            } else {
                val id = insertHabit(habit)
                habitDataSaved.value = id
            }
        }
    }

    fun getRecurrenceHeader(context: Context) : String {
        return HabitInfoUtil.getRecurrenceHeader(context, weekDaysSelectionModel)
    }

    private suspend fun insertHabit(habit: Habit) : Long {
        return databaseManager.habitRepository.createHabit(habit)
    }

    private suspend fun updateHabit(habit: Habit) : Boolean {
        val rows = databaseManager.habitRepository.updateHabit(habit)
        return rows > 0
    }

    fun isEditingExistingHabit() : Boolean {
        return habitId >= 0
    }

    private suspend fun getHabit() : Habit? {
        return databaseManager.habitRepository.getHabitById(habitId)
    }
}