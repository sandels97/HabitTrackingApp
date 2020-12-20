package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.R
import kotlinx.android.synthetic.main.fragment_habit_form.*

class HabitFormViewModel : ViewModel() {

    companion object {
        const val MAX_PRIORITY_VALUE = 100
    }

    var priorityLevels : Array<String> = Array (0) { "" }

    var priorityValue = 0

    var selectedWeekDayButtons = Array(7) { false }

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

        return false
    }
}