package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.os.Build
import com.example.R
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.sin

/**
 * Provides gentle, pleasant auditory and tactile feedback
 * using calm, harmonious acoustic chimes and responsive haptics.
 */
object AppFeedbackHelper {
    private const val TAG = "AppFeedbackHelper"
    private var lastClickTime = 0L
    private const val CLICK_THROTTLE_MS = 120L

    /**
     * Plays a very gentle, subtle, warm click for standard taps and buttons.
     */
    fun triggerClick(context: Context) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime < CLICK_THROTTLE_MS) return
        lastClickTime = now

        playCalmChime(freqHz = 660, durationMs = 45, volume = 0.16f)
        vibrate(context, durationMs = 18, amplitude = 95)
    }

    /**
     * Gentle, soft pleasant chime for chip selection, dates, and tabs.
     */
    fun triggerSelection(context: Context) {
        val now = System.currentTimeMillis()
        if (now - lastClickTime < CLICK_THROTTLE_MS) return
        lastClickTime = now

        playCalmChime(freqHz = 784, durationMs = 50, volume = 0.18f)
        vibrate(context, durationMs = 22, amplitude = 110)
    }

    /**
     * Plays a calm, short, and non-intrusive sound effect using Android's MediaPlayer API
     * triggered by successful app actions like booking confirmation, login, or registration.
     */
    fun playSuccessSoundWithMediaPlayer(context: Context) {
        try {
            val mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.calm_success)
            if (mediaPlayer != null) {
                mediaPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                mediaPlayer.setVolume(0.70f, 0.70f)
                mediaPlayer.setOnCompletionListener { mp ->
                    try {
                        mp.reset()
                        mp.release()
                    } catch (e: Exception) {
                        Log.w(TAG, "MediaPlayer completion release notice: ${e.localizedMessage}")
                    }
                }
                mediaPlayer.setOnErrorListener { mp, _, _ ->
                    try {
                        mp.reset()
                        mp.release()
                    } catch (e: Exception) {
                        Log.w(TAG, "MediaPlayer error release notice: ${e.localizedMessage}")
                    }
                    true
                }
                mediaPlayer.start()
            } else {
                playMelodicChime(frequencies = intArrayOf(587, 880), noteDurationMs = 75, volume = 0.22f)
            }
        } catch (e: Exception) {
            Log.w(TAG, "MediaPlayer API fallback notice: ${e.localizedMessage}")
            playMelodicChime(frequencies = intArrayOf(587, 880), noteDurationMs = 75, volume = 0.22f)
        }
    }

    /**
     * Calm, soothing success chime and pleasant haptic confirmation using the MediaPlayer API.
     */
    fun triggerSuccess(context: Context) {
        playSuccessSoundWithMediaPlayer(context)
        vibrateSuccess(context)
    }

    /**
     * Soft alert vibration and calm low-frequency tone for errors/warnings.
     */
    fun triggerError(context: Context) {
        playCalmChime(freqHz = 330, durationMs = 110, volume = 0.20f)
        vibrateDouble(context)
    }

    /**
     * Calming luxury bell chime for notifications and appointment alerts.
     */
    fun triggerNotification(context: Context) {
        playMelodicChime(frequencies = intArrayOf(659, 880, 1046), noteDurationMs = 80, volume = 0.25f)
        vibrate(context, durationMs = 60, amplitude = 140)
    }

    /**
     * Generates a soft, pure sinusoidal sound wave with smooth exponential fade-out
     * ensuring the tone is warm, soothing, and completely free of harsh clicks.
     */
    private fun playCalmChime(freqHz: Int, durationMs: Int, volume: Float) {
        thread(name = "AppCalmAudioPlayer", isDaemon = true) {
            try {
                val sampleRate = 22050
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(100)
                val buffer = ShortArray(numSamples)

                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val rawSin = sin(2.0 * PI * freqHz * time)
                    // Cosine attack and exponential decay envelope to avoid speaker clicks
                    val progress = i.toDouble() / numSamples
                    val envelope = (1.0 - progress) * (1.0 - progress)
                    val sample = (rawSin * envelope * volume * Short.MAX_VALUE).toInt()
                    buffer[i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
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
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                Thread.sleep(durationMs.toLong() + 20)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                Log.w(TAG, "Calm chime synthesis error: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Generates a gentle multi-tone sequence (like a singing bowl or luxury hotel doorbell).
     */
    private fun playMelodicChime(frequencies: IntArray, noteDurationMs: Int, volume: Float) {
        thread(name = "AppMelodicAudioPlayer", isDaemon = true) {
            try {
                val sampleRate = 22050
                val totalSamplesPerNote = (sampleRate * (noteDurationMs / 1000.0)).toInt()
                val totalSamples = totalSamplesPerNote * frequencies.size
                val buffer = ShortArray(totalSamples)

                var offset = 0
                for (freq in frequencies) {
                    for (i in 0 until totalSamplesPerNote) {
                        val time = i.toDouble() / sampleRate
                        val rawSin = sin(2.0 * PI * freq * time)
                        val progress = i.toDouble() / totalSamplesPerNote
                        val envelope = (1.0 - progress) * (1.0 - progress)
                        val sample = (rawSin * envelope * volume * Short.MAX_VALUE).toInt()
                        buffer[offset + i] = sample.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                    }
                    offset += totalSamplesPerNote
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
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                Thread.sleep((noteDurationMs * frequencies.size).toLong() + 30)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                Log.w(TAG, "Melodic chime synthesis error: ${e.localizedMessage}")
            }
        }
    }

    private fun vibrate(context: Context, durationMs: Long, amplitude: Int = 100) {
        try {
            val vibrator = getVibrator(context) ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(durationMs, amplitude.coerceIn(1, 255)))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMs)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Vibration feedback error: ${e.localizedMessage}")
        }
    }

    private fun vibrateSuccess(context: Context) {
        try {
            val vibrator = getVibrator(context) ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 25, 45, 30)
                val amplitudes = intArrayOf(0, 90, 0, 120)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 25, 45, 30), -1)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Success vibration error: ${e.localizedMessage}")
        }
    }

    private fun vibrateDouble(context: Context) {
        try {
            val vibrator = getVibrator(context) ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 35, 50, 45)
                val amplitudes = intArrayOf(0, 110, 0, 130)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 35, 50, 45), -1)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Double vibration error: ${e.localizedMessage}")
        }
    }

    private fun getVibrator(context: Context): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            null
        }
    }
}
