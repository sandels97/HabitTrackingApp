package com.santtuhyvarinen.habittracker.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.databinding.FragmentStatisticsBinding
import com.santtuhyvarinen.habittracker.databinding.LayoutStatBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.models.LineGraphDataModel
import com.santtuhyvarinen.habittracker.utils.StatisticsUtil
import com.santtuhyvarinen.habittracker.viewmodels.StatisticsViewModel
import org.joda.time.DateTime

class StatisticsFragment : Fragment() {

    private lateinit var statisticsViewModel : StatisticsViewModel

    private var _binding : FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)

        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)

        //Observer habits from database
        val habitsObserver = Observer<List<HabitWithTaskLogs>> { list ->
            statisticsViewModel.habitsWithTaskLogs = list
            updateStats()
            statisticsViewModel.generateLineGraphData()
        }

        statisticsViewModel.getHabitsWithTaskLogs().observe(viewLifecycleOwner, habitsObserver)

        val lineGraphDataObserver = Observer<List<LineGraphDataModel>> { list ->
            updateLineGraphView(list)
        }

        binding.selectColumnsLineGraphViewButton.setOnClickListener {
            showColumnsMenu()
        }

        binding.selectDateLineGraphViewButton.setOnClickListener {
            showDatePickerDialog()
        }

        statisticsViewModel.getLineGraphData().observe(viewLifecycleOwner, lineGraphDataObserver)

        setStatHeader(binding.statHabits, getString(R.string.stat_habits))
        setStatHeader(binding.statTotalSuccesses, getString(R.string.total_success))

        return binding.root
    }

    private fun updateLineGraphView(data : List<LineGraphDataModel>) {

        binding.lineGraphView.lineGraphData = data
        binding.lineGraphView.columns = statisticsViewModel.lineGraphColumns
        binding.lineGraphView.rows = if(data.isNotEmpty()) (data.maxOf { it.value }.coerceAtLeast(5)) + 1 else 0

        binding.lineGraphView.invalidate()
    }

    private fun showDatePickerDialog() {
        val selectedDate = statisticsViewModel.getSelectedDate()
        val dialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            val dateTime = DateTime.now().withYear(year).withMonthOfYear(month + 1).withDayOfMonth(day)
            statisticsViewModel.setSelectedDate(dateTime)
        }, selectedDate.year, selectedDate.monthOfYear - 1, selectedDate.dayOfMonth)

        dialog.show()
    }

    private fun showColumnsMenu() {
        val popupMenu = PopupMenu(activity, binding.selectColumnsLineGraphViewButton)

        popupMenu.menuInflater.inflate(R.menu.menu_select_columns, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->

            when(item.itemId) {
                R.id.columnsWeekView -> statisticsViewModel.setColumns(7)
                R.id.columnsTwoWeeksView -> statisticsViewModel.setColumns(14)
                R.id.columnsMonthView -> statisticsViewModel.setColumns(30)
            }

            true
        }

        popupMenu.show()
    }

    private fun updateStats() {
        val habits = statisticsViewModel.habitsWithTaskLogs
        updateStatValue(binding.statHabits, habits.size.toString())
        updateStatValue(binding.statTotalSuccesses, StatisticsUtil.getTotalSuccessesForHabits(habits).toString())
    }

    private fun setStatHeader(stat : LayoutStatBinding, headerText : String) {
        stat.statHeaderText.text = headerText
    }

    private fun updateStatValue(stat : LayoutStatBinding, value : String) {
        stat.statValueText.text = value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}