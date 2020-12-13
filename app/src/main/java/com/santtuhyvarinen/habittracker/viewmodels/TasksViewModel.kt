package com.santtuhyvarinen.habittracker.viewmodels

import androidx.lifecycle.ViewModel
import com.santtuhyvarinen.habittracker.models.TaskModel

class TasksViewModel : ViewModel() {
    val tasks : ArrayList<TaskModel> = ArrayList()

    init {
        //Dummy data for testing
        for(i in 0 until 10) {
            tasks.add(TaskModel("Task example"))
        }
    }
}