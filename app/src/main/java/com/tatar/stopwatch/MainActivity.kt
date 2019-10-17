package com.tatar.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tatar.stopwatch.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var hasStarted = false

    private lateinit var binding: ActivityMainBinding

    private lateinit var timeWorker: TimeWorker

    private val uiHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            binding.timeTextView.text = msg.obj.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        prepareTimeWorker()
        prepareViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        timeWorker.quit()
    }

    private fun prepareTimeWorker() {
        timeWorker = TimeWorker()
        timeWorker.setUiHandlerHandler(uiHandler)
    }

    private fun prepareViews() {
        binding.startStopBtn.setOnClickListener setOnClickListener@{
            if (hasStarted) {
                timeWorker.stopCounting()
                binding.startStopBtn.text = getString(R.string.start_stop_btn_start_txt)
                binding.resetBtn.isEnabled = true
                hasStarted = false

                return@setOnClickListener
            }

            timeWorker.startCounting()
            binding.startStopBtn.text = getString(R.string.start_stop_btn_stop_txt)
            binding.resetBtn.isEnabled = false
            hasStarted = true
        }

        binding.resetBtn.setOnClickListener {

        }
    }
}
