package com.santtuhyvarinen.habittracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.callbacks.TaskModelDiffCallback
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.SettingsUtil
import com.santtuhyvarinen.habittracker.utils.StatisticsUtil
import com.santtuhyvarinen.habittracker.utils.TaskUtil

class TasksAdapter(private var context: Context, private val iconManager: IconManager) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private var animationPlaying = false

    private val displayTaskTaskValue = SettingsUtil.getDisplayTaskStatValue(context)

    var data : ArrayList<TaskModel> = ArrayList()
    private var holdData : ArrayList<TaskModel>? = null

    var tasksAdapterListener : TasksAdapterListener? = null
    interface TasksAdapterListener {
        fun createTaskLog(taskModel: TaskModel, status : String)
        fun allTasksDone()
    }

    class ViewHolder(var layout : View) : RecyclerView.ViewHolder(layout) {
        val titleTextView : TextView = layout.findViewById(R.id.title)
        val scoreTextView : TextView = layout.findViewById(R.id.scoreTextView)
        val iconView : ImageView = layout.findViewById(R.id.icon)

        val successButton : ImageButton = layout.findViewById(R.id.taskSuccessButton)
        val skipButton : ImageButton = layout.findViewById(R.id.taskSkipButton)
        val failButton : ImageButton = layout.findViewById(R.id.taskFailButton)

        val taskPopUp : TextView = layout.findViewById(R.id.taskPopUp)
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

        val habitName = taskModel.habitWithTaskLogs.habit.name
        holder.titleTextView.text = habitName

        when(displayTaskTaskValue) {
            SettingsUtil.TASK_STAT_STREAK -> {
                val score = taskModel.habitWithTaskLogs.habit.score
                holder.scoreTextView.text = context.getString(R.string.score_text, score)
                holder.scoreTextView.contentDescription = context.getString(R.string.score_content_description, score)
            }
            SettingsUtil.TASK_STAT_TOTAL -> {
                val totalSuccesses = StatisticsUtil.getTotalSuccesses(taskModel.habitWithTaskLogs)
                holder.scoreTextView.text = totalSuccesses.toString()
                holder.scoreTextView.contentDescription = context.getString(R.string.total_completed_content_description, totalSuccesses)
            }
            else -> {
                holder.scoreTextView.text = ""
                holder.scoreTextView.contentDescription = ""
            }
        }

        val iconKey = taskModel.habitWithTaskLogs.habit.iconKey
        if(iconKey != null)
            holder.iconView.setImageDrawable(iconManager.getIconByKey(iconKey))

        //Set content descriptions for task buttons
        holder.successButton.contentDescription = context.getString(R.string.task_set_success_content_description, habitName)
        holder.skipButton.contentDescription = context.getString(R.string.task_set_skip_content_description, habitName)
        holder.failButton.contentDescription = context.getString(R.string.task_set_failed_content_description, habitName)

        holder.successButton.setOnClickListener {
            animateExit(TaskUtil.STATUS_SUCCESS, holder, taskModel, position)
        }

        holder.failButton.setOnClickListener {
            animateExit(TaskUtil.STATUS_FAILED, holder, taskModel, position)
        }

        holder.skipButton.setOnClickListener {
            animateExit(TaskUtil.STATUS_SKIPPED, holder, taskModel, position)
        }
    }

    private fun animateExit(status : String, viewHolder: ViewHolder, taskModel: TaskModel, position : Int) {
        if(!taskModel.enabled) return

        tasksAdapterListener?.createTaskLog(taskModel, status)

        taskModel.enabled = false

        val layout = viewHolder.layout

        val animationDuration = context.resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        val animationSet = AnimationSet(true)
        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = animationDuration
        animationSet.addAnimation(alphaAnimation)
        animationSet.interpolator = AccelerateDecelerateInterpolator()

        viewHolder.taskPopUp.visibility = View.VISIBLE

        when(status) {
            TaskUtil.STATUS_SUCCESS -> {
                //Task marked as success animation
                viewHolder.taskPopUp.text = context.getString(R.string.task_done)
                viewHolder.taskPopUp.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSuccess))

                val scaleAnimation = ScaleAnimation(
                    1f,
                    1.1f,
                    1f,
                    1.1f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )

                scaleAnimation.duration = animationDuration
                animationSet.addAnimation(scaleAnimation)
            }

            TaskUtil.STATUS_SKIPPED -> {
                viewHolder.taskPopUp.text = context.getString(R.string.task_skipped)
                viewHolder.taskPopUp.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSkipped))
                viewHolder.taskPopUp.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimaryLight))

                //Task marked as skipped animation
                val translateAnimation = TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0.1f,
                    Animation.RELATIVE_TO_SELF,
                    0f,
                    Animation.RELATIVE_TO_SELF,
                    0f
                )

                translateAnimation.duration = animationDuration
                animationSet.addAnimation(translateAnimation)
            }

            TaskUtil.STATUS_FAILED -> {
                //Task marked as failed animation
                viewHolder.taskPopUp.visibility = View.VISIBLE
                viewHolder.taskPopUp.text = context.getString(R.string.task_failed)
                viewHolder.taskPopUp.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFail))

                val scaleAnimation = ScaleAnimation(
                    1f,
                    0.9f,
                    1f,
                    0.9f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )

                scaleAnimation.duration = animationDuration
                animationSet.addAnimation(scaleAnimation)
            }
        }

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                layout.visibility = View.GONE
                animationPlaying = false

                val dataToUpdate = holdData
                if(dataToUpdate != null) {
                    updateData(dataToUpdate)
                }
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        layout.startAnimation(animationSet)
        animationPlaying = true
    }

    fun updateData(newData : ArrayList<TaskModel>) {
        if(!animationPlaying) {
            val result = DiffUtil.calculateDiff(TaskModelDiffCallback(data, newData))
            data = newData
            result.dispatchUpdatesTo(this)

            holdData = null
        } else {
            holdData = newData
        }
    }
}