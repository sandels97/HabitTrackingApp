package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import java.lang.StringBuilder

class CalendarUtil {
    companion object {
        const val RRULE_EVERY_DAY = "FREQ=DAILY;"
        const val RRULE_WEEKLY = "FREQ=WEEKLY;BYDAY="

        fun getRRuleFromWeekDaysSelectionModel(context: Context, weekDaysSelectionModel: WeekDaysSelectionModel) : String {
            if(weekDaysSelectionModel.isEveryDaySelectedOrNotSelected()) {
                return RRULE_EVERY_DAY
            }

            val stringBuilder = StringBuilder()
            stringBuilder.append(RRULE_WEEKLY)

            val weekDaysRules = context.resources.getStringArray(R.array.WeekDaysRRULE)

            val numberOfSelected = weekDaysSelectionModel.getNumberOfDaysSelected()
            var index = 0
            for(i in weekDaysSelectionModel.selectedWeekDayButtons.indices) {
                val weekDay = weekDaysSelectionModel.selectedWeekDayButtons[i]
                if(weekDay) {
                    index ++
                    stringBuilder.append(weekDaysRules[i])
                    if(index < numberOfSelected) {
                        stringBuilder.append(",")
                    }
                }
            }

            return stringBuilder.toString()
        }
    }
}