package com.santtuhyvarinen.habittracker.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.santtuhyvarinen.habittracker.models.TaskModel

class TaskModelDiffCallback(private val oldData : List<TaskModel>, private val newData : List<TaskModel>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTaskModel = oldData[oldItemPosition]
        val newTaskModel = newData[newItemPosition]

        return oldTaskModel.habitWithTaskLogs.habit.id == newTaskModel.habitWithTaskLogs.habit.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTaskModel = oldData[oldItemPosition]
        val newTaskModel = newData[newItemPosition]


        val newTaskLogs = newTaskModel.habitWithTaskLogs.taskLogs
        val oldTaskLogs = oldTaskModel.habitWithTaskLogs.taskLogs

        val areTaskLogAmountSame = newTaskLogs.size == oldTaskLogs.size

        return oldTaskModel.habitWithTaskLogs.habit.hasSameContent(newTaskModel.habitWithTaskLogs.habit) && areTaskLogAmountSame
    }
}