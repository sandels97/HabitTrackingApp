package com.santtuhyvarinen.habittracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.santtuhyvarinen.habittracker.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun setFragment() {
        supportFragmentManager.beginTransaction().commit()
    }
}