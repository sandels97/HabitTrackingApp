package com.santtuhyvarinen.habittracker.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.fragments.HabitFormFragmentArgs
import com.santtuhyvarinen.habittracker.fragments.HabitViewFragmentDirections
import com.santtuhyvarinen.habittracker.fragments.HabitsFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        //Set up bottom navigation bar and toolbar with NavController
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.tasksFragment,
            R.id.habitsFragment,
            R.id.statisticsFragment)
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id) {
                R.id.habitFormFragment, R.id.settingsFragment, R.id.habitViewFragment -> {
                    hideNavigationElements(true)
                }
                else -> {
                    hideNavigationElements(false)
                }
            }

            hideHabitViewButtons(destination.id != R.id.habitViewFragment)
        }

        //Toolbar buttons
        settingsButton.setOnClickListener {
            navController.navigate(R.id.action_to_settingsFragment)
        }
        addHabitButton.setOnClickListener {
            navController.navigate(R.id.action_to_habitFormFragment)
        }
    }

    private fun hideNavigationElements(hidden : Boolean) {
        bottomNavigation?.visibility = if(hidden) View.GONE else View.VISIBLE
        settingsButton?.visibility = if(hidden) View.GONE else View.VISIBLE
        addHabitButton?.visibility = if(hidden) View.GONE else View.VISIBLE
        //toolbarTitle.visibility = if(hidden) View.GONE else View.VISIBLE
    }

    private fun hideHabitViewButtons(hidden : Boolean) {
        editButton?.visibility = if(hidden) View.GONE else View.VISIBLE
        deleteButton?.visibility = if(hidden) View.GONE else View.VISIBLE
    }

    fun getToolBarEditButton() : ImageButton {
        return editButton
    }

    fun getToolBarDeleteButton() : ImageButton {
        return deleteButton
    }
}