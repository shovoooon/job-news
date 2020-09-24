package com.job.jobnews.admob

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.job.jobnews.App
import com.job.jobnews.Constants


class AdsInit {

    private lateinit var mInterstitialAd: InterstitialAd

    fun mobileAds() {
        MobileAds.initialize(App.applicationContext()){}
        mInterstitialAd = InterstitialAd(App.applicationContext())
        mInterstitialAd.adUnitId = Constants.interstitial_id

        if (!mInterstitialAd.isLoaded){
            mInterstitialAd.loadAd(AdRequest.Builder().build())
        }

    }

    fun isLoaded() : Boolean {
        return mInterstitialAd.isLoaded
    }

    fun showAd() {
        mInterstitialAd = InterstitialAd(App.applicationContext())
        if (mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }
    }
}