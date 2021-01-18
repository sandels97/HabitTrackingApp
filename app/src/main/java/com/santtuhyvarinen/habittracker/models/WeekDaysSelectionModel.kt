package com.santtuhyvarinen.habittracker.models

class WeekDaysSelectionModel {
    var selectedWeekDayButtons = Array(7) { false }

    fun isEveryDaySelectedOrNotSelected() : Boolean {
        return selectedWeekDayButtons.all { it } || selectedWeekDayButtons.all { !it }
    }

    fun getNumberOfDaysSelected() : Int {
        return selectedWeekDayButtons.count { it }
    }

    fun setAllWeekDaysFalse() {
        selectedWeekDayButtons.fill(false)
    }
}