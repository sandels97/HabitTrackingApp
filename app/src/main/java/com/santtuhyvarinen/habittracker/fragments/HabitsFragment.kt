package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.HabitsListAdapter
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.viewmodels.HabitsViewModel
import kotlinx.android.synthetic.main.fragment_habits.*


class HabitsFragment : Fragment() {
    
    private lateinit var habitsAdapter: HabitsListAdapter
    private lateinit var habitsViewModel : HabitsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitsViewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)

        //Observer habits from database
        val habitsObserver = Observer<List<Habit>> { list ->
            habitsAdapter.data = list
            habitsAdapter.notifyDataSetChanged()
            progress.visibility = View.GONE
        }
        habitsViewModel.setHabitsObserver(viewLifecycleOwner, habitsObserver)

        habitsAdapter = HabitsListAdapter(requireContext(), habitsViewModel.iconManager)
        recyclerView.adapter = habitsAdapter
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, (recyclerView.layoutManager as LinearLayoutManager).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)

        habitsAdapter.habitClickedListener = object : HabitsListAdapter.HabitClickedListener {
            override fun habitClicked(habit: Habit) {
                openHabitView(habit)
            }
        }
    }

    fun openHabitView(habit: Habit) {
        val action = HabitsFragmentDirections.actionFromHabitsFragmentToHabitViewFragment(habit.id)
        findNavController().navigate(action)
    }
}