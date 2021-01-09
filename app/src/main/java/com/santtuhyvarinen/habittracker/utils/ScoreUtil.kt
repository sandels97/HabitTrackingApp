package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs

class ScoreUtil {
    companion object {

        fun shouldResetHabitScore(habitWithTaskLogs: HabitWithTaskLogs): Boolean {
            val previousDateForHabit = CalendarUtil.getPreviousDateForHabit(habitWithTaskLogs.habit)?: return false

            val startTime = previousDateForHabit.withTimeAtStartOfDay().millis

            val taskLogs = habitWithTaskLogs.taskLogs.sortedByDescending { it.timestamp }
            for (taskLog in taskLogs) {
                //Return false, if TaskLog timestamp is more recent than previous scheduled date
                if(taskLog.timestamp > startTime) {
                    return false
                }
            }

            return true
        }
    }
}