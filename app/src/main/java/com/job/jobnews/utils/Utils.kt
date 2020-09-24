package com.job.jobnews.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import android.widget.Toast
import com.job.jobnews.App
import kotlin.random.Random


private val mApplicationContext = App.applicationContext()

fun toast(msg: String) {
    Toast.makeText(mApplicationContext, msg, Toast.LENGTH_SHORT).show()
}


fun isOnline(): Boolean {
    val cm =
        mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun packageTrue(): Boolean {
    val packageName = mApplicationContext.packageName
    return packageName == "com.shovon.onetouch"
}


fun isVPN(): Boolean {
    val cm =
        mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting
}


fun saveData(
    key: String?,
    value: String?
) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
    val editor = preferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getData(key: String?): String? {
    val preferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
    return preferences.getString(key, null)
}

fun saveIntData(
    key: String?,
    value: Int
) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
    val editor = preferences.edit()
    editor.putInt(key, value)
    editor.apply()

}

fun getIntData(key: String?): Int {
    val preferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
    return preferences.getInt(key, 0)
}

fun saveBooleanData(
    key: String?,
    value: Boolean
) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
    val editor = preferences.edit()
    editor.putBoolean(key, value)
    editor.apply()

}

fun getBooleanData(key: String?): Boolean {
    val preferences = PreferenceManager.getDefaultSharedPreferences(mApplicationContext)
    return preferences.getBoolean(key, false)
}

fun getPackageName(): String? {
    return mApplicationContext.packageName
}

fun getAppName(): String? {
    val packageManager = mApplicationContext.packageManager
    var applicationInfo: ApplicationInfo? = null
    try {
        applicationInfo =
            packageManager.getApplicationInfo(mApplicationContext.applicationInfo.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
    }
    return (if (applicationInfo != null) packageManager.getApplicationLabel(applicationInfo) else "Unknown") as String
}

fun getVersionName(): String {

    val pInfo: PackageInfo = mApplicationContext.packageManager.getPackageInfo(getPackageName(), 0)

    return pInfo.versionName
}

fun adCanShow() : Boolean {
    val rand = Random.nextInt(0, 100)
    return rand < 10
}


