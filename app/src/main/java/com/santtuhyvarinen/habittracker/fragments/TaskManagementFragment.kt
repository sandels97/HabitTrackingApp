package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.santtuhyvarinen.habittracker.adapters.TaskManagementAdapter
import com.santtuhyvarinen.habittracker.databinding.FragmentTaskManagementBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.viewmodels.TaskManagementViewModel

class TaskManagementFragment : Fragment() {

    private val args : TaskManagementFragmentArgs by navArgs()

    private lateinit var taskManagementAdapter : TaskManagementAdapter
    private lateinit var taskManagementViewModel: TaskManagementViewModel

    private var _binding: FragmentTaskManagementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.habitId < 0) {
            findNavController().navigateUp()
            return
        }

        taskManagementViewModel = ViewModelProvider(this).get(TaskManagementViewModel::class.java)
        taskManagementViewModel.initialize(args.habitId)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskManagementAdapter = TaskManagementAdapter(requireContext())
        binding.recyclerView.adapter = taskManagementAdapter

        val habitObserver = Observer<HabitWithTaskLogs> {
            updateData(it)
        }

        taskManagementViewModel.getHabitWithTaskLogsLiveData().observe(viewLifecycleOwner, habitObserver)
    }

    private fun updateData(habitWithTaskLogs: HabitWithTaskLogs) {

        binding.habitNameText.text = habitWithTaskLogs.habit.name
        binding.habitIcon.setImageDrawable(taskManagementViewModel.iconManager.getIconByKey(habitWithTaskLogs.habit.iconKey))
        
        taskManagementAdapter.updateData(habitWithTaskLogs.taskLogs)
        binding.progress.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}