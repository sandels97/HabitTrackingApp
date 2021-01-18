package com.santtuhyvarinen.habittracker.utils

import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.TaskLog

class StatisticsUtil {
    companion object {
        fun getTotalSuccesses(taskLogs : List<TaskLog>) : Int {
            return taskLogs.filter { it.status == TaskManager.STATUS_SUCCESS }.count()
        }

        fun getHighestScore(taskLogs : List<TaskLog>) : Int {
            if(taskLogs.isEmpty()) return 0
            return taskLogs.maxOf { it.score }
        }
    }
}