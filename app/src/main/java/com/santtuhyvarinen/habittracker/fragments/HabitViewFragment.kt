package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.activities.MainActivity
import com.santtuhyvarinen.habittracker.databinding.FragmentHabitViewBinding
import com.santtuhyvarinen.habittracker.databinding.LayoutStatBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.HabitInfoUtil
import com.santtuhyvarinen.habittracker.utils.StatisticsUtil
import com.santtuhyvarinen.habittracker.viewmodels.HabitViewModel

class HabitViewFragment : Fragment() {

    private var _binding: FragmentHabitViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var habitViewModel : HabitViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHabitViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val args: HabitViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //If habitId is negative, should navigate up and exit fragment
        if(args.habitId < 0) {
            findNavController().navigateUp()
            return
        }

        updateProgress(false)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
        habitViewModel.initialize(args.habitId)

        //Observe Habit
        val habitObserver = Observer<HabitWithTaskLogs?> { habit ->
            if(habit != null) {
                updateHabitValues(habit)
            } else {
                //Could not load habit
                Toast.makeText(requireContext(), getString(R.string.error_load_habit), Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
        habitViewModel.getHabitWithTaskLogsLiveData().observe(viewLifecycleOwner, habitObserver)

        //Observe ShouldExitView variable to exit the fragment
        val shouldExitViewObserver = Observer<Boolean> { exit ->
            if(exit) findNavController().navigateUp()
        }
        habitViewModel.getShouldExitView().observe(viewLifecycleOwner, shouldExitViewObserver)

        //Edit button on Activity ToolBar
        val activity = (activity as MainActivity)
        activity.getEditButton().setOnClickListener {
            if(context == null) return@setOnClickListener

            val action = HabitViewFragmentDirections.actionFromHabitViewFragmentToHabitFormFragment(args.habitId)
            findNavController().navigate(action)
        }

        //Delete button on Activity ToolBar
        activity.getDeleteButton().setOnClickListener {
            if(context == null) return@setOnClickListener

            showDeleteConfirmationDialog()
        }

        binding.habitDisableSwitch.setOnClickListener {
            handleHabitDisableSwitch(binding.habitDisableSwitch.isChecked)
        }

        binding.viewTaskLogsButton.setOnClickListener {
            navigateToTaskManagement()
        }

        //Update stat headers
        setStatHeader(binding.statCreated, getString(R.string.created))
        setStatHeader(binding.statTotalSuccesses, getString(R.string.total_success))
        setStatHeader(binding.statHighestScore, getString(R.string.highest_score))
    }

    private fun handleHabitDisableSwitch(enabled : Boolean) {
        habitViewModel.setHabitEnabled(enabled)

        val toastStringId = if (enabled) R.string.tasks_enabled else R.string.tasks_disabled
        Toast.makeText(requireContext(), getString(toastStringId), Toast.LENGTH_SHORT).show()
    }

    private fun updateProgress(showLayout : Boolean) {
        binding.progress.visibility = if(showLayout) View.GONE else View.VISIBLE
        binding.habitInfoLayout.visibility = if(showLayout) View.VISIBLE else View.GONE
        binding.viewTaskLogsButton.visibility = if(showLayout) View.VISIBLE else View.GONE
    }

    private fun updateHabitValues(habitWithTaskLogs: HabitWithTaskLogs) {
        val habit = habitWithTaskLogs.habit
        binding.habitNameText.text = habit.name

        //Priority text
        val priorityText = HabitInfoUtil.getPriorityLevelText(requireContext(), habit.priority)
        binding.habitPriorityText.text = getString(R.string.habit_priority_header, priorityText)

        //WeekDaysText
        val recurrenceText = habitViewModel.getRecurrenceText(requireContext(), habit)
        binding.habitRecurrenceText.text = recurrenceText

        //Icon
        val iconKey = habit.iconKey
        if (iconKey != null)
            binding.habitIcon.setImageDrawable(habitViewModel.iconManager.getIconByKey(iconKey))

        //Score
        val score = habit.score
        binding.scoreTextView.text = getString(R.string.score_text, score)
        binding.scoreTextView.contentDescription = getString(R.string.score_content_description, score)

        //Disabled
        binding.habitDisableSwitch.isChecked = !habit.disabled

        //Update stats
        updateStatValue(binding.statCreated, CalendarUtil.getDateText(habitWithTaskLogs.habit.creationDate, requireContext()))
        updateStatValue(binding.statTotalSuccesses, StatisticsUtil.getTotalSuccesses(habitWithTaskLogs).toString())

        val highestScore = StatisticsUtil.getHighestScore(habitWithTaskLogs.taskLogs)
        updateStatValue(binding.statHighestScore, getString(R.string.score_text, highestScore))

        binding.habitTimelineView.setup(habitWithTaskLogs)

        updateProgress(true)
    }

    private fun setStatHeader(stat : LayoutStatBinding, headerText : String) {
        stat.statHeaderText.text = headerText
    }

    private fun updateStatValue(stat : LayoutStatBinding, value : String) {
        stat.statValueText.text = value
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.delete_habit))
        alertDialog.setMessage(getString(R.string.habit_delete_confirmation))
        alertDialog.setPositiveButton(getString(R.string.delete)) { _, _ -> habitViewModel.deleteHabit(requireContext()) }

        alertDialog.setNegativeButton(getString(R.string.cancel), null)

        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun navigateToTaskManagement() {
        val direction = HabitViewFragmentDirections.actionFromHabitViewFragmentToTaskManagementFragment(args.habitId)
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}