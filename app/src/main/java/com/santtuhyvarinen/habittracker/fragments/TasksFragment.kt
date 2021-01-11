package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.TasksAdapter
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.SettingsUtil
import com.santtuhyvarinen.habittracker.viewmodels.TasksViewModel
import kotlinx.android.synthetic.main.fragment_tasks.recyclerView
import kotlinx.android.synthetic.main.layout_time_bar.*
import java.util.*
import kotlin.collections.ArrayList

class TasksFragment : Fragment() {

    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasksViewModel : TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)

        //Tasks RecyclerView adapter
        tasksAdapter = TasksAdapter(requireContext(), tasksViewModel.iconManager)
        tasksAdapter.taskListener = object : TasksAdapter.TaskListener {
            override fun taskMarkedDone(taskModel: TaskModel) {
                tasksViewModel.setTaskAsDone(taskModel)
                SettingsUtil.vibrateDevice(requireContext())
            }
        }
        recyclerView.adapter = tasksAdapter

        //Observer habits from database
        val habitsObserver = Observer<List<HabitWithTaskLogs>> { list ->
            tasksViewModel.generateDailyTasks(requireContext(), list)
        }

        tasksViewModel.getHabitsWithTaskLogs().observe(viewLifecycleOwner, habitsObserver)

        //Observer tasks
        val tasksObserver = Observer<ArrayList<TaskModel>> { list ->
            tasksAdapter.data = list
            tasksAdapter.notifyDataSetChanged()
        }

        tasksViewModel.getTasks().observe(viewLifecycleOwner, tasksObserver)

        updateTimeBar()
    }

    private fun updateTimeBar() {
        timeBarWeekDayText.text =
            CalendarUtil.getCurrentWeekDayText(requireContext()).capitalize(Locale.ROOT)
        timeBarDateText.text = CalendarUtil.getCurrentDateText()
    }
}