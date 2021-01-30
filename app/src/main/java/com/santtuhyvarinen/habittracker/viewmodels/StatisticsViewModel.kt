package com.santtuhyvarinen.habittracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.santtuhyvarinen.habittracker.managers.DatabaseManager
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.GraphDataModel
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {

    var habitsWithTaskLogs : List<HabitWithTaskLogs> = ArrayList()

    var lineGraphColumns = 7

    private var selectedDate = DateTime.now()
    private val databaseManager = DatabaseManager(application)

    private val loading : MutableLiveData<Boolean> = MutableLiveData()

    private val completedTasksGraphData : MutableLiveData<List<GraphDataModel>> = MutableLiveData()
    private val scheduledTasksGraphData : MutableLiveData<List<GraphDataModel>> = MutableLiveData()

    fun getHabitsWithTaskLogs() : LiveData<List<HabitWithTaskLogs>> {
        return databaseManager.habitRepository.habitsWithTaskLogs
    }

    fun getCompletedTasksGraphData() : LiveData<List<GraphDataModel>> {
        return completedTasksGraphData
    }

    fun getScheduledTasksGraphData() : LiveData<List<GraphDataModel>> {
        return scheduledTasksGraphData
    }

    fun getLoadingLiveData() : LiveData<Boolean> {
        return loading
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

    fun generateData() {
        viewModelScope.launch {
            generateLineGraphData()
            generateScheduledTasksGraphData()

            loading.value = false
        }
    }

    private fun generateLineGraphData() {
        val fromDate = selectedDate.minusDays(lineGraphColumns)
        completedTasksGraphData.value = TaskUtil.getAmountOfDoneTasksForDateRange(getApplication(), habitsWithTaskLogs, fromDate, selectedDate).reversed()
    }

    private fun generateScheduledTasksGraphData() {
        scheduledTasksGraphData.value = TaskUtil.getAmountOfScheduledTasksPerWeekDay(getApplication(), habitsWithTaskLogs)
    }
}