package com.hackaton.wikitrainer.core.audio

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * SoundFeedbackManager:
 * Provides instant audio-tactile feedback for gamified Duolingo interactions.
 * Uses hardware-native ToneGenerator and VibrationEffect, guaranteeing zero audio asset latency
 * and 100% offline availability across all Android devices.
 */
class SoundFeedbackManager(private val context: Context) {

    private var isSoundEnabled: Boolean = true
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val toneGenerator: ToneGenerator? by lazy {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (e: Exception) {
            null
        }
    }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
    }

    fun isSoundEnabled(): Boolean = isSoundEnabled

    /**
     * Plays cheerful ascending chime on correct answer + tactile click haptic.
     */
    fun playCorrectFeedback() {
        // Haptic feedback
        performHaptic(isError = false)

        if (!isSoundEnabled) return

        coroutineScope.launch {
            try {
                // Cheerful ascending double ding
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
                delay(130)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 180)
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays dull alert/buzzer on wrong answer + warning double-pulse haptic.
     */
    fun playIncorrectFeedback() {
        performHaptic(isError = true)

        if (!isSoundEnabled) return

        coroutineScope.launch {
            try {
                // Low thud / error buzzer
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 260)
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays triumphant fanfare on lesson completion.
     */
    fun playLessonCompleteFeedback() {
        performHaptic(isError = false)

        if (!isSoundEnabled) return

        coroutineScope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 300)
            } catch (_: Exception) {}
        }
    }

    private fun performHaptic(isError: Boolean) {
        try {
            vibrator?.let { vib ->
                if (vib.hasVibrator()) {
                    if (isError) {
                        // Double pulse for error
                        val timings = longArrayOf(0, 60, 80, 100)
                        val amplitudes = intArrayOf(0, 180, 0, 220)
                        val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                        vib.vibrate(effect)
                    } else {
                        // Single pleasant tick for success
                        val effect = VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
                        vib.vibrate(effect)
                    }
                }
            }
        } catch (_: Exception) {}
    }
}
