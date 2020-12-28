package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import android.util.Log
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil

class HabitInfoManager {

    companion object {
        const val MAX_PRIORITY_LEVEL = 4
    }

    fun getCurrentPriorityLevelText(context: Context, priorityValue : Int) : String {
        val priorityLevels : Array<String> = context.resources.getStringArray(R.array.PriorityLevels)
        val index = priorityValue.coerceAtLeast(0).coerceAtMost(MAX_PRIORITY_LEVEL)
        return priorityLevels[index]
    }

    fun getRecurrenceHeader(context: Context, weekDaysSelectionModel: WeekDaysSelectionModel) : String {
        if(weekDaysSelectionModel.isEveryDaySelectedOrNotSelected()) {
            return context.getString(R.string.habit_repeat_every_day)
        } else {
            return context.getString(R.string.habit_repeat_days, getWeekDaysSelectedText(context, weekDaysSelectionModel))
        }
    }

    private fun getWeekDaysSelectedText(context: Context, weekDaysSelectionModel: WeekDaysSelectionModel) : String {
        val daysSelected = weekDaysSelectionModel.getNumberOfDaysSelected()

        val weekDays = if(daysSelected < 3) context.resources.getStringArray(R.array.WeekDays) else context.resources.getStringArray(R.array.WeekDaysShort)

        var index = 0

        val stringBuilder = StringBuilder()
        for(i in weekDays.indices) {
            if(weekDaysSelectionModel.selectedWeekDayButtons[i]) {
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
}