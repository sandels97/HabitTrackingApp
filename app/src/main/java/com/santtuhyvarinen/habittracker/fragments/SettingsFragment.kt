package com.santtuhyvarinen.habittracker.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.utils.SettingsUtil
import com.santtuhyvarinen.habittracker.viewmodels.HabitsViewModel
import com.santtuhyvarinen.habittracker.viewmodels.SettingsViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var settingsViewModel : SettingsViewModel
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        val notificationPreference = findPreference<SwitchPreferenceCompat>(requireContext().getString(R.string.setting_notification_enable_key))
        notificationPreference?.setOnPreferenceChangeListener { _, newValue ->
            val isNotificationsEnabled = newValue as Boolean
            if(isNotificationsEnabled) {
                SettingsUtil.startNotificationService(requireContext())
            } else {
                SettingsUtil.stopNotificationService(requireContext())
            }

            true
        }


        val deleteDataPreference = findPreference<Preference>(requireContext().getString(R.string.setting_delete_all_habits_key))
        deleteDataPreference?.setOnPreferenceClickListener {
            showDeleteDataConfirmationDialog()
            true
        }
    }

    private fun showDeleteDataConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(R.string.setting_delete_all_habits_title)
        dialog.setMessage(R.string.setting_delete_all_habits_confirmation)
        dialog.setPositiveButton(R.string.delete) { _, _ ->
            settingsViewModel.deleteAllHabits()
            Toast.makeText(requireContext(), getString(R.string.setting_delete_all_habits_done), Toast.LENGTH_LONG).show()
        }
        dialog.setNegativeButton(R.string.cancel, null)

        dialog.show()
    }
}