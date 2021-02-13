package com.santtuhyvarinen.habittracker.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.databinding.DialogMarkTaskBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.utils.CalendarUtil
import com.santtuhyvarinen.habittracker.utils.TaskUtil
import org.joda.time.DateTime
import java.sql.Timestamp

class MarkTaskDialog (context: Context, private val habitWithTaskLogs: HabitWithTaskLogs) : Dialog(context) {

    private var selectedDateTimestamp : Long = System.currentTimeMillis()

    var onTaskMarkedListener : OnTaskMarkedListener? = null
    interface OnTaskMarkedListener {
        fun taskMarkedComplete(timestamp: Long)
    }

    private var _binding: DialogMarkTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DialogMarkTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.markTaskButton.setOnClickListener {
            if(isMarkTaskButtonEnabled()) {
                onTaskMarkedListener?.taskMarkedComplete(selectedDateTimestamp)
                dismiss()
            } else {
                showTaskAlreadyMarkedToast()
            }
        }

        updateDate()
    }

    private fun showDatePickerDialog() {
        val selectedDate = DateTime(selectedDateTimestamp)
        val dialog = DatePickerDialog(context, { _, year, month, day ->
            val dateTime = DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day)
            selectedDateTimestamp = dateTime.millis

            updateDate()
        }, selectedDate.year, selectedDate.monthOfYear - 1, selectedDate.dayOfMonth)

        dialog.show()
    }

    private fun updateDate() {
        binding.dateText.text = CalendarUtil.getDateText(selectedDateTimestamp)

        val markTaskButtonEnabled = isMarkTaskButtonEnabled()
        binding.markTaskButton.alpha = if(markTaskButtonEnabled) 1f else 0.5f

        if(!markTaskButtonEnabled) showTaskAlreadyMarkedToast()
    }

    private fun showTaskAlreadyMarkedToast() {
        Toast.makeText(context, context.getString(R.string.task_already_marked), Toast.LENGTH_SHORT).show()
    }

    private fun isMarkTaskButtonEnabled() : Boolean {
        return !TaskUtil.hasTaskLogForDate(habitWithTaskLogs, selectedDateTimestamp)
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }
}