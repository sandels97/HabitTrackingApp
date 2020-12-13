package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.santtuhyvarinen.habittracker.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}