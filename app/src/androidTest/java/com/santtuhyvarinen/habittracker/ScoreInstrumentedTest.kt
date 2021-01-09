package com.santtuhyvarinen.habittracker

import android.content.Context
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskLog
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.ScoreUtil
import org.junit.Test

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoreInstrumentedTest {

    @Test
    fun resetHabitScore() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val habit = Habit()

        val taskLogs = ArrayList<TaskLog>()

        DateTimeUtils.setCurrentMillisFixed(1610193600000L) //Saturday
        taskLogs.add(createTaskLogTest(1609939668000L)) //Wednesday
        taskLogs.add(createTaskLogTest(1609766868000L)) //Monday

        habit.taskRecurrence = CalendarUtil.RRULE_EVERY_DAY

        val habitWithTaskLogs = HabitWithTaskLogs(habit, taskLogs)
        assertTrue("${DateTime.now().millis}", ScoreUtil.shouldResetHabitScore(context, habitWithTaskLogs))

        habit.taskRecurrence = CalendarUtil.RRULE_WEEKLY + "WE"
        assertFalse(ScoreUtil.shouldResetHabitScore(context, habitWithTaskLogs))

        habit.taskRecurrence = CalendarUtil.RRULE_WEEKLY + "TU"
        assertFalse(ScoreUtil.shouldResetHabitScore(context, habitWithTaskLogs))

        habit.taskRecurrence = CalendarUtil.RRULE_WEEKLY + "TH"
        assertTrue(ScoreUtil.shouldResetHabitScore(context, habitWithTaskLogs))
    }

    private fun createTaskLogTest(timestamp : Long) : TaskLog {
        val taskLog = TaskLog()
        taskLog.timestamp = timestamp
        return taskLog
    }
}