package com.santtuhyvarinen.habittracker.views

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.models.WeekDaysSelectionModel
import kotlinx.android.synthetic.main.layout_weekday_picker.view.*

class WeekDayPickerView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    var weekDaySelectedListener : WeekDaySelectedListener? = null
    interface WeekDaySelectedListener {
        fun weekDaySelected(index : Int, selected : Boolean)
    }

    private lateinit var buttons : Array<Button>

    init {
        inflate(context, R.layout.layout_weekday_picker, this)

        initializeWeekDayButtons()
    }

    private fun initializeWeekDayButtons() {
        buttons = arrayOf(button1, button2, button3, button4, button5, button6, button7)

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