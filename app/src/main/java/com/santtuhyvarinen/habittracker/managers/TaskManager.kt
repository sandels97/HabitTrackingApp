package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.database.repositories.TaskLogRepository
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.TaskLog
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import kotlinx.coroutines.launch

class TaskManager(private val databaseManager: DatabaseManager) {

    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_SKIPPED = "skip"
        const val STATUS_FAILED = "failed"
    }

    val tasks : MutableLiveData<ArrayList<TaskModel>> by lazy {
        MutableLiveData<ArrayList<TaskModel>>()
    }

    suspend fun generateTasks(context: Context, habits : List<Habit>) {
        val taskList = ArrayList<TaskModel>()

        val currentTime = System.currentTimeMillis()
        for(habit in habits) {
            if(CalendarUtil.isHabitScheduledForToday(context, habit)) {
                //Check if already added a task log for habit today. If not, the list should be empty
                val taskLogs = databaseManager.taskLogRepository.getTaskLogsByHabitAndTime(habit, currentTime)

                if (taskLogs.isEmpty()) {
                    taskList.add(TaskModel(habit))
                }
            }
        }

        taskList.sortWith (compareByDescending<TaskModel> { it.habit.priority}.thenBy { it.habit.name} )

        tasks.value = taskList
    }

    suspend fun insertTaskLog(taskModel: TaskModel, taskStatus : String) {
        val taskLog = TaskLog()

        taskLog.habit_id = taskModel.habit.id
        taskLog.timestamp = System.currentTimeMillis()
        taskLog.status = taskStatus

        databaseManager.taskLogRepository.createTaskLog(taskLog)
    }
}