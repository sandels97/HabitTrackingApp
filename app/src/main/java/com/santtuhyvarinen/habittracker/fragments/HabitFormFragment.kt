package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        saveHabitButton.setOnClickListener {
            val saveSuccess = habitFormViewModel.saveHabit()

            if(saveSuccess) findNavController().navigateUp()
        }

        weekDayPicker.weekDaySelectedListener = object : WeekDayPicker.WeekDaySelectedListener {
            override fun weekDaySelected(index: Int, selected: Boolean) {
                habitFormViewModel.selectedWeekDayButtons[index] = selected
                updateWeekDayHeader()
            }
        }

        for(i in habitFormViewModel.selectedWeekDayButtons.indices) {
            weekDayPicker.setWeekDayButtonSelected(i, habitFormViewModel.selectedWeekDayButtons[i])
        }

        updateWeekDayHeader()
    }

    private fun updateWeekDayHeader() {
        if(habitFormViewModel.isEveryDaySelectedOrNotSelected()) {
            weekDayPickerHeader.text = getString(R.string.habit_repeat_every_day)
        } else {
            //Show selected week days in to the header

            val daysSelected = habitFormViewModel.daysSelected()

            val weekDays = if(daysSelected < 3) resources.getStringArray(R.array.WeekDays) else resources.getStringArray(R.array.WeekDaysShort)

            var index = 0

            val stringBuilder = StringBuilder()
            for(i in weekDays.indices) {
                if(habitFormViewModel.isDaySelected(i)) {
                    val weekDay = weekDays[i]
                    stringBuilder.append(weekDay)
                    index ++

                    if(daysSelected > 1) {
                        if (index == daysSelected - 1) {
                            stringBuilder.append(" ${getString(R.string.and)} ")
                        } else if (index < daysSelected) {
                            stringBuilder.append(", ")
                        }
                    }
                }
            }


            weekDayPickerHeader.text = getString(R.string.habit_repeat_days, stringBuilder.toString())
        }
    }
}