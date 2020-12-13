package com.santtuhyvarinen.habittracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.TaskModel

class TasksAdapter(var context: Context, var data : List<TaskModel>) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val titleTextView : TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskModel = data[position]
        holder.titleTextView.text = taskModel.title
    }

    override fun getItemCount(): Int {
        return data.size
    }

}