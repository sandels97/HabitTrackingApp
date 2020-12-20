package com.santtuhyvarinen.habittracker.viewmodels

import androidx.lifecycle.ViewModel

class HabitFormViewModel : ViewModel() {

    var selectedWeekDayButtons = Array(7) { false }

    fun saveHabit() : Boolean {

        return false
    }
}