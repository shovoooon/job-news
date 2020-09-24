package com.job.jobnews.ui.splash

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View.INVISIBLE
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.job.jobnews.Constants
import com.job.jobnews.MainActivity
import com.job.jobnews.R
import com.job.jobnews.db.MyDatabase
import com.job.jobnews.ui.intro.IntroActivity
import com.job.jobnews.utils.getBooleanData
import com.job.jobnews.utils.getVersionName
import com.job.jobnews.utils.isOnline
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private lateinit var timer: CountDownTimer
    private val LOG = "SplashActivity"
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (!getBooleanData("intro")) {

            return
        }
        setContentView(R.layout.activity_splash)

        checkVersion()

        //startTimer()
    }

    private fun checkVersion() {
        class CheckVersion : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {

                var version = "0"
                try {
                    version = MyDatabase(this@SplashActivity).getInfoDao().getInfo().version
                } catch (e: Exception) {
                    Log.e(LOG, e.message.toString())
                }
                Log.v(LOG, "version: $version")
                return version
            }

            override fun onPostExecute(result: String?) {
                Log.v(LOG, "onPostExecute: $result")

                if (result == getVersionName() || result == "0") {
                    if (isOnline()) {
                        startTimer()
                    } else {
                        offlineAlert()
                    }
                } else {
                    updateAlert()
                    return
                }

                super.onPostExecute(result)
            }
        }
        CheckVersion().execute()
    }

    private fun offlineAlert() {
        builder = AlertDialog.Builder(this)
        builder.setTitle("Offline")
        builder.setMessage("Please turn your internet connection on")
        /*builder.setPositiveButton("Update Now"){dialog, which ->
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.appLinkPlayStore))
            startActivity(intent)
        }*/
        builder.setNegativeButton("Close App") { dialog, which ->
            dialog.dismiss()
            finishAffinity()
            finish()
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun updateAlert() {
        builder = AlertDialog.Builder(this)
        builder.setTitle("Update available")
        builder.setMessage("You are using an old version of this app. Please update to get better experience")
        builder.setPositiveButton("Update Now") { dialog, which ->
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.appLinkPlayStore))
            startActivity(intent)
        }
        builder.setNegativeButton("Later") { dialog, which ->
            dialog.dismiss()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    override fun onStart() {
        super.onStart()
        if (!getBooleanData("intro")){
            try {
                startActivity(
                    Intent(this, IntroActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
            } catch (e: Exception) {
                Log.getStackTraceString(e)
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                animation_view.visibility = INVISIBLE
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

            override fun onTick(millisUntilFinished: Long) {}

        }.start()
    }
}