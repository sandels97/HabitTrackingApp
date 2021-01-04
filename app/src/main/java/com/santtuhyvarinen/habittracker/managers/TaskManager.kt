package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.database.repositories.TaskLogRepository
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskLog
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class TaskManager(private val databaseManager: DatabaseManager) {

    companion object {
        const val STATUS_SUCCESS = "success"
        const val STATUS_SKIPPED = "skip"
        const val STATUS_FAILED = "failed"
    }

    val tasks : MutableLiveData<ArrayList<TaskModel>> by lazy {
        MutableLiveData<ArrayList<TaskModel>>()
    }

    fun generateDailyTasks(context: Context, habits : List<HabitWithTaskLogs>) {
        val taskList = ArrayList<TaskModel>()

        for(habitWithTaskLogs in habits) {
            if(CalendarUtil.isHabitScheduledForToday(context, habitWithTaskLogs.habit)) {

                //Check if already added a task log for habit today. If already has a task log for today, don't add the task
                if (!hasTaskLogForToday(habitWithTaskLogs)) {
                    taskList.add(TaskModel(habitWithTaskLogs.habit))
                }
            }
        }

        taskList.sortWith (compareByDescending<TaskModel> { it.habit.priority}.thenBy { it.habit.name} )

        tasks.value = taskList
    }

    private fun hasTaskLogForToday(habitWithTaskLogs: HabitWithTaskLogs) : Boolean {
        val currentTime = System.currentTimeMillis()
        val startTime = DateTime(currentTime).withTimeAtStartOfDay()
        val endTime = startTime.plusDays(1)

        val startTimeMillis = startTime.millis
        val endTimeMillis = endTime.millis

        for(taskLog in habitWithTaskLogs.taskLogs) {
            if(taskLog.timestamp in startTimeMillis..endTimeMillis) {
                return true
            }
        }

        return false
    }

    suspend fun insertTaskLog(taskModel: TaskModel, taskStatus : String) {
        val taskLog = TaskLog()

        taskLog.habitId = taskModel.habit.id
        taskLog.timestamp = System.currentTimeMillis()
        taskLog.status = taskStatus

        databaseManager.taskLogRepository.createTaskLog(taskLog)
    }
}