package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import com.santtuhyvarinen.habittracker.models.DateStatusModel
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.GraphDataModel
import org.joda.time.DateTime
import org.joda.time.Days

class TaskUtil {
    companion object {

        const val STATUS_SUCCESS = "success"
        const val STATUS_SKIPPED = "skip"
        const val STATUS_FAILED = "failed"
        const val STATUS_NONE = "none"

        fun hasTaskLogForToday(habitWithTaskLogs: HabitWithTaskLogs) : Boolean {
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

        //Returns an array for Habit that contains Habit task status from last seven days
        fun getDateStatusModelsForHabit(context: Context, habitWithTaskLogs: HabitWithTaskLogs, fromDate : DateTime, days : Int) : Array<DateStatusModel> {
            val array = Array(days) { DateStatusModel("", STATUS_NONE) }

            var date = fromDate
            for(i in array.indices) {
                val dateStatusModel = array[i]
                dateStatusModel.date = CalendarUtil.getWeekDayTextShort(context, date)

                val startTime = DateTime(date).withTimeAtStartOfDay()
                val endTime = startTime.plusDays(1)
                val startTimeMillis = startTime.millis
                val endTimeMillis = endTime.millis

                for(taskLog in habitWithTaskLogs.taskLogs) {
                    if(taskLog.timestamp in startTimeMillis..endTimeMillis) {
                        dateStatusModel.status = taskLog.status
                        break
                    }
                }

                date = date.minusDays(1)
            }

            array.reverse()

            return array
        }

        fun getAmountOfDoneTasksForDateRange(context: Context, habitsWithTaskLogs: List<HabitWithTaskLogs>, fromDate: DateTime, toDate: DateTime) : List<GraphDataModel> {
            val difference = Days.daysBetween(fromDate.toLocalDate(), toDate.toLocalDate()).days

            var date = toDate
            val list = ArrayList<GraphDataModel>()
            for(i in 0 until difference) {
                val weekDayText = CalendarUtil.getWeekDayTextShort(context, date)
                val dateText = CalendarUtil.getDateTextShort(date)
                val lineGraphDataModel = GraphDataModel(weekDayText, dateText, getAmountOfDoneTasksForDate(habitsWithTaskLogs, date))
                list.add(lineGraphDataModel)

                date = date.minusDays(1)
            }

            return list
        }

        private fun getAmountOfDoneTasksForDate(habitsWithTaskLogs: List<HabitWithTaskLogs>, date: DateTime) : Int {
            val startTime = DateTime(date).withTimeAtStartOfDay()
            val endTime = startTime.plusDays(1)
            val startTimeMillis = startTime.millis
            val endTimeMillis = endTime.millis

            var sum = 0

            for(habitWithTaskLogs in habitsWithTaskLogs) {
                if(habitWithTaskLogs.taskLogs.isEmpty()) continue

                sum += habitWithTaskLogs.taskLogs.filter { it.status == STATUS_SUCCESS }.filter { it.timestamp in startTimeMillis..endTimeMillis }.count()
            }

            return sum
        }
    }
}