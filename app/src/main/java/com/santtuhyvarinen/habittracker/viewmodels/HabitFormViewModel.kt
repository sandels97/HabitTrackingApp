package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.database.AppDatabase
import com.santtuhyvarinen.habittracker.database.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.IconModel
import kotlinx.coroutines.launch

class HabitFormViewModel : ViewModel() {

    private var initialized = false

    var loading = false

    var habitId : Long = -1L

    lateinit var databaseManager : DatabaseManager

    var priorityLevels : Array<String> = Array (0) { "" }
    var priorityValue = 0
    var selectedWeekDayButtons = Array(7) { false }
    var selectedIconModel : IconModel? = null

    val iconManager = IconManager()

    fun initialize(context: Context, id : Long) {
        if(initialized) return

        databaseManager = DatabaseManager(context)

        habitId = id

        iconManager.loadIcons(context)
        priorityLevels = context.resources.getStringArray(R.array.PriorityLevels)

        initialized = true
    }

    fun getMaxPriorityLevel() : Int {
        return priorityLevels.size - 1
    }

    fun getCurrentPriorityLevelText() : String {
        if(priorityLevels.isEmpty()) return ""

        val index = priorityValue.coerceAtLeast(0).coerceAtMost(priorityLevels.size-1)
        return priorityLevels[index]
    }

    fun getWeekDaysSelectedText(context: Context) : String {
        val daysSelected = daysSelected()

        val weekDays = if(daysSelected < 3) context.resources.getStringArray(R.array.WeekDays) else context.resources.getStringArray(R.array.WeekDaysShort)

        var index = 0

        val stringBuilder = StringBuilder()
        for(i in weekDays.indices) {
            if(selectedWeekDayButtons[i]) {
                val weekDay = weekDays[i]
                stringBuilder.append(weekDay)
                index ++

                if(daysSelected > 1) {
                    if (index == daysSelected - 1) {
                        stringBuilder.append(" ${context.getString(R.string.and)} ")
                    } else if (index < daysSelected) {
                        stringBuilder.append(", ")
                    }
                }
            }
        }

        return stringBuilder.toString()
    }

    fun isEveryDaySelectedOrNotSelected() : Boolean {
        return selectedWeekDayButtons.all { it } || selectedWeekDayButtons.all { !it }
    }

    fun daysSelected() : Int {
        return selectedWeekDayButtons.count { it }
    }

    fun saveHabit(context: Context, name : String) : Boolean {
        if(name.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_name_empty), Toast.LENGTH_LONG).show()
            return false
        }

        val habit = Habit()
        habit.name = name

        val currentTime = System.currentTimeMillis()
        habit.creationDate = currentTime
        habit.modificationDate = currentTime

        val selectedIconModel = selectedIconModel
        if(selectedIconModel == null) {
            habit.iconKey = null
        } else {
            habit.iconKey = selectedIconModel.key
        }

        habit.priority = priorityValue

        viewModelScope.launch {
            insertHabit(habit)
        }

        return true
    }

    private suspend fun insertHabit(habit: Habit) {
        databaseManager.habitRepository.createHabit(habit)
    }

    fun isEditingExistingHabit() : Boolean {
        return habitId != -1L
    }
}