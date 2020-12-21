package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.IconModel

class HabitFormViewModel() : ViewModel() {

    private var initialized = false

    var loading = false

    var priorityLevels : Array<String> = Array (0) { "" }

    var priorityValue = 0
    var selectedWeekDayButtons = Array(7) { false }
    var selectedIconModel : IconModel? = null

    val iconManager = IconManager()

    fun initialize(context: Context) {
        if(initialized) return

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

    fun saveHabit() : Boolean {
        return true
    }
}