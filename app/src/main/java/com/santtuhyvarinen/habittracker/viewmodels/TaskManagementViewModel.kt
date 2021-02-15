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
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskManagementViewModel(application: Application) : AndroidViewModel(application) {
    private var initialized = false

    private val databaseManager = DatabaseManager(getApplication())
    private val taskManager = TaskManager(databaseManager)
    val iconManager = IconManager(application)

    private lateinit var habitWithTaskLogs : LiveData<HabitWithTaskLogs?>

    private var selectedDateTimestamp : MutableLiveData<Long> = MutableLiveData()

    fun initialize(id : Long) {
        if (initialized) return

        selectedDateTimestamp.value = System.currentTimeMillis()
        habitWithTaskLogs = databaseManager.habitRepository.getHabitWithTaskLogsById(id)

        initialized = true
    }

    fun getHabitWithTaskLogsLiveData() : LiveData<HabitWithTaskLogs?> {
        return habitWithTaskLogs
    }

    fun createTaskLog(status : String) : Boolean {
        val habit = habitWithTaskLogs.value?: return false
        if(!canAddTaskLog()) return false

        viewModelScope.launch {
            taskManager.insertTaskLog(TaskModel(habit), status, getSelectedDateTimestamp())
        }

        return true
    }

    fun getSelectedDateTimestampLiveData() : LiveData<Long> {
        return selectedDateTimestamp
    }

    fun getSelectedDateTimestamp() : Long {
        return selectedDateTimestamp.value?: System.currentTimeMillis()
    }

    fun setSelectedDateTimestamp(timestamp: Long) {
        selectedDateTimestamp.value = timestamp
    }

    fun canAddTaskLog() : Boolean {
        val habitWithTaskLogs = habitWithTaskLogs.value?: return false
        return !TaskUtil.hasTaskLogForDate(habitWithTaskLogs, getSelectedDateTimestamp())
    }
}