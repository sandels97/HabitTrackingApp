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
import androidx.recyclerview.widget.RecyclerView
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.managers.IconManager
import com.santtuhyvarinen.habittracker.managers.TaskManager
import com.santtuhyvarinen.habittracker.models.TaskModel

class TasksAdapter(private var context: Context, private val iconManager: IconManager) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    var data : ArrayList<TaskModel> = ArrayList()

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
            TaskManager.STATUS_SUCCESS -> {
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

            TaskManager.STATUS_SKIPPED -> {
                viewHolder.taskPopUp.text = context.getString(R.string.task_skipped)
                viewHolder.taskPopUp.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSkipped))
                viewHolder.taskPopUp.setTextColor(ContextCompat.getColor(context, R.color.colorTextSecondary))

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

            TaskManager.STATUS_FAILED -> {
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
                data.remove(taskModel)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)

                if(data.isEmpty()) tasksAdapterListener?.allTasksDone()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })

        layout.startAnimation(animationSet)
    }
}