package com.santtuhyvarinen.habittracker.utils

import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskLog

class StatisticsUtil {
    companion object {
        fun getTotalSuccesses(taskLogs : List<TaskLog>) : Int {
            return taskLogs.filter { it.status == TaskUtil.STATUS_SUCCESS }.count()
        }

        fun getTotalSuccessesForHabits(habitsWithTaskLogs : List<HabitWithTaskLogs>) : Int {
            var sum = 0
            for(habitWithTaskLogs in habitsWithTaskLogs) {
                sum += getTotalSuccesses(habitWithTaskLogs.taskLogs)
            }

            return sum
        }

        fun getHighestScore(taskLogs : List<TaskLog>) : Int {
            if(taskLogs.isEmpty()) return 0
            return taskLogs.maxOf { it.score }
        }
    }
}