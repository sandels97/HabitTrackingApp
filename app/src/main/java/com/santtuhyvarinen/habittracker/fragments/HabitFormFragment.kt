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
    }
}