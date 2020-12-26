package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel

class HabitInfoManager(context: Context) {

    private val andString = context.getString(R.string.and)

    private val weekDays : Array<String> = context.resources.getStringArray(R.array.WeekDays)
    private val weekDaysShort : Array<String> = context.resources.getStringArray(R.array.WeekDaysShort)
    private val priorityLevels : Array<String> = context.resources.getStringArray(R.array.PriorityLevels)

    fun getMaxPriorityLevel() : Int {
        return priorityLevels.size - 1
    }

    fun getCurrentPriorityLevelText(priorityValue : Int) : String {
        if(priorityLevels.isEmpty()) return ""

        val index = priorityValue.coerceAtLeast(0).coerceAtMost(priorityLevels.size-1)
        return priorityLevels[index]
    }

    fun getRecurrenceHeader(context: Context, weekDaysSelectionModel: WeekDaysSelectionModel) : String {
        if(weekDaysSelectionModel.isEveryDaySelectedOrNotSelected()) {
            return context.getString(R.string.habit_repeat_every_day)
        } else {
            return context.getString(R.string.habit_repeat_days, getWeekDaysSelectedText(weekDaysSelectionModel))
        }
    }

    private fun getWeekDaysSelectedText(weekDaysSelectionModel: WeekDaysSelectionModel) : String {
        val daysSelected = weekDaysSelectionModel.getNumberOfDaysSelected()

        val weekDays = if(daysSelected < 3) weekDays else weekDaysShort

        var index = 0

        val stringBuilder = StringBuilder()
        for(i in weekDays.indices) {
            if(weekDaysSelectionModel.selectedWeekDayButtons[i]) {
                val weekDay = weekDays[i]
                stringBuilder.append(weekDay)
                index ++

                if(daysSelected > 1) {
                    if (index == daysSelected - 1) {
                        stringBuilder.append(" $andString ")
                    } else if (index < daysSelected) {
                        stringBuilder.append(", ")
                    }
                }
            }
        }

        return stringBuilder.toString()
    }
}