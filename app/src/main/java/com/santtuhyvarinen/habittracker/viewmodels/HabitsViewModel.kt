package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.database.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit

class HabitsViewModel : ViewModel() {

    private var initialized = false

    val iconManager = IconManager()

    lateinit var databaseManager : DatabaseManager

    fun initialize(context: Context) {
        if(initialized) return

        databaseManager = DatabaseManager(context)
        iconManager.loadIcons(context)

        initialized = true
    }

    fun setHabitsObserver(lifecycleOwner: LifecycleOwner, observer: Observer<List<Habit>>) {
        return databaseManager.habitRepository.habits.observe(lifecycleOwner, observer)
    }
}