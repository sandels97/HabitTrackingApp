package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.database.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.IconModel
import kotlinx.coroutines.launch

class HabitFormViewModel : ViewModel() {

    private var initialized = false

    var loading = false

    var habitId : Long = -1L
    val habitData : MutableLiveData<Habit> by lazy {
        MutableLiveData<Habit>()
    }

    val habitDataSaved : MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    lateinit var databaseManager : DatabaseManager

    var habitName = ""
    var priorityLevels : Array<String> = Array (0) { "" }
    var priorityValue = 0
    var selectedWeekDayButtons = Array(7) { false }
    var selectedIconModel : IconModel? = null

    val iconManager = IconManager()

    fun initialize(context: Context, id : Long) {
        if(initialized) return

        databaseManager = DatabaseManager(context)

        habitId = id

        if(isEditingExistingHabit()) {
            loading = true
            viewModelScope.launch {
                habitData.value = getHabit()
            }
        }

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

    fun saveHabit(context: Context) {
        if(habitName.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.error_name_empty), Toast.LENGTH_LONG).show()
            return
        }

        val habit : Habit
        if(isEditingExistingHabit()) {
            habit = habitData.value as Habit
        } else {
            habit = Habit()
        }

        habit.name = habitName

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
            if(isEditingExistingHabit()) {
                updateHabit(habit)
                habitDataSaved.value = habitId
            } else {
                val id = insertHabit(habit)
                habitDataSaved.value = id
            }
        }
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