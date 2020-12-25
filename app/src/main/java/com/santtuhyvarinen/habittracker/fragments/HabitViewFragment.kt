package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.activities.MainActivity
import com.santtuhyvarinen.habittracker.viewmodels.HabitViewModel

class HabitViewFragment : Fragment() {

    private lateinit var habitViewModel : HabitViewModel

    private val args: HabitViewFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_habit_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        //Edit button on Activity ToolBar
        val activity = (activity as MainActivity)
        activity.getToolBarEditButton().setOnClickListener {
            val action = HabitViewFragmentDirections.actionFromHabitViewFragmentToHabitFormFragment(args.habitId)
            findNavController().navigate(action)
        }
    }
}