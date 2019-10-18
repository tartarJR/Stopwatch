package com.tatar.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tatar.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var hasStarted = false // TODO use ViewModel and DataBinding

    private lateinit var binding: ActivityMainBinding

    private lateinit var stopwatchWorker: StopwatchWorker

    private val uiHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            binding.currentTime = msg.obj.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prepareBindings()
        prepareTimeWorker()
        prepareViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopwatchWorker.quit()
    }

    private fun prepareBindings() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.currentTime = "00:00:00:00" // TODO use ViewModel
    }

    private fun prepareTimeWorker() {
        stopwatchWorker = StopwatchWorker()
        stopwatchWorker.setUiHandler(uiHandler)
    }

    private fun prepareViews() {
        binding.startStopBtn.setOnClickListener setOnClickListener@{
            if (hasStarted) {
                stopStopwatch()
                return@setOnClickListener
            }

            startStopwatch()
        }

        binding.resetBtn.setOnClickListener {
            stopwatchWorker.resetCounting()
        }
    }

    private fun stopStopwatch() {
        stopwatchWorker.stopCounting()
        binding.startStopBtn.text = getString(R.string.start_stop_btn_start_txt)
        binding.resetBtn.isEnabled = true
        hasStarted = false
    }

    private fun startStopwatch() {
        stopwatchWorker.startCounting()
        binding.startStopBtn.text = getString(R.string.start_stop_btn_stop_txt)
        binding.resetBtn.isEnabled = false
        hasStarted = true
    }
}
