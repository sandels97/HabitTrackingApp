package com.santtuhyvarinen.habittracker.utils

import android.util.Log
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskLog
import org.joda.time.DateTime
import org.joda.time.Days

class StatisticsUtil {
    companion object {
        fun getTotalSuccesses(habitWithTaskLogs: HabitWithTaskLogs) : Int {
            return habitWithTaskLogs.taskLogs.filter { it.status == TaskUtil.STATUS_SUCCESS }.count()
        }

        fun getTotalSuccessesForHabits(habitsWithTaskLogs : List<HabitWithTaskLogs>) : Int {
            var sum = 0
            for(habitWithTaskLogs in habitsWithTaskLogs) {
                sum += getTotalSuccesses(habitWithTaskLogs)
            }

            return sum
        }

        fun getHighestScore(taskLogs : List<TaskLog>) : Int {
            if(taskLogs.isEmpty()) return 0
            return taskLogs.maxOf { it.score }
        }

        fun getAverageTasksCompletedByDay(habitsWithTaskLogs : List<HabitWithTaskLogs>) : Double {

            val taskLogs = ArrayList<TaskLog>()
            for (habitWithTaskLog in habitsWithTaskLogs) {
                taskLogs.addAll(habitWithTaskLog.taskLogs)
            }

            val doneTaskLogs = taskLogs.filter { it.status == TaskUtil.STATUS_SUCCESS }.sortedBy { it.timestamp }

            if(doneTaskLogs.isEmpty()) return 0.0

            val sum = doneTaskLogs.size

            val firstLogTimeStamp = doneTaskLogs.minOf { it.timestamp }
            val firstLogDate = DateTime(firstLogTimeStamp).toLocalDate()

            val lastLogTimeStamp = doneTaskLogs.maxOf { it.timestamp }
            val lastLogDate = DateTime(lastLogTimeStamp).toLocalDate()
            val currentDate = DateTime.now().toLocalDate()

            val compareDate = if(lastLogDate.isBefore(currentDate)) currentDate else lastLogDate

            val dayCount = Days.daysBetween(firstLogDate, compareDate).days.toDouble() + 1

            if(dayCount <= 0.0) return 0.0

            return sum / dayCount
        }
    }
}