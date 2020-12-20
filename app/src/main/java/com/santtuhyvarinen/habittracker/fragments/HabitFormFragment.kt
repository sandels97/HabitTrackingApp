package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.viewmodels.HabitFormViewModel
import com.santtuhyvarinen.habittracker.views.WeekDayPicker
import kotlinx.android.synthetic.main.fragment_habit_form.*

class HabitFormFragment : Fragment() {

    private lateinit var habitFormViewModel : HabitFormViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_habit_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitFormViewModel = ViewModelProvider(this).get(HabitFormViewModel::class.java)

        if(habitFormViewModel.priorityLevels.isEmpty()) habitFormViewModel.priorityLevels = resources.getStringArray(R.array.PriorityLevels)

        saveHabitButton.setOnClickListener {
            val saveSuccess = habitFormViewModel.saveHabit()

            if(saveSuccess) findNavController().navigateUp()
        }

        habitPrioritySeekBar.max = habitFormViewModel.getMaxPriorityLevel()
        habitPrioritySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar, progress: Int, fromUser: Boolean) {
                habitFormViewModel.priorityValue = progress
                updatePriorityHeader()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        weekDayPicker.weekDaySelectedListener = object : WeekDayPicker.WeekDaySelectedListener {
            override fun weekDaySelected(index: Int, selected: Boolean) {
                habitFormViewModel.selectedWeekDayButtons[index] = selected
                updateWeekDayHeader()
            }
        }

        for(i in habitFormViewModel.selectedWeekDayButtons.indices) {
            weekDayPicker.setWeekDayButtonSelected(i, habitFormViewModel.selectedWeekDayButtons[i])
        }

        updatePriorityHeader()
        updateWeekDayHeader()
    }

    private fun updatePriorityHeader() {
        val currentPriority = habitFormViewModel.getCurrentPriorityLevelText()
        habitPriorityHeader.text = getString(R.string.habit_priority_header, currentPriority)
    }

    private fun updateWeekDayHeader() {
        if(habitFormViewModel.isEveryDaySelectedOrNotSelected()) {
            weekDayPickerHeader.text = getString(R.string.habit_repeat_every_day)
        } else {
            //Show selected week days in to the header
            weekDayPickerHeader.text = getString(R.string.habit_repeat_days, habitFormViewModel.getWeekDaysSelectedText(requireContext()))
        }
    }
}