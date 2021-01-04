package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseManager = DatabaseManager(application)
    private val taskManager = TaskManager(databaseManager)
    val iconManager = IconManager(application)

    fun setTaskAsDone(taskModel: TaskModel) {
        viewModelScope.launch {
            taskManager.insertTaskLog(taskModel, TaskManager.STATUS_SUCCESS)
        }
    }

    fun generateDailyTasks(context: Context, habits : List<HabitWithTaskLogs>) {
        viewModelScope.launch {
            taskManager.generateDailyTasks(context, habits)
        }
    }

    fun getHabitsWithTaskLogs() : LiveData<List<HabitWithTaskLogs>> {
        return databaseManager.habitRepository.habitsWithTaskLogs
    }

    fun getTasks() : LiveData<ArrayList<TaskModel>> {
        return taskManager.tasks
    }
}