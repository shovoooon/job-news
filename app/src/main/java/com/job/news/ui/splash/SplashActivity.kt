package com.job.news.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.job.news.MainActivity
import com.job.news.R
import com.job.news.ui.intro.IntroActivity
import com.job.news.utils.getBooleanData

class SplashActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startTimer()
    }

    override fun onStart() {
        super.onStart()
        if (!getBooleanData("intro")){
            startActivity(Intent(this, IntroActivity::class.java))
            return
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(2000, 1000){
            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

            override fun onTick(millisUntilFinished: Long) {}

        }.start()
    }
}