package com.santtuhyvarinen.habittracker.utils

import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs

class ScoreUtil {
    companion object {

        fun shouldResetHabitScore(habitWithTaskLogs: HabitWithTaskLogs): Boolean {
            val previousDateForHabit = CalendarUtil.getPreviousDateForHabit(habitWithTaskLogs.habit)?: return false

            val startTime = previousDateForHabit.withTimeAtStartOfDay().millis

            //Sort TaskLogs by date by descending
            val taskLogs = habitWithTaskLogs.taskLogs.sortedByDescending { it.timestamp }
            for (taskLog in taskLogs) {

                if(taskLog.status == TaskUtil.STATUS_FAILED) return true

                //Return false, if TaskLog timestamp is more recent than previous scheduled date
                if(taskLog.timestamp > startTime) {
                    return false
                }
            }

            return true
        }
    }
}