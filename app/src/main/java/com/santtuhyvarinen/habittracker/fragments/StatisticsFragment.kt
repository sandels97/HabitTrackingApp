package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.databinding.FragmentStatisticsBinding
import com.santtuhyvarinen.habittracker.databinding.LayoutStatBinding
import com.santtuhyvarinen.habittracker.models.HabitWithTaskLogs
import com.santtuhyvarinen.habittracker.utils.StatisticsUtil
import com.santtuhyvarinen.habittracker.viewmodels.HabitsViewModel
import com.santtuhyvarinen.habittracker.viewmodels.StatisticsViewModel

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
            updateStats(list)
        }

        statisticsViewModel.getHabitsWithTaskLogs().observe(viewLifecycleOwner, habitsObserver)

        setStatHeader(binding.statHabits, getString(R.string.stat_habits))
        setStatHeader(binding.statTotalSuccesses, getString(R.string.total_success))

        updateLineGraphView()

        return binding.root
    }

    private fun updateLineGraphView() {
        binding.lineGraphView.values = listOf(0, 1, 2, 3, 2, 3, 2, 1)
        binding.lineGraphView.invalidate()
    }

    private fun updateStats(habits : List<HabitWithTaskLogs>) {
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