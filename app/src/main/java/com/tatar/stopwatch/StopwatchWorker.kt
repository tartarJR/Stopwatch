package com.tatar.stopwatch

import android.os.Handler
import android.os.HandlerThread
import android.os.Message

// TODO refactor method/variable names
// TODO divide methods if possible
class StopwatchWorker : HandlerThread(TAG) {

    private var milliSeconds = 0L

    // Using two different handlers to experience communication between different threads and handlers
    // A more efficient way might be possible
    private var stopwatchWorkerHandler: Handler
    private lateinit var uiHandler: Handler

    private lateinit var stopwatchWorkerRunnable: Runnable

    init {
        start()
        stopwatchWorkerHandler = Handler(looper) // TODO apply DI
    }

    fun setUiHandler(uiHandler: Handler) {
        this.uiHandler = uiHandler
    }

    fun startCounting() {
        prepareRunnable()
        startCountingLoop() // to start the loop for the first time
    }

    private fun prepareRunnable() {
        stopwatchWorkerRunnable = Runnable {
            increaseCentiSecondsByTen()
            sendCurrentTimeMessageMessage()
            startCountingLoop() // to keep the loop running
        }
    }

    private fun sendCurrentTimeMessageMessage() {
        val currentTimeMessage = Message.obtain()
        currentTimeMessage.obj = getFormattedTimeString()
        uiHandler.sendMessage(currentTimeMessage)
    }

    private fun increaseCentiSecondsByTen() {
        milliSeconds += CENTI_SECOND_IN_MILLIS
    }

    private fun startCountingLoop() {
        stopwatchWorkerHandler.postDelayed(stopwatchWorkerRunnable, CENTI_SECOND_IN_MILLIS)
    }

    private fun getFormattedTimeString(): String {
        val centiSecsDisplay = (milliSeconds / CENTI_SECOND_IN_MILLIS) % SECOND_IN_CENTI_SECONDS
        val secsDisplay = (milliSeconds / SECOND_IN_MILLIS) % MINUTE_IN_SECONDS
        val minsDisplay = (milliSeconds / (MINUTE_IN_SECONDS * SECOND_IN_MILLIS)) % HOUR_IN_MINUTES
        val hoursDisplay = milliSeconds / (HOUR_IN_MINUTES * MINUTE_IN_SECONDS * SECOND_IN_MILLIS)

        return String.format(
            "%02d:%02d:%02d:%02d",
            hoursDisplay,
            minsDisplay,
            secsDisplay,
            centiSecsDisplay
        )
    }

    fun stopCounting() {
        stopwatchWorkerHandler.removeCallbacks(stopwatchWorkerRunnable)
    }

    fun resetCounting() {
        stopwatchWorkerHandler.removeCallbacks(stopwatchWorkerRunnable)
        milliSeconds = 0L
        sendCurrentTimeMessageMessage()
    }

    companion object {
        private const val TAG = "StopwatchWorker"

        private const val CENTI_SECOND_IN_MILLIS = 10L
        private const val SECOND_IN_MILLIS = 1000L
        private const val SECOND_IN_CENTI_SECONDS = 100L
        private const val MINUTE_IN_SECONDS = 60L
        private const val HOUR_IN_MINUTES = 60L
    }
}
