package com.santtuhyvarinen.habittracker.models

import android.content.Context
import com.santtuhyvarinen.habittracker.R

class WeekDaysSelectionModel {
    var selectedWeekDayButtons = Array(7) { false }

    fun isEveryDaySelectedOrNotSelected() : Boolean {
        return selectedWeekDayButtons.all { it } || selectedWeekDayButtons.all { !it }
    }

    fun getNumberOfDaysSelected() : Int {
        return selectedWeekDayButtons.count { it }
    }
}