package com.job.news

import com.job.news.utils.getAppName
import com.job.news.utils.getPackageName


object Constants {

    private val mContext = App.applicationContext()
    private val packageName = getPackageName()
    private val appName = getAppName()



    val appLinkPlayStore = "https://play.google.com/store/apps/details?id=$packageName"
    const val BASE_URL = "https://job.cpayou.com/app/"
    const val BASE_URL_IMG = "https://job.cpayou.com/assets/img/"
    const val BASE_URL_FILE = "https://job.cpayou.com/assets/img/files/"
    const val privacyPolicyUrl = "https://play.google.com/store/apps/details?"
    const val developerUrl = "https://www.facebook.com/shovoooon"
    const val facebookUrl = "https://www.facebook.com/shovoooon"
    const val youtubeUrl = "https://www.facebook.com/shovoooon"

    val share_msg: String = "Hey download" + appName + "Link: " + appLinkPlayStore

}