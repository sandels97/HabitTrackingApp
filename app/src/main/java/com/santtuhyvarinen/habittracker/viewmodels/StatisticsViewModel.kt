package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.ChartDataModel
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    var habitsWithTaskLogs : List<HabitWithTaskLogs> = ArrayList()

    var lineChartColumns = 7

    private var selectedDate = DateTime.now()
    private val databaseManager = DatabaseManager(application)

    private val loading : MutableLiveData<Boolean> = MutableLiveData()

    private val completedTasksChartData : MutableLiveData<List<ChartDataModel>> = MutableLiveData()
    private val scheduledTasksChartData : MutableLiveData<List<ChartDataModel>> = MutableLiveData()

    fun getHabitsWithTaskLogs() : LiveData<List<HabitWithTaskLogs>> {
        return databaseManager.habitRepository.habitsWithTaskLogs
    }

    fun getCompletedTasksChartData() : LiveData<List<ChartDataModel>> {
        return completedTasksChartData
    }

    fun getScheduledTasksChartData() : LiveData<List<ChartDataModel>> {
        return scheduledTasksChartData
    }

    fun getLoadingLiveData() : LiveData<Boolean> {
        return loading
    }

    fun setSelectedDate(dateTime: DateTime) {
        selectedDate = dateTime
        generateLineChartData()
    }

    fun setColumns(columns : Int) {
        lineChartColumns = columns
        generateLineChartData()
    }

    fun getSelectedDate() : DateTime {
        return selectedDate
    }

    fun generateData() {
        viewModelScope.launch {
            generateLineChartData()
            generateScheduledTasksChartData()

            loading.value = false
        }
    }

    private fun generateLineChartData() {
        val fromDate = selectedDate.minusDays(lineChartColumns)
        completedTasksChartData.value = TaskUtil.getAmountOfDoneTasksForDateRange(getApplication(), habitsWithTaskLogs, fromDate, selectedDate).reversed()
    }

    private fun generateScheduledTasksChartData() {
        scheduledTasksChartData.value = TaskUtil.getAmountOfScheduledTasksPerWeekDay(getApplication(), habitsWithTaskLogs)
    }
}