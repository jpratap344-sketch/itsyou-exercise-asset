package com.example.ui.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import kotlin.concurrent.thread
import kotlin.math.*

enum class SoundCategory {
    EXERCISE,
    NOTIFICATION,
    TIMER,
    UI
}

object PremiumSoundManager {

    private const val SAMPLE_RATE = 22050

    private fun playProceduralSound(
        context: Context,
        durationMs: Int,
        vibePattern: LongArray? = null,
        category: SoundCategory,
        generateSamples: (DoubleArray) -> Unit
    ) {
        val prefs = context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
        val soundEnabled = prefs.getBoolean("sound_enabled", true)
        val notificationEnabled = prefs.getBoolean("notification_sound_enabled", true)
        val vibrationEnabled = prefs.getBoolean("vibration_enabled", true)

        // Validate appropriate category constraints
        if (category == SoundCategory.NOTIFICATION || category == SoundCategory.TIMER) {
            if (!notificationEnabled) return
        } else {
            if (!soundEnabled) return
        }

        // Trigger safe vibration haptics in background
        if (vibrationEnabled && vibePattern != null) {
            try {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (vibePattern.size == 1) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    vibePattern[0],
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(VibrationEffect.createWaveform(vibePattern, -1))
                        }
                    } else {
                        vibrator.vibrate(vibePattern, -1)
                    }
                }
            } catch (e: Exception) {
                Log.e("PremiumSoundManager", "Haptic vibration failed", e)
            }
        }

        // Synthesize and play on a decoupled background thread
        thread {
            try {
                val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt()
                if (numSamples <= 0) return@thread
                val samples = DoubleArray(numSamples)
                generateSamples(samples)

                // Transcode double arrays into 16-bit Mono short buffers
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val sampleValue = samples[i] * 32767.0
                    buffer[i] = max(-32768.0, min(32767.0, sampleValue)).toInt().toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()

                // Yield wait and stop dynamically
                Thread.sleep(durationMs.toLong() + 50)
                try {
                    audioTrack.stop()
                } catch (ignored: Exception) {}
                audioTrack.release()
            } catch (e: Exception) {
                Log.e("PremiumSoundManager", "Error playing procedural sound", e)
            }
        }
    }

    /**
     * App launch: Welcome tone (completely silent / tiny soft bip to avoid thunder)
     */
    fun playSoftThunderRumble(context: Context) {
        // We make this silent as the user explicitly asked to "Do not add thunder sounds."
    }

    /**
     * Workout starting sound: Standard start chime (clean high beep tone instead of thunder/lightning)
     */
    fun playLightningStrike(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 600,
            vibePattern = longArrayOf(0, 100),
            category = SoundCategory.UI
        ) { samples ->
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                // 880Hz (A5) clear sound with exponential decay
                samples[i] = sin(2 * PI * 880.0 * t) * exp(-8.0 * t) * 0.35
            }
        }
    }

    /**
     * Rest timer complete: standard double chime bee-beep
     */
    fun playRestTimerComplete(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 600,
            vibePattern = longArrayOf(0, 80, 80, 80),
            category = SoundCategory.TIMER
        ) { samples ->
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                if (t < 0.25) {
                    samples[i] = sin(2 * PI * 1000.0 * t) * 0.4
                } else if (t in 0.3..0.55) {
                    val t2 = t - 0.3
                    samples[i] = sin(2 * PI * 1000.0 * t2) * 0.4
                }
            }
        }
    }

    /**
     * Workout complete: Triumphant arpeggio
     */
    fun playWorkoutComplete(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 1200,
            vibePattern = longArrayOf(0, 150, 100, 150),
            category = SoundCategory.NOTIFICATION
        ) { samples ->
            val freqs = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C5 - E5 - G5 - C6
            val stepSize = 0.25
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                val step = (t / stepSize).toInt()
                if (step < freqs.size) {
                    val freq = freqs[step]
                    val tLocal = t - (step * stepSize)
                    samples[i] = sin(2 * PI * freq * tLocal) * exp(-4.0 * tLocal) * 0.35
                }
            }
        }
    }

    /**
     * New personal record: Bright double chime
     */
    fun playNewPersonalRecord(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 800,
            vibePattern = longArrayOf(0, 120, 80, 120),
            category = SoundCategory.NOTIFICATION
        ) { samples ->
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                if (t < 0.3) {
                    samples[i] = sin(2 * PI * 1100.0 * t) * exp(-10.0 * t) * 0.35
                } else if (t >= 0.3) {
                    val t2 = t - 0.3
                    samples[i] = sin(2 * PI * 1320.0 * t2) * exp(-8.0 * t2) * 0.35
                }
            }
        }
    }

    /**
     * Water goal completed: standard pleasant bubble click
     */
    fun playWaterGoalCompleted(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 500,
            vibePattern = longArrayOf(80),
            category = SoundCategory.UI
        ) { samples ->
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                val freq = 440.0 + 330 * (t / 0.5)
                samples[i] = sin(2 * PI * freq * t) * exp(-12.0 * t) * 0.4
            }
        }
    }

    /**
     * Contextual barbell/dumbbell click pop (Standard click sounds instead of custom heavy metallic fx)
     */
    fun playBarbellMetalSound(context: Context) = playStandardClick(context)
    fun playDumbbellPickupSound(context: Context) = playStandardClick(context)
    fun playCableMachineSound(context: Context) = playStandardClick(context)
    fun playMachineClickSound(context: Context) = playStandardClick(context)
    fun playCardioEquipmentSound(context: Context) = playStandardClick(context)

    /**
     * Standard button click sound: clean UI pop/tick
     */
    fun playStandardClick(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 80,
            vibePattern = longArrayOf(25),
            category = SoundCategory.UI
        ) { samples ->
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                samples[i] = sin(2 * PI * 1400.0 * t) * exp(-35.0 * t) * 0.3
            }
        }
    }

    /**
     * Route notification sounds to standard chime
     */
    fun playNotificationSound(context: Context) {
        playProceduralSound(
            context = context,
            durationMs = 400,
            vibePattern = longArrayOf(80),
            category = SoundCategory.NOTIFICATION
        ) { samples ->
            for (i in samples.indices) {
                val t = i.toDouble() / SAMPLE_RATE
                samples[i] = (sin(2 * PI * 880.0 * t) + sin(2 * PI * 1100.0 * t)) * 0.2 * exp(-12.0 * t)
            }
        }
    }

    /**
     * Exercise Category opener: plays a soft, pleasant click
     */
    fun playExerciseCategorySound(context: Context, equipment: String, name: String) {
        playStandardClick(context)
    }
}
