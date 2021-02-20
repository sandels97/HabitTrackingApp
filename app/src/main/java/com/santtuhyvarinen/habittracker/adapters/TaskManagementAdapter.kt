package com.santtuhyvarinen.habittracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.callbacks.TaskLogDiffCallback
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.TaskLog
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.HabitInfoUtil
import com.santtuhyvarinen.habittracker.utils.TaskUtil

class TaskManagementAdapter(private val context: Context) : RecyclerView.Adapter<TaskManagementAdapter.ViewHolder>() {

    var data : List<TaskLog> = ArrayList()

    var taskLogClickedListener : OnTaskLogClickedListener? = null
    interface OnTaskLogClickedListener {
        fun taskLogClicked(taskLog: TaskLog)
    }

    class ViewHolder(var layout : View) : RecyclerView.ViewHolder(layout) {
        val dateTextView : TextView = layout.findViewById(R.id.date)
        val iconImageView : ImageView = layout.findViewById(R.id.icon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task_log, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskLog = data[position]

        holder.dateTextView.text = CalendarUtil.getDateText(taskLog.timestamp, context)

        val iconId = when(taskLog.status) {
            TaskUtil.STATUS_SUCCESS -> R.drawable.ic_task_success
            TaskUtil.STATUS_FAILED -> R.drawable.ic_task_fail
            TaskUtil.STATUS_SKIPPED -> R.drawable.ic_task_skip
            else -> null
        }

        if(iconId != null) {
            holder.iconImageView.setImageDrawable(ContextCompat.getDrawable(context, iconId))
        } else {
            holder.iconImageView.setImageDrawable(null)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(taskLogs: List<TaskLog>) {
        val newData = taskLogs.sortedByDescending { it.timestamp }
        val result = DiffUtil.calculateDiff(TaskLogDiffCallback(data, newData))

        data = newData

        result.dispatchUpdatesTo(this)
    }
}