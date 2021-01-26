package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.LineGraphDataModel
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import org.joda.time.DateTime

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    var habitsWithTaskLogs : List<HabitWithTaskLogs> = ArrayList()

    var lineGraphColumns = 7

    private var selectedDate = DateTime.now()
    private val databaseManager = DatabaseManager(application)

    private val lineGraphData : MutableLiveData<List<LineGraphDataModel>> = MutableLiveData()

    fun getHabitsWithTaskLogs() : LiveData<List<HabitWithTaskLogs>> {
        return databaseManager.habitRepository.habitsWithTaskLogs
    }

    fun getLineGraphData() : LiveData<List<LineGraphDataModel>> {
        return lineGraphData
    }

    fun setSelectedDate(dateTime: DateTime) {
        selectedDate = dateTime
        generateLineGraphData()
    }

    fun setColumns(columns : Int) {
        lineGraphColumns = columns
        generateLineGraphData()
    }

    fun getSelectedDate() : DateTime {
        return selectedDate
    }

    fun generateLineGraphData() {
        val fromDate = selectedDate.minusDays(lineGraphColumns)
        lineGraphData.value = TaskUtil.getAmountOfDoneTasksForDateRange(getApplication(), habitsWithTaskLogs, fromDate, selectedDate).reversed()
    }
}