package com.santtuhyvarinen.habittracker.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class SettingsUtil {
    companion object {
        private const val DEFAULT_VIBRATE_LENGTH = 100L
        
        fun vibrateDevice(context: Context) {
            val vibrateService = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrateService.vibrate(VibrationEffect.createOneShot(DEFAULT_VIBRATE_LENGTH, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrateService.vibrate(DEFAULT_VIBRATE_LENGTH);
            }
        }
    }
}