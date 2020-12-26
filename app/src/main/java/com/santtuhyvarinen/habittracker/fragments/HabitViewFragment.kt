package com.santtuhyvarinen.habittracker.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.activities.MainActivity
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.viewmodels.HabitViewModel
import kotlinx.android.synthetic.main.fragment_habit_view.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class HabitViewFragment : Fragment() {

    private lateinit var habitViewModel : HabitViewModel

    private val args: HabitViewFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_habit_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //If there is no id set, navigate up
        if(args.habitId < 0) {
            findNavController().navigateUp()
            return
        }

        updateProgress(false)

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
        habitViewModel.initialize(requireContext(), args.habitId)

        val habitObserver = Observer<Habit> { habit ->
            if(habit != null) {
                updateHabitValues(habit)
            } else {
                findNavController().navigateUp()
            }
        }

        habitViewModel.habit.observe(viewLifecycleOwner, habitObserver)

        //Edit button on Activity ToolBar
        val activity = (activity as MainActivity)
        activity.editButton.setOnClickListener {
            if(context == null) return@setOnClickListener

            val action = HabitViewFragmentDirections.actionFromHabitViewFragmentToHabitFormFragment(args.habitId)
            findNavController().navigate(action)
        }

        //Delete button on Activity ToolBar
        activity.deleteButton.setOnClickListener {
            if(context == null) return@setOnClickListener

            showDeleteConfirmationDialog()
        }
    }

    private fun updateProgress(showLayout : Boolean) {
        progress.visibility = if(showLayout) View.GONE else View.VISIBLE
        habitInfoLayout.visibility = if(showLayout) View.VISIBLE else View.GONE
    }

    private fun updateHabitValues(habit: Habit) {
        habitNameText.text = habit.name

        //Priority text
        val priorityText = habitViewModel.habitInfoManager.getCurrentPriorityLevelText(habit.priority)
        habitPriorityText.text = getString(R.string.habit_priority_header, priorityText)

        //WeekDaysText
        val recurrenceText = habitViewModel.getRecurrenceText(requireContext(), habit)
        habitRecurrenceText.text = recurrenceText

        //Icon
        val iconKey = habit.iconKey
        if (iconKey != null)
            habitIcon.setImageDrawable(habitViewModel.iconManager.getIconByKey(iconKey))

        updateProgress(true)
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.delete_habit))
        alertDialog.setMessage(getString(R.string.habit_delete_confirmation))
        alertDialog.setPositiveButton(getString(R.string.delete), object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                habitViewModel.deleteHabit(requireContext())
            }
        })

        alertDialog.setNegativeButton(getString(R.string.cancel), null)

        val dialog = alertDialog.create()
        dialog.show()
    }
}