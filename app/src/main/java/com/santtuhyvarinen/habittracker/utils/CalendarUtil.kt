package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

class CalendarUtil {
    companion object {
        const val RRULE_EVERY_DAY = "FREQ=DAILY;"
        const val RRULE_WEEKLY = "FREQ=WEEKLY;BYDAY="

        fun getRRuleWeekDays() : Array<String> {
            return arrayOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
        }

        fun getRRuleFromWeekDaysSelectionModel(weekDaysSelectionModel: WeekDaysSelectionModel) : String {
            if(weekDaysSelectionModel.isEveryDaySelectedOrNotSelected()) {
                return RRULE_EVERY_DAY
            }

            val stringBuilder = StringBuilder()
            stringBuilder.append(RRULE_WEEKLY)

            val weekDaysRules = getRRuleWeekDays()

            val numberOfSelected = weekDaysSelectionModel.getNumberOfDaysSelected()
            var index = 0
            for(i in weekDaysSelectionModel.selectedWeekDayButtons.indices) {
                val weekDay = weekDaysSelectionModel.selectedWeekDayButtons[i]
                if(weekDay) {
                    index ++
                    stringBuilder.append(weekDaysRules[i])
                    if(index < numberOfSelected) {
                        stringBuilder.append(",")
                    }
                }
            }

            return stringBuilder.toString()
        }

        fun parseRRULEtoWeekDaysSelectionModel(rrule : String, weekDaysSelectionModel: WeekDaysSelectionModel) {

            weekDaysSelectionModel.setAllWeekDaysFalse()

            if(rrule.contains(RRULE_EVERY_DAY)) return

            if(rrule.contains(RRULE_WEEKLY)) {
                try {
                    val selectedWeekDays = rrule.removePrefix(RRULE_WEEKLY).split(",")
                    val weekDaysRules = getRRuleWeekDays()

                    for(i in weekDaysSelectionModel.selectedWeekDayButtons.indices) {
                        weekDaysSelectionModel.selectedWeekDayButtons[i] = selectedWeekDays.contains(weekDaysRules[i])
                    }
                } catch (e : Error) {
                    e.printStackTrace()
                    Log.e("", "RRule is most likely in wrong format: $rrule")
                }
            }
        }

        fun isHabitScheduledForToday(habit: Habit) : Boolean {
            val rrule = habit.taskRecurrence

            if(rrule.contains(RRULE_EVERY_DAY)) return true

            if(rrule.contains(RRULE_WEEKLY)) {
                val currentWeekDay = getCurrentWeekDay() - 1
                val selectedWeekDays = rrule.removePrefix(RRULE_WEEKLY).split(",")
                val weekDaysRules = getRRuleWeekDays()

                val weekDayText = weekDaysRules[currentWeekDay]

                return selectedWeekDays.contains(weekDayText)
            }

            return false
        }

        fun isHabitScheduledForDate(habit: Habit, date : DateTime) : Boolean {
            val rrule = habit.taskRecurrence

            if(rrule.contains(RRULE_EVERY_DAY)) return true

            if(rrule.contains(RRULE_WEEKLY)) {
                val currentWeekDay = date.dayOfWeek - 1
                val selectedWeekDays = rrule.removePrefix(RRULE_WEEKLY).split(",")
                val weekDaysRules = getRRuleWeekDays()

                val weekDayText = weekDaysRules[currentWeekDay]

                return selectedWeekDays.contains(weekDayText)
            }

            return false
        }

        //WeekDay. Monday = 0, Sunday = 6
        fun isHabitScheduledForWeekday(habit: Habit, weekDay : Int) : Boolean {
            val rrule = habit.taskRecurrence

            if(rrule.contains(RRULE_EVERY_DAY)) return true

            if(rrule.contains(RRULE_WEEKLY)) {

                val selectedWeekDays = rrule.removePrefix(RRULE_WEEKLY).split(",")
                val weekDaysRules = getRRuleWeekDays()

                val weekDayText = weekDaysRules[weekDay]

                return selectedWeekDays.contains(weekDayText)
            }

            return false
        }

        fun getPreviousDateForHabit(habit: Habit) : DateTime? {
            val rrule = habit.taskRecurrence

            //If Habit is scheduled for every day, return yesterday
            if(rrule.contains(RRULE_EVERY_DAY)) return DateTime.now().minusDays(1)

            if(rrule.contains(RRULE_WEEKLY)) {
                val selectedWeekDays = rrule.removePrefix(RRULE_WEEKLY).split(",")
                val weekDaysRules = getRRuleWeekDays()

                var dateToCheck = DateTime.now().minusDays(1).withTimeAtStartOfDay()

                //Find the previous date when habit was scheduled
                for (i in 0 until 7) {
                    val weekDay = dateToCheck.dayOfWeek - 1
                    val weekDayText = weekDaysRules[weekDay]

                    if(selectedWeekDays.contains(weekDayText)) {
                        return dateToCheck
                    }

                    dateToCheck = dateToCheck.minusDays(1)
                }
            }

            //Return null if could not find the previous date
            return null
        }

        fun getCurrentDateText(context: Context) : String {
            val dateTime = DateTime.now()
            val dateTimeFormatter = DateTimeFormat.longDate().withLocale(SettingsUtil.getLocale(context))

            return dateTimeFormatter.print(dateTime)
        }

        fun getDateText(timestamp : Long, context: Context) : String {

            val dateTime = DateTime(timestamp)
            val dateTimeFormatter = DateTimeFormat.longDate().withLocale(SettingsUtil.getLocale(context))

            return dateTimeFormatter.print(dateTime)
        }

        fun getDateTextShort(date: DateTime, context: Context) : String {
            val pattern = DateTimeFormat.patternForStyle("S-", SettingsUtil.getLocale(context)).replace("y", "")

            val dateTimeFormatter = DateTimeFormat.forPattern(pattern.substring(0, pattern.length - 1))
            return dateTimeFormatter.print(date)
        }

        fun getCurrentWeekDayText(context: Context) : String {
            val dayOfWeek = getCurrentWeekDay()-1
            val weekDaysArray = context.resources.getStringArray(R.array.WeekDays)

            return weekDaysArray[dayOfWeek]
        }

        fun areSameDate(dateTime: DateTime, dateTime2: DateTime) : Boolean {
            return dateTime.toLocalDate().isEqual(dateTime2.toLocalDate())
        }

        fun getWeekDayTextShort(context: Context, date: DateTime) : String {
            val dayOfWeek = date.dayOfWeek - 1
            val weekDaysArray = context.resources.getStringArray(R.array.WeekDaysShort)

            return weekDaysArray[dayOfWeek]
        }

        //Monday == 1
        //Sunday == 7
        //etc
        fun getCurrentWeekDay() : Int {
            val dateTime = DateTime.now()
            return dateTime.dayOfWeek
        }
    }
}