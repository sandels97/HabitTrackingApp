package com.santtuhyvarinen.habittracker.managers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil

class TaskManager {

    val tasks : MutableLiveData<ArrayList<TaskModel>> by lazy {
        MutableLiveData<ArrayList<TaskModel>>()
    }

    fun generateTasks(context: Context, habits : List<Habit>) {
        val taskList = ArrayList<TaskModel>()

        for(habit in habits) {
            if(CalendarUtil.isRRULEToday(context, habit.taskRecurrence)) {
                taskList.add(TaskModel(habit))
            }
        }

        taskList.sortWith (compareByDescending<TaskModel> { it.habit.priority}.thenBy { it.habit.name} )

        tasks.value = taskList
    }
}