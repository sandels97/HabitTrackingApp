package com.santtuhyvarinen.habittracker.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.santtuhyvarinen.habittracker.R
import kotlinx.android.synthetic.main.activity_main.*

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
            R.id.habitsFragment)
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.habitFormFragment -> hideNavigationElements(true)
                else -> hideNavigationElements(false)
            }
        }
    }

    private fun hideNavigationElements(hidden : Boolean) {
        bottomNavigation.visibility = if(hidden) View.GONE else View.VISIBLE
        toolbarTitle.visibility = if(hidden) View.GONE else View.VISIBLE
    }
}