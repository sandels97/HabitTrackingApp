package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.models.TaskModel

class HabitViewModel : ViewModel() {
    private var initialized = false
    private var habitId : Long = -1

    fun initialize(context: Context, id : Long) {
        if (initialized) return

        habitId = id

        initialized = true
    }
}