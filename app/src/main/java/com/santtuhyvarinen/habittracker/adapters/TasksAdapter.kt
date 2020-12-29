package com.santtuhyvarinen.habittracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.TaskModel

class TasksAdapter(private var context: Context, private val iconManager: IconManager) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    var data : ArrayList<TaskModel> = ArrayList()

    var taskListener : TaskListener? = null
    interface TaskListener {
        fun taskMarkedDone(taskModel: TaskModel)
    }

    class ViewHolder(var layout : View) : RecyclerView.ViewHolder(layout) {
        val titleTextView : TextView = layout.findViewById(R.id.title)
        val iconView : ImageView = layout.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskModel = data[position]
        holder.titleTextView.text = taskModel.habit.name

        val iconKey = taskModel.habit.iconKey

        if(iconKey != null)
            holder.iconView.setImageDrawable(iconManager.getIconByKey(iconKey))

        holder.layout.setOnLongClickListener {
            taskListener?.taskMarkedDone(taskModel)

            data.remove(taskModel)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)

            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}