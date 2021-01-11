package com.santtuhyvarinen.habittracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.viewmodels.TasksViewModel

class TasksAdapter(private var context: Context, private val iconManager: IconManager) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    var data : ArrayList<TaskModel> = ArrayList()

    var taskListener : TaskListener? = null
    interface TaskListener {
        fun createTaskLog(taskModel: TaskModel, status : String)
    }

    class ViewHolder(var layout : View) : RecyclerView.ViewHolder(layout) {
        val titleTextView : TextView = layout.findViewById(R.id.title)
        val scoreTextView : TextView = layout.findViewById(R.id.scoreTextView)
        val iconView : ImageView = layout.findViewById(R.id.icon)

        val successButton : ImageButton = layout.findViewById(R.id.taskSuccessButton)
        val skipButton : ImageButton = layout.findViewById(R.id.taskSkipButton)
        val failButton : ImageButton = layout.findViewById(R.id.taskFailButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskModel = data[position]
        holder.titleTextView.text = taskModel.habit.name
        holder.scoreTextView.text = context.getString(R.string.score_text, taskModel.habit.score)

        val iconKey = taskModel.habit.iconKey

        if(iconKey != null)
            holder.iconView.setImageDrawable(iconManager.getIconByKey(iconKey))

        holder.successButton.setOnClickListener {
            animateExit(TaskManager.STATUS_SUCCESS, holder, taskModel, position)
        }

        holder.failButton.setOnClickListener {
            animateExit(TaskManager.STATUS_FAILED, holder, taskModel, position)
        }

        holder.skipButton.setOnClickListener {
            animateExit(TaskManager.STATUS_SKIPPED, holder, taskModel, position)
        }
    }

    private fun animateExit(status : String, viewHolder: ViewHolder, taskModel: TaskModel, position : Int) {
        if(!taskModel.enabled) return

        taskListener?.createTaskLog(taskModel, status)

        taskModel.enabled = false

        val layout = viewHolder.layout

        val animationDuration = context.resources.getInteger(android.R.integer.config_mediumAnimTime)
        val animationSet = AnimationSet(true)
        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = animationDuration.toLong()
        animationSet.addAnimation(alphaAnimation)

        when(status) {
            TaskManager.STATUS_SUCCESS -> {
            }
        }

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                layout.visibility = View.GONE
                data.remove(taskModel)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        layout.startAnimation(animationSet)
    }
}