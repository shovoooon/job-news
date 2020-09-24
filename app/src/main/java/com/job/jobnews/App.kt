package com.job.jobnews

import android.app.Application
import android.content.Context
import com.job.jobnews.utils.ExampleNotificationOpenedHandler
import com.onesignal.OneSignal

class App : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: App? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = App.applicationContext()

        // Logging set to help debug issues, remove before releasing your app.
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN)

        OneSignal.startInit(this)
            .setNotificationOpenedHandler(ExampleNotificationOpenedHandler())
            .autoPromptLocation(true)
            .init()
    }
}