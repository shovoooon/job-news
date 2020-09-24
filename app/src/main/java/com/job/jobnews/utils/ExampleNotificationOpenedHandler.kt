package com.job.jobnews.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.job.jobnews.App
import com.job.jobnews.MainActivity
import com.job.jobnews.ui.job.JobActivity
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal


class ExampleNotificationOpenedHandler : OneSignal.NotificationOpenedHandler {

    // This fires when a notification is opened by tapping on it.
    override fun notificationOpened(result: OSNotificationOpenResult) {
        val actionType = result.action.type
        val data = result.notification.payload.additionalData
        val appURL = result.notification.payload.launchURL

        if (data != null){

            val customKey = data.optString("key", null)
            if (customKey != null){
                Log.v("OneSignalExample", "customkey set with value: $customKey")

                val intent = Intent(App.applicationContext(), JobActivity::class.java)
                intent.putExtra("key", customKey)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    App.applicationContext().startActivity(intent)
                }catch (e:Exception){Log.e("OneSignalExample", e.message.toString())}

                return
            }else{
                val intent = Intent(App.applicationContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    App.applicationContext().startActivity(intent)
                }catch (e:Exception){Log.e("OneSignalExample", e.message.toString())}
                return
            }

        }


        if (appURL != null){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appURL))
            try {
                App.applicationContext().startActivity(intent)
            }catch (e:Exception){Log.e("OneSignalExample", e.message.toString())}
            return
        }


        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: $result.action.actionID")


        val intent = Intent(App.applicationContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            App.applicationContext().startActivity(intent)
        }catch (e:Exception){Log.e("OneSignalExample", e.message.toString())}

        // The following can be used to open an Activity of your choice.
        //   Replace MainActivity with your own Activity class.
        /*val intent = Intent(App.applicationContext(), JobActivity::class.java)
        intent.putExtra("id", customKey)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.applicationContext().startActivity(intent)*/

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //  if you are calling startActivity above.
        /*
        <application ...>
          <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
        </application>
           */
    }
}