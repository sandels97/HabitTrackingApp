package com.santtuhyvarinen.habittracker.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.santtuhyvarinen.habittracker.utils.SettingsUtil

class DeviceBootUpReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if(intent == null) return

        if(intent.action == Intent.ACTION_BOOT_COMPLETED) SettingsUtil.startNotificationServiceIfEnabled(context)
    }
}