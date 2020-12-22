package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit

class HabitsViewModel : ViewModel() {

    private var initialized = false

    val habits : ArrayList<Habit> = ArrayList()
    val iconManager = IconManager()

    fun initialize(context: Context) {
        if(initialized) return

        iconManager.loadIcons(context)
        createDummyData()

        initialized = true
    }

    private fun createDummyData() {
        val habit = Habit(1)
        habit.name = "Go to the gym"
        habit.iconDrawable = iconManager.getIconByKey("star")
        habits.add(habit)

        val habit2 = Habit(2)
        habit2.name = "Drink water"
        habit2.iconDrawable = iconManager.getIconByKey("favorite")
        habits.add(habit2)

        val habit3 = Habit(3)
        habit3.name = "Go for a walk"
        habit3.iconDrawable = iconManager.getIconByKey("thumbs_up")
        habits.add(habit3)
    }
}