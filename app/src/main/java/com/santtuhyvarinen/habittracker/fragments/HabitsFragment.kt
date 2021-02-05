package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.adapters.HabitsListAdapter
import com.santtuhyvarinen.habittracker.databinding.FragmentHabitsBinding
import com.santtuhyvarinen.habittracker.models.Habit
import com.santtuhyvarinen.habittracker.viewmodels.HabitsViewModel


class HabitsFragment : Fragment() {

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var habitsAdapter: HabitsListAdapter
    private lateinit var habitsViewModel : HabitsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitsViewModel = ViewModelProvider(this).get(HabitsViewModel::class.java)

        //Observer habits from Database
        val habitsObserver = Observer<List<Habit>> { list ->
            habitsAdapter.data = list
            habitsAdapter.sortData()
            habitsAdapter.notifyDataSetChanged()

            binding.progress.hide()

            updateMessageVisibility(list.isEmpty())
        }
        habitsViewModel.getHabits().observe(viewLifecycleOwner, habitsObserver)

        //Set up HabitsListAdapter and RecyclerView
        habitsAdapter = HabitsListAdapter(requireContext(), habitsViewModel.iconManager)
        binding.recyclerView.adapter = habitsAdapter

        val dividerItemDecoration = DividerItemDecoration(
                binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
        )

        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        habitsAdapter.habitClickedListener = object : HabitsListAdapter.HabitClickedListener {
            override fun habitClicked(habit: Habit) {
                openHabitView(habit)
            }
        }
    }

    private fun openHabitView(habit: Habit) {
        val action = HabitsFragmentDirections.actionFromHabitsFragmentToHabitViewFragment(habit.id)
        findNavController().navigate(action)
    }

    private fun updateMessageVisibility(visible : Boolean) {
        val previousLayoutMessageVisibility = binding.layoutMessage.messageContainer.visibility
        binding.layoutMessage.messageContainer.visibility = if(visible) View.VISIBLE else View.GONE

        if(previousLayoutMessageVisibility == View.GONE && visible) {
            val alphaAnimation = AlphaAnimation(0f, 1f)
            alphaAnimation.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
            binding.layoutMessage.messageContainer.startAnimation(alphaAnimation)
        }

        //Set message content
        binding.layoutMessage.messageText.text = getString(R.string.no_habits_message)
        binding.layoutMessage.messageIcon.setImageDrawable(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}