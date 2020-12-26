package com.santtuhyvarinen.habittracker.viewmodels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.TaskModel

class TasksViewModel : ViewModel() {

    private lateinit var databaseManager : DatabaseManager
    val taskManager = TaskManager()

    private var initialized = false

    fun initialize(context: Context) {
        if (initialized) return

        databaseManager = DatabaseManager(context)

        initialized = true
    }

    fun setTaskAsDone(taskModel: TaskModel) {
    }

    fun setHabitsObserver(lifecycleOwner: LifecycleOwner, observer: Observer<List<Habit>>) {
        return databaseManager.habitRepository.habits.observe(lifecycleOwner, observer)
    }

    fun setTasksObserver(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<TaskModel>>) {
        return taskManager.tasks.observe(lifecycleOwner, observer)
    }
}