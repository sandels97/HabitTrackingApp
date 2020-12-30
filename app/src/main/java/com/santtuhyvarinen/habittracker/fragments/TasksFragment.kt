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
import com.santtuhyvarinen.habittracker.models.TaskModel
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.SettingsUtil
import com.santtuhyvarinen.habittracker.viewmodels.TasksViewModel
import kotlinx.android.synthetic.main.fragment_habits.*
import kotlinx.android.synthetic.main.fragment_tasks.*
import kotlinx.android.synthetic.main.fragment_tasks.recyclerView
import kotlinx.android.synthetic.main.layout_time_bar.*

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

        tasksAdapter = TasksAdapter(requireContext(), tasksViewModel.iconManager)
        tasksAdapter.taskListener = object : TasksAdapter.TaskListener {
            override fun taskMarkedDone(taskModel: TaskModel) {
                tasksViewModel.setTaskAsDone(taskModel)
                SettingsUtil.vibrateDevice(requireContext())
            }
        }

        //Observer habits from database
        val habitsObserver = Observer<List<Habit>> { list ->
            tasksViewModel.generateTasks(requireContext(), list)
        }
        tasksViewModel.setHabitsObserver(viewLifecycleOwner, habitsObserver)

        //Observer tasks
        val tasksObserver = Observer<ArrayList<TaskModel>> { list ->
            tasksAdapter.data = list
            tasksAdapter.notifyDataSetChanged()
        }

        tasksViewModel.setTasksObserver(viewLifecycleOwner, tasksObserver)

        recyclerView.adapter = tasksAdapter

        updateTimeBar()
    }

    fun updateTimeBar() {
        timeBarWeekDayText.text = CalendarUtil.getCurrentWeekDayText(requireContext())
        timeBarDateText.text = CalendarUtil.getCurrentDateText()
    }
}