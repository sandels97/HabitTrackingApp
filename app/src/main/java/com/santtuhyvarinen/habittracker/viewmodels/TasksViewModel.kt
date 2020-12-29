package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.TaskLog
import com.santtuhyvarinen.habittracker.models.TaskModel
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseManager = DatabaseManager(application)
    private val taskManager = TaskManager(databaseManager)

    fun setTaskAsDone(taskModel: TaskModel) {
        viewModelScope.launch {
            taskManager.insertTaskLog(taskModel, TaskManager.STATUS_SUCCESS)
        }
    }

    fun generateTasks(context: Context, habits : List<Habit>) {
        viewModelScope.launch {
            taskManager.generateTasks(context, habits)
        }
    }
    
    fun setHabitsObserver(lifecycleOwner: LifecycleOwner, observer: Observer<List<Habit>>) {
        return databaseManager.habitRepository.habits.observe(lifecycleOwner, observer)
    }

    fun setTasksObserver(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<TaskModel>>) {
        return taskManager.tasks.observe(lifecycleOwner, observer)
    }
}