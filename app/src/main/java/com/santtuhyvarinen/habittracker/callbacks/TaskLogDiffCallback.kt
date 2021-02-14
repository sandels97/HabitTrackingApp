package com.santtuhyvarinen.habittracker.callbacks

import androidx.recyclerview.widget.DiffUtil
import com.santtuhyvarinen.habittracker.models.TaskLog

class TaskLogDiffCallback(private val oldData : List<TaskLog>, private val newData : List<TaskLog>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTaskLog = oldData[oldItemPosition]
        val newTaskLog = newData[newItemPosition]

        return oldTaskLog.id == newTaskLog.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTaskLog = oldData[oldItemPosition]
        val newTaskLog = newData[newItemPosition]

        return oldTaskLog.status == newTaskLog.status && oldTaskLog.timestamp == newTaskLog.timestamp
    }
}