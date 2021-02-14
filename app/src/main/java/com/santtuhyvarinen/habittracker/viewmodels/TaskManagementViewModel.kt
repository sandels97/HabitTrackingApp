package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.HabitInfoUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskManagementViewModel(application: Application) : AndroidViewModel(application) {
    private var initialized = false

    private val databaseManager = DatabaseManager(getApplication())
    private val taskManager = TaskManager(databaseManager)
    val iconManager = IconManager(application)

    private val habitWithTaskLogs : MutableLiveData<HabitWithTaskLogs> = MutableLiveData<HabitWithTaskLogs>()

    fun initialize(id : Long) {
        if (initialized) return

        viewModelScope.launch {
            habitWithTaskLogs.value = fetchHabit(id)
        }

        initialized = true
    }

    fun getHabitWithTaskLogsLiveData() : LiveData<HabitWithTaskLogs> {
        return habitWithTaskLogs
    }

    fun getHabitWithTaskLogs() : HabitWithTaskLogs? {
        return habitWithTaskLogs.value
    }


    private suspend fun fetchHabit(habitId : Long) : HabitWithTaskLogs? {
        return databaseManager.habitRepository.getHabitWithTaskLogsById(habitId)
    }

    fun createTaskLog(taskModel: TaskModel, status : String, timestamp : Long) {
        viewModelScope.launch {
            taskManager.insertTaskLog(taskModel, status, timestamp)

            //Reload the habit to update the statistics
            habitWithTaskLogs.value = fetchHabit(taskModel.habitWithTaskLogs.habit.id)
        }
    }
}