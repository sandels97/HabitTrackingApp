package com.santtuhyvarinen.habittracker.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.databinding.ActivityMainBinding
import com.santtuhyvarinen.habittracker.utils.SettingsUtil

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        //Set up bottom navigation bar and toolbar with NavController
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.tasksFragment,
            R.id.habitsFragment,
            R.id.statisticsFragment)
        )

        binding.toolbarLayout.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when(destination.id) {
                R.id.habitFormFragment, R.id.settingsFragment, R.id.habitViewFragment, R.id.taskManagementFragment -> {
                    hideNavigationElements(true)
                }
                else -> {
                    hideNavigationElements(false)
                }
            }

            hideHabitViewButtons(destination.id != R.id.habitViewFragment)
        }

        //Toolbar buttons
        binding.toolbarLayout.settingsButton.setOnClickListener {
            navController.navigate(R.id.action_to_settingsFragment)
        }
        binding.toolbarLayout.addHabitButton.setOnClickListener {
            navController.navigate(R.id.action_to_habitFormFragment)
        }

        SettingsUtil.startNotificationServiceIfEnabled(this)
    }

    private fun hideNavigationElements(hidden : Boolean) {
        binding.bottomNavigation.visibility = if(hidden) View.GONE else View.VISIBLE
        binding.toolbarLayout.settingsButton.visibility = if(hidden) View.GONE else View.VISIBLE
        binding.toolbarLayout.addHabitButton.visibility = if(hidden) View.GONE else View.VISIBLE
        //toolbarTitle.visibility = if(hidden) View.GONE else View.VISIBLE
    }

    private fun hideHabitViewButtons(hidden : Boolean) {
        binding.toolbarLayout.editButton.visibility = if(hidden) View.GONE else View.VISIBLE
        binding.toolbarLayout.deleteButton.visibility = if(hidden) View.GONE else View.VISIBLE
    }

    fun getEditButton() : ImageButton {
        return binding.toolbarLayout.editButton
    }

    fun getDeleteButton() : ImageButton {
        return binding.toolbarLayout.deleteButton
    }
}