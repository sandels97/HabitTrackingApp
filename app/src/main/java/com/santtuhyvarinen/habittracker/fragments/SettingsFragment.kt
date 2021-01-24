package com.santtuhyvarinen.habittracker.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.utils.SettingsUtil

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val notificationPreference = findPreference<SwitchPreference>(requireContext().getString(R.string.setting_notification_enable_key))
        notificationPreference?.setOnPreferenceChangeListener { _, newValue ->
            val isNotificationsEnabled = newValue as Boolean
            if(isNotificationsEnabled) {
                SettingsUtil.startNotificationService(requireContext())
            } else {
                SettingsUtil.stopNotificationService(requireContext())
            }

            true
        }

    }
}