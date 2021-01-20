package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.databinding.LayoutWeekdayPickerBinding
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel

class WeekDayPickerView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private val binding : LayoutWeekdayPickerBinding = LayoutWeekdayPickerBinding.inflate(LayoutInflater.from(context), this, true)

    var weekDaySelectedListener : WeekDaySelectedListener? = null
    interface WeekDaySelectedListener {
        fun weekDaySelected(index : Int, selected : Boolean)
    }

    private lateinit var buttons : Array<Button>

    init {
        initializeWeekDayButtons()
    }

    private fun initializeWeekDayButtons() {
        buttons = arrayOf(binding.button1, binding.button2, binding.button3, binding.button4, binding.button5, binding.button6, binding.button7)

        val weekDayLetters = context.resources.getStringArray(R.array.WeekDaysLetters)
        val weekDayContentDescriptions = context.resources.getStringArray(R.array.WeekDays)

        for(i in buttons.indices) {
            val button = buttons[i]
            button.text = weekDayLetters[i]
            button.contentDescription = weekDayContentDescriptions[i]

            button.setOnClickListener {
                val newValue = !button.isSelected
                button.isSelected = newValue
                weekDaySelectedListener?.weekDaySelected(i, newValue)
            }
        }
    }

    fun updateFromWeekDaysModel(weekDaysSelectionModel: WeekDaysSelectionModel) {
        for(i in weekDaysSelectionModel.selectedWeekDayButtons.indices) {
            setWeekDayButtonSelected(i, weekDaysSelectionModel.selectedWeekDayButtons[i])
        }
    }

    fun setWeekDayButtonSelected(index : Int, selected : Boolean) {
        if(index < 0 || index >= 7) return
        buttons[index].isSelected = selected
    }
}