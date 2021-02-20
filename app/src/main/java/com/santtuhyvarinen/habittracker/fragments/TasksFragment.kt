package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.TasksAdapter
import com.santtuhyvarinen.habittracker.databinding.FragmentTasksBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.SettingsUtil
import com.santtuhyvarinen.habittracker.viewmodels.TasksViewModel
import java.util.*

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasksViewModel : TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)

        //Tasks RecyclerView adapter
        tasksAdapter = TasksAdapter(requireContext(), tasksViewModel.iconManager)
        tasksAdapter.tasksAdapterListener = object : TasksAdapter.TasksAdapterListener {
            override fun createTaskLog(taskModel: TaskModel, status : String) {
                tasksViewModel.createTaskLog(taskModel, status)

                SettingsUtil.sendTouchFeedback(requireContext())
            }

            override fun allTasksDone() {
                updateMessageVisibility(true)
            }
        }
        binding.recyclerView.adapter = tasksAdapter

        //Observer habits from database
        val habitsObserver = Observer<List<HabitWithTaskLogs>> { list ->
            tasksViewModel.generateDailyTasks(list)
        }

        tasksViewModel.getHabitsWithTaskLogs().observe(viewLifecycleOwner, habitsObserver)

        //Observer tasks
        val tasksObserver = Observer<ArrayList<TaskModel>> { list ->
            updateTasks(list)
        }

        tasksViewModel.getTasks().observe(viewLifecycleOwner, tasksObserver)

        updateTimeBar()

        //Set message text and icon
        binding.layoutMessage.messageText.text = getString(R.string.all_tasks_done)
        binding.layoutMessage.messageIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_thumb_up))
    }

    private fun updateTasks(taskList : ArrayList<TaskModel>) {
        //Sort tasks based on the task sort setting
        when(SettingsUtil.getSortTasksValue(requireContext())) {
            SettingsUtil.TASK_SORT_PRIORITY -> taskList.sortWith (compareByDescending<TaskModel> { it.habitWithTaskLogs.habit.priority }.thenBy { it.habitWithTaskLogs.habit.name } )
            SettingsUtil.TASK_SORT_ALPHABET -> taskList.sortWith (compareBy { it.habitWithTaskLogs.habit.name })
            SettingsUtil.TASK_SORT_SCORE -> taskList.sortWith(compareByDescending { it.habitWithTaskLogs.habit.score })
        }

        tasksAdapter.updateData(taskList)

        updateMessageVisibility(taskList.isEmpty())

        binding.progress.hide()
    }

    private fun updateMessageVisibility(visible : Boolean) {
        val previousLayoutMessageVisibility = binding.layoutMessage.messageContainer.visibility
        binding.layoutMessage.messageContainer.visibility = if(visible) View.VISIBLE else View.GONE

        if(previousLayoutMessageVisibility == View.GONE && visible) {
            val alphaAnimation = AlphaAnimation(0f, 1f)
            alphaAnimation.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            binding.layoutMessage.messageContainer.startAnimation(alphaAnimation)
        }

        //Set message content
        if(tasksViewModel.hasHabits()) {
            binding.layoutMessage.messageText.text = getString(R.string.all_tasks_done)
            binding.layoutMessage.messageIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_notification_tasks_done))
        } else {
            binding.layoutMessage.messageText.text = getString(R.string.no_habits_message)
            binding.layoutMessage.messageIcon.setImageDrawable(null)
        }
    }

    private fun updateTimeBar() {
        binding.timeBar.timeBarWeekDayText.text =
            CalendarUtil.getCurrentWeekDayText(requireContext()).capitalize(Locale.ROOT)
        binding.timeBar.timeBarDateText.text = CalendarUtil.getCurrentDateText(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}