package com.exertia.wikingo.core.audio

import android.content.Context
import android.media.AudioManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
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
            ToneGenerator(AudioManager.STREAM_MUSIC, 100)
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
                // Mirrors the SuperCollider Pbind: two bell events spaced by dur = 0.1s.
                launch { playBellTone(midiNote = 72) }
                delay(BELL_EVENT_DURATION_MS)
                launch { playBellTone(midiNote = 76) }
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
                // Xylophone-like two-note error cue: MIDI 72 -> 66, dur = 0.08s.
                launch { playXylophoneTone(midiNote = 72) }
                delay(XYLOPHONE_EVENT_DURATION_MS)
                launch { playXylophoneTone(midiNote = 66) }
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

    private suspend fun playBellTone(midiNote: Int) {
        val sampleRate = 44_100
        val attackDurationMs = 25
        val releaseDurationMs = 1_000
        val sampleCount = sampleRate * (attackDurationMs + releaseDurationMs) / 1_000
        val samples = ShortArray(sampleCount)
        val fundamental = 440.0 * Math.pow(2.0, (midiNote - 69) / 12.0)
        val partialRatios = doubleArrayOf(1.0, 3.997, 9.469, 15.566, 20.863, 29.440)
        val partialAmplitudes = doubleArrayOf(1.0, 1.0 / 4.0, 1.0 / 9.0, 1.0 / 16.0, 1.0 / 25.0, 1.0 / 36.0)

        for (index in samples.indices) {
            val time = index.toDouble() / sampleRate
            val attack = (time / (attackDurationMs / 1_000.0)).coerceAtMost(1.0)
            val releaseProgress = ((time - attackDurationMs / 1_000.0) / (releaseDurationMs / 1_000.0))
                .coerceIn(0.0, 1.0)
            val envelope = if (releaseProgress == 0.0) {
                attack
            } else {
                attack * Math.exp(-6.0 * releaseProgress)
            }
            var value = 0.0

            for (partialIndex in partialRatios.indices) {
                value += partialAmplitudes[partialIndex] *
                    Math.sin(2.0 * Math.PI * fundamental * partialRatios[partialIndex] * time)
            }

            samples[index] = (value * envelope * Short.MAX_VALUE * CORRECT_FEEDBACK_GAIN)
                .toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .toShort()
        }

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(samples.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        try {
            audioTrack.write(samples, 0, samples.size)
            audioTrack.setVolume(AudioTrack.getMaxVolume())
            audioTrack.play()
            delay((attackDurationMs + releaseDurationMs).toLong())
        } finally {
            audioTrack.stop()
            audioTrack.release()
        }
    }

    private suspend fun playXylophoneTone(midiNote: Int) {
        val sampleRate = 44_100
        val attackDurationMs = 25
        val releaseDurationMs = 500
        val sampleCount = sampleRate * (attackDurationMs + releaseDurationMs) / 1_000
        val samples = ShortArray(sampleCount)
        val fundamental = 440.0 * Math.pow(2.0, (midiNote - 69) / 12.0)
        val partialRatios = doubleArrayOf(1.0, 3.932, 9.538, 16.688, 24.566, 31.147)
        val partialAmplitudes = doubleArrayOf(
            1.0,
            1.0 / 4.0,
            1.0 / 9.0,
            1.0 / 16.0,
            1.0 / 25.0,
            1.0 / 36.0
        )

        for (index in samples.indices) {
            val time = index.toDouble() / sampleRate
            val attack = (time / (attackDurationMs / 1_000.0)).coerceAtMost(1.0)
            val releaseProgress = ((time - attackDurationMs / 1_000.0) /
                (releaseDurationMs / 1_000.0)).coerceIn(0.0, 1.0)
            val envelope = if (releaseProgress == 0.0) {
                attack
            } else {
                attack * Math.exp(-6.0 * releaseProgress)
            }
            var value = 0.0

            for (partialIndex in partialRatios.indices) {
                value += partialAmplitudes[partialIndex] *
                    Math.sin(2.0 * Math.PI * fundamental * partialRatios[partialIndex] * time)
            }

            samples[index] = (value * envelope * Short.MAX_VALUE * INCORRECT_FEEDBACK_GAIN)
                .toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .toShort()
        }

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(samples.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        try {
            audioTrack.write(samples, 0, samples.size)
            audioTrack.setVolume(AudioTrack.getMaxVolume())
            audioTrack.play()
            delay((attackDurationMs + releaseDurationMs).toLong())
        } finally {
            audioTrack.stop()
            audioTrack.release()
        }
    }

    private companion object {
        const val BELL_EVENT_DURATION_MS = 100L
        const val XYLOPHONE_EVENT_DURATION_MS = 500L
        const val CORRECT_FEEDBACK_GAIN = 0.8
        const val INCORRECT_FEEDBACK_GAIN = 0.9
    }
}
