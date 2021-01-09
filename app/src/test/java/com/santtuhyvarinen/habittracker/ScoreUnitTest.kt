package com.santtuhyvarinen.habittracker

import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskLog
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.ScoreUtil
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils
import org.junit.Assert
import org.junit.Test

class ScoreUnitTest {
    @Test
    fun resetHabitScore() {

        val habit = Habit()

        val taskLogs = ArrayList<TaskLog>()

        DateTimeUtils.setCurrentMillisFixed(1610193600000L) //Saturday
        taskLogs.add(createTaskLogTest(1609939668000L)) //Wednesday
        taskLogs.add(createTaskLogTest(1609766868000L)) //Monday

        habit.taskRecurrence = CalendarUtil.RRULE_EVERY_DAY

        val habitWithTaskLogs = HabitWithTaskLogs(habit, taskLogs)
        Assert.assertTrue("${DateTime.now().millis}", ScoreUtil.shouldResetHabitScore(habitWithTaskLogs))

        habit.taskRecurrence = CalendarUtil.RRULE_WEEKLY + "WE"
        Assert.assertFalse(ScoreUtil.shouldResetHabitScore(habitWithTaskLogs))

        habit.taskRecurrence = CalendarUtil.RRULE_WEEKLY + "TU"
        Assert.assertFalse(ScoreUtil.shouldResetHabitScore(habitWithTaskLogs))

        habit.taskRecurrence = CalendarUtil.RRULE_WEEKLY + "TH"
        Assert.assertTrue(ScoreUtil.shouldResetHabitScore(habitWithTaskLogs))
    }

    private fun createTaskLogTest(timestamp : Long) : TaskLog {
        val taskLog = TaskLog()
        taskLog.timestamp = timestamp
        return taskLog
    }
}