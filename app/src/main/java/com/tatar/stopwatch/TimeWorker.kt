package com.tatar.stopwatch

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log

class TimeWorker : HandlerThread(TAG) {

    private var secondsCount = 0
    private var shouldRun = false

    private var timeWorkerHandler: Handler
    private lateinit var uiHandler: Handler

    private lateinit var runnable: Runnable

    init {
        start()
        timeWorkerHandler = Handler(looper) // TODO apply DI
    }

    fun setUiHandlerHandler(uiHandler: Handler) {
        this.uiHandler = uiHandler
    }

    fun startCounting() {
        shouldRun = true
        prepareRunnable()
        addRunnableToTimeWorkerHandlerQueue()
    }

    fun stopCounting() {
        shouldRun = false
    }

    private fun prepareRunnable() {
        runnable = Runnable {
            if (shouldRun) {
                increaseSecondsByOne()
                sendSecondsMessage()
                addRunnableToTimeWorkerHandlerQueue()
            }
        }
    }

    private fun increaseSecondsByOne() {
        secondsCount++
        Log.i(TAG, "Seconds is at : $secondsCount")
    }

    private fun sendSecondsMessage() {
        val secondsMessage = Message.obtain()
        secondsMessage.obj = secondsCount
        uiHandler.sendMessage(secondsMessage)
    }

    private fun addRunnableToTimeWorkerHandlerQueue() {
        timeWorkerHandler.postDelayed(runnable, 1000)
    }

    companion object {
        private const val TAG = "TimeWorker"
    }
}
