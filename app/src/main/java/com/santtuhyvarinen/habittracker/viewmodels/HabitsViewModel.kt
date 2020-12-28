package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit

class HabitsViewModel(application: Application) : AndroidViewModel(application) {

    private val iconManager = IconManager()
    private val databaseManager : DatabaseManager = DatabaseManager(getApplication())

    init {
        iconManager.loadIcons(getApplication())
    }

    fun setHabitsObserver(lifecycleOwner: LifecycleOwner, observer: Observer<List<Habit>>) {
        return databaseManager.habitRepository.habits.observe(lifecycleOwner, observer)
    }
}