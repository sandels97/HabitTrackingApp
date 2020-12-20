package com.santtuhyvarinen.habittracker.viewmodels

import androidx.lifecycle.ViewModel

class HabitFormViewModel : ViewModel() {

    var selectedWeekDayButtons = Array(7) { false }

    fun saveHabit() : Boolean {

        return false
    }

    fun isDaySelected(index : Int) : Boolean {
        if(index < 0 || index >= selectedWeekDayButtons.size) return false

        return selectedWeekDayButtons[index]
    }

    fun isEveryDaySelectedOrNotSelected() : Boolean {
        return selectedWeekDayButtons.all { it } || selectedWeekDayButtons.all { !it }
    }

    fun daysSelected() : Int {
        return selectedWeekDayButtons.count { it }
    }
}