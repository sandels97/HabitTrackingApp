package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import android.util.Log
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import org.joda.time.DateTime

class ScoreUtil {
    companion object {

        fun shouldResetHabitScore(context: Context, habitWithTaskLogs: HabitWithTaskLogs): Boolean {
            val previousDateForHabit = CalendarUtil.getPreviousDateForHabit(context, habitWithTaskLogs.habit)?: return false

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