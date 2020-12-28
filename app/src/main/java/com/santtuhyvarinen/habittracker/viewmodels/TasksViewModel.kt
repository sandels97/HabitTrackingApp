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
    private val taskManager = TaskManager()

    fun setTaskAsDone(taskModel: TaskModel) {
        val taskLog = TaskLog()

        taskLog.habit_id = taskModel.habit.id
        taskLog.timestamp = System.currentTimeMillis()
        taskLog.status = TaskManager.STATUS_SUCCESS

        viewModelScope.launch {
            databaseManager.taskLogRepository.createTaskLog(taskLog)
        }
    }

    fun generateTasks(context: Context, habits : List<Habit>) {
        viewModelScope.launch {
            taskManager.generateTasks(context, habits, databaseManager.taskLogRepository)
        }
    }
    fun setHabitsObserver(lifecycleOwner: LifecycleOwner, observer: Observer<List<Habit>>) {
        return databaseManager.habitRepository.habits.observe(lifecycleOwner, observer)
    }

    fun setTasksObserver(lifecycleOwner: LifecycleOwner, observer: Observer<ArrayList<TaskModel>>) {
        return taskManager.tasks.observe(lifecycleOwner, observer)
    }
}