package com.job.jobnews

import com.job.jobnews.utils.getAppName
import com.job.jobnews.utils.getPackageName


object Constants {

    private val mContext = App.applicationContext()
    private val packageName = getPackageName()
    private val appName = getAppName()



    val appLinkPlayStore = "https://play.google.com/store/apps/details?id=$packageName"
    const val BASE_URL = "https://app.jobnewspaperbd.com/app/"
    const val BASE_URL_IMG = "https://app.jobnewspaperbd.com/assets/img/"
    const val BASE_URL_FILE = "https://app.jobnewspaperbd.com/assets/files/"
    const val privacyPolicyUrl = "https://play.google.com/store/apps/details?"
    const val developerUrl = "https://www.facebook.com/shovoooon"
    const val facebookUrl = "https://www.facebook.com/allnewsonlinebd"
    const val youtubeUrl = "https://www.youtube.com/c/AllnewsonlineBD"

    val share_msg: String = "Hey download" + appName + "Link: " + appLinkPlayStore

    /*const val banner_id = "ca-app-pub-3940256099942544/6300978111"
    const val interstitial_id = "ca-app-pub-3940256099942544/1033173712"*/

    const val banner_id = "ca-app-pub-7707281179781435/1350199054"
    const val interstitial_id = "ca-app-pub-7707281179781435/2861401903"

}