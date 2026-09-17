package com.secretmafia.ui

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object Feedback {
    fun tick(context: Context, vibrate: Boolean = true) {
        if (vibrate) pulse(context, 30)
    }

    fun unlock(context: Context, vibrate: Boolean = true) {
        if (vibrate) pulse(context, 60)
    }

    fun death(context: Context, vibrate: Boolean = true) {
        if (vibrate) pulse(context, 180)
    }

    fun alarm(context: Context, sound: Boolean, vibrate: Boolean) {
        if (vibrate) pulse(context, 400)
        if (sound) {
            runCatching {
                val tone = ToneGenerator(AudioManager.STREAM_ALARM, 80)
                tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 700)
            }
        }
    }

    private fun pulse(context: Context, ms: Long) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(VibratorManager::class.java)
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Vibrator::class.java)
        }
        vibrator?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
