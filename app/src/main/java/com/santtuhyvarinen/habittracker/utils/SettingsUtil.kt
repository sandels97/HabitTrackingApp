package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.preference.PreferenceManager
import com.santtuhyvarinen.habittracker.R
import com.santtuhyvarinen.habittracker.services.NotificationService
import java.util.*

class SettingsUtil {
    companion object {
        private const val DEFAULT_VIBRATE_LENGTH = 100L

        const val TASK_STAT_NONE = "none"
        const val TASK_STAT_STREAK = "score"
        const val TASK_STAT_TOTAL = "total"

        const val TASK_SORT_PRIORITY = "priority"
        const val TASK_SORT_ALPHABET = "alphabet"
        const val TASK_SORT_SCORE = "score"

        fun sendTouchFeedback(context: Context) {
            if(!isTouchFeedbackEnabled(context)) return

            val vibrateService = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrateService.vibrate(VibrationEffect.createOneShot(DEFAULT_VIBRATE_LENGTH, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrateService.vibrate(DEFAULT_VIBRATE_LENGTH);
            }
        }

        fun startNotificationServiceIfEnabled(context: Context) {
            if(!isNotificationServiceEnabled(context)) return

            startNotificationService(context)
        }

        fun startNotificationService(context: Context) {
            val intent = Intent(context, NotificationService::class.java)

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopNotificationService(context: Context) {
            val intent = Intent(context, NotificationService::class.java)
            context.stopService(intent)
        }

        fun isNotificationServiceEnabled(context: Context) : Boolean {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getBoolean(context.getString(R.string.setting_notification_enable_key), true)
        }

        fun isTouchFeedbackEnabled(context: Context) : Boolean {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getBoolean(context.getString(R.string.setting_touch_feedback_key), true)
        }

        fun getDisplayTaskStatValue(context: Context) : String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(context.getString(R.string.setting_task_stat_key), TASK_STAT_STREAK)!!
        }

        fun getSortTasksValue(context: Context) : String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(context.getString(R.string.setting_task_sort_key), TASK_SORT_PRIORITY)!!
        }

        fun getLocale(context: Context) : Locale {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]
            } else {
                context.resources.configuration.locale
            }
        }
    }
}