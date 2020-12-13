package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.TasksAdapter
import com.santtuhyvarinen.habittracker.viewmodels.TasksViewModel
import kotlinx.android.synthetic.main.fragment_tasks.*

class TasksFragment : Fragment() {

    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasksViewModel : TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tasksViewModel = ViewModelProvider(this).get(TasksViewModel::class.java)

        tasksAdapter = TasksAdapter(requireContext(), tasksViewModel.tasks)
        recyclerView.adapter = tasksAdapter
    }
}