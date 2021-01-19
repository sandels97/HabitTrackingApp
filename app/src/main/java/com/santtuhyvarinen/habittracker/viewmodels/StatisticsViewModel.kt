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
    private var lineGraphLoaded = false
    private val databaseManager = DatabaseManager(application)

    private val lineGraphData : MutableLiveData<List<LineGraphDataModel>> = MutableLiveData()

    fun getHabitsWithTaskLogs() : LiveData<List<HabitWithTaskLogs>> {
        return databaseManager.habitRepository.habitsWithTaskLogs
    }

    fun getLineGraphData() : LiveData<List<LineGraphDataModel>> {
        return lineGraphData
    }

    fun generateLineGraphData(habitsWithTaskLogs : List<HabitWithTaskLogs>, columns : Int) {
        if(lineGraphLoaded) return

        val toDate = DateTime.now()
        val fromDate = toDate.minusDays(columns)
        lineGraphData.value = TaskUtil.getAmountOfDoneTasksForDateRange(getApplication(), habitsWithTaskLogs, fromDate, toDate).reversed()
        lineGraphLoaded = true
    }
}