package com.santtuhyvarinen.habittracker.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.TaskManagementAdapter
import com.santtuhyvarinen.habittracker.databinding.FragmentTaskManagementBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import com.santtuhyvarinen.habittracker.viewmodels.TaskManagementViewModel
import org.joda.time.DateTime

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

        val habitObserver = Observer<HabitWithTaskLogs?> {
            updateData(it)
        }
        taskManagementViewModel.getHabitWithTaskLogsLiveData().observe(viewLifecycleOwner, habitObserver)

        //Add task log
        val timestampObserver = Observer<Long> {
            updateDateText(it)
        }
        taskManagementViewModel.getSelectedDateTimestampLiveData().observe(viewLifecycleOwner, timestampObserver)

        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.addTaskLogButton.setOnClickListener {
            if(taskManagementViewModel.canAddTaskLog()) {
                val success = taskManagementViewModel.createTaskLog(TaskUtil.STATUS_SUCCESS)
                if(success)
                    Toast.makeText(requireContext(), getString(R.string.task_log_added), Toast.LENGTH_SHORT).show()

            } else {
                showTaskAlreadyMarkedToast()
            }
        }
    }

    private fun showTaskAlreadyMarkedToast() {
        Toast.makeText(requireContext(), getString(R.string.task_already_marked), Toast.LENGTH_SHORT).show()
    }

    private fun updateData(habitWithTaskLogs: HabitWithTaskLogs) {

        binding.habitNameText.text = habitWithTaskLogs.habit.name
        binding.habitIcon.setImageDrawable(taskManagementViewModel.iconManager.getIconByKey(habitWithTaskLogs.habit.iconKey))

        taskManagementAdapter.updateData(habitWithTaskLogs.taskLogs)
        binding.progress.visibility = View.GONE
    }

    private fun updateDateText(timestamp : Long) {
        binding.dateText.text = getString(R.string.for_date, CalendarUtil.getDateText(timestamp, requireContext()))
    }

    private fun showDatePickerDialog() {
        val selectedDate = DateTime(taskManagementViewModel.getSelectedDateTimestamp())
        val dialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            val dateTime = DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day)
            taskManagementViewModel.setSelectedDateTimestamp(dateTime.millis)
        }, selectedDate.year, selectedDate.monthOfYear - 1, selectedDate.dayOfMonth)

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}