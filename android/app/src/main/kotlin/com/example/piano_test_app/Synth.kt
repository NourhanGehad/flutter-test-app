package com.example.piano

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack

/** A synthesizer that plays sin waves for Android.  */
class Synth : Runnable {
    private lateinit var mThread: Thread
    private var mRunning = false
    private var mFreq = 440.0
    private var mAmp = 0.0
    private var mNumKeysDown = 0


    fun start() {
        mThread = Thread(this)
        mRunning = true
        mThread.start()
    }

    fun stop() {
        mRunning = false
    }

    fun keyDown(key: Int): Int {
        mFreq = Math.pow(1.0594630f.toDouble(), key.toDouble() - 69.0) * 440.0
        mAmp = 1.0
        mNumKeysDown += 1
        return mNumKeysDown
    }

    fun keyUp(key: Int): Int {
        mAmp = 0.0
        mNumKeysDown -= 1
        return mNumKeysDown
    }

    override fun run() {
        val sampleRate = 44100
        val bufferSize = 1024
        val buffer = ShortArray(bufferSize)
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )
        val fSampleRate = sampleRate.toDouble()
        val pi2: Double = 2.0 * Math.PI
        var counter = 0.0
        audioTrack.play()
        while (mRunning) {
            val tau = pi2 * mFreq / fSampleRate
            val maxValue = Short.MAX_VALUE * mAmp
            for (i in 0 until bufferSize) {
                buffer[i] = (Math.sin(tau * counter) * maxValue).toInt().toShort()
                counter += 1.0
            }
            audioTrack.write(buffer, 0, bufferSize)
        }
        audioTrack.stop()
        audioTrack.release()
    }
}


// This class calls Android Media API: android.media
// fun start()
// Initiate a new thread that runs the procedures defined in run().
// fun keyDown()
// Set the audio amplitude to 1.0
// Calculate the frequency of the Sine wave corresponding to the key note.
// (Optional: Read how the pitch of a sound has to do with Sine wave article.)
// fun keyUp()
// Set the audio amplitude to 0.0
// override fun run()
// It generates the Sine wave values into a buffer array.
// Write the buffer values to audioTrack to play the sound.
// Stop playing and release the memory.
// Note that this thread is constantly running, and keeps generating values to the buffer array. However, when keyUp, the amplitude is set to 0.0, and thus the written value is 0, so no sound would be heard.

