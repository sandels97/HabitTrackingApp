package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseManager = DatabaseManager(application)
    private val taskManager = TaskManager(databaseManager)
    val iconManager = IconManager(application)

    fun createTaskLog(taskModel: TaskModel, status : String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskManager.insertTaskLog(taskModel, status)
        }
    }

    fun generateDailyTasks(habits : List<HabitWithTaskLogs>) {
        taskManager.generateDailyTasks(habits)
    }

    fun getHabitsWithTaskLogs() : LiveData<List<HabitWithTaskLogs>> {
        return databaseManager.habitRepository.habitsWithTaskLogs
    }

    fun getTasks() : LiveData<ArrayList<TaskModel>> {
        return taskManager.tasks
    }
}