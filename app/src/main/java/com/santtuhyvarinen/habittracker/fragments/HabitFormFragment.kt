package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.IconSelectionAdapter
import com.santtuhyvarinen.habittracker.databinding.FragmentHabitFormBinding
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.models.IconModel
import com.santtuhyvarinen.habittracker.utils.HabitInfoUtil
import com.santtuhyvarinen.habittracker.viewmodels.HabitFormViewModel
import com.santtuhyvarinen.habittracker.views.WeekDayPickerView

class HabitFormFragment : Fragment() {

    private var _binding: FragmentHabitFormBinding? = null
    private val binding get() = _binding!!

    private val args : HabitFormFragmentArgs by navArgs()

    private lateinit var habitFormViewModel : HabitFormViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHabitFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitFormViewModel = ViewModelProvider(this).get(HabitFormViewModel::class.java)
        habitFormViewModel.initialize(args.habitId)

        //If editing a habit, load the existing habit values to correct fields
        val habitObserver = Observer<Habit> { habit ->
            if(habit != null) {
                habitFormViewModel.loading = false
                updateHabitValues(habit)
                updateLoadingProgressVisibility()
            }
        }
        habitFormViewModel.habitData.observe(viewLifecycleOwner, habitObserver)

        //Navigate to HabitView after saving the habit
        val saveHabitObserver = Observer<Long> { id ->
            if(id >= 0) {
                val action = HabitFormFragmentDirections.actionToHabitViewFragment(id)
                findNavController().navigate(action)
            }
        }
        habitFormViewModel.habitDataSaved.observe(viewLifecycleOwner, saveHabitObserver)

        binding.habitNameEditText.requestFocus()
        binding.habitNameEditText.setOnEditorActionListener { _, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                binding.habitNameEditText.clearFocus()
            }
            return@setOnEditorActionListener false
        }

        //WeekDayPicker
        binding.weekDayPicker.weekDaySelectedListener = object : WeekDayPickerView.WeekDaySelectedListener {
            override fun weekDaySelected(index: Int, selected: Boolean) {
                habitFormViewModel.weekDaysSelectionModel.selectedWeekDayButtons[index] = selected
                updateWeekDayHeader()
            }
        }

        binding.weekDayPicker.updateFromWeekDaysModel(habitFormViewModel.weekDaysSelectionModel)

        //Priority SeekBar
        binding.habitPrioritySeekBar.max = HabitInfoUtil.MAX_PRIORITY_LEVEL
        binding.habitPrioritySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) {
                habitFormViewModel.priorityValue = progress
                updatePriorityHeader()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        //IconPicker
        binding.iconPickerView.iconManager = habitFormViewModel.iconManager
        binding.iconPickerView.setIconSelectedListener(object : IconSelectionAdapter.IconSelectedListener {
            override fun iconSelected(iconModel: IconModel?) {
                habitFormViewModel.selectedIconModel = iconModel
            }
        })
        binding.iconPickerView.setSelectedIcon(habitFormViewModel.selectedIconModel)

        //Save button
        binding.saveHabitButton.text = if(habitFormViewModel.isEditingExistingHabit()) getString(R.string.save_changes) else getString(R.string.create_habit)
        binding.saveHabitButton.setOnClickListener {
            habitFormViewModel.habitName = binding.habitNameEditText.text.toString()

            habitFormViewModel.saveHabit(requireContext())
        }

        updatePriorityHeader()
        updateWeekDayHeader()
        updateLoadingProgressVisibility()
    }

    private fun updateLoadingProgressVisibility() {
        binding.progress.apply {
            visibility = if(habitFormViewModel.loading) View.VISIBLE else View.GONE
        }
        binding.scrollView.apply {
            visibility = if(habitFormViewModel.loading) View.GONE else View.VISIBLE
        }
        binding.saveHabitButton.apply {
            visibility = if(habitFormViewModel.loading) View.GONE else View.VISIBLE
        }
    }

    private fun updatePriorityHeader() {
        val currentPriority = HabitInfoUtil.getPriorityLevelText(requireContext(), habitFormViewModel.priorityValue)
        binding.habitPriorityHeader.text = getString(R.string.habit_priority_header, currentPriority)
    }

    private fun updateWeekDayHeader() {
        binding.weekDayPickerHeader.text = habitFormViewModel.getRecurrenceHeader(requireContext())
    }

    private fun updateHabitValues(habit: Habit) {
        binding.habitNameEditText.setText(habit.name)
        binding.habitPrioritySeekBar.progress = habit.priority

        //Icon
        binding.iconPickerView.setSelectedIcon(habitFormViewModel.selectedIconModel)

        //WeekDays
        binding.weekDayPicker.updateFromWeekDaysModel(habitFormViewModel.weekDaysSelectionModel)
        updateWeekDayHeader()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}