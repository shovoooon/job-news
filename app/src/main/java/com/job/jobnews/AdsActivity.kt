package com.job.jobnews

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.job.jobnews.admob.AdsInit
import kotlinx.android.synthetic.main.activity_ads.*


class AdsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)


        banner_ad.setOnClickListener {
            val adContainer = findViewById<View>(R.id.adMobView)

            val mAdView = AdView(this)
            mAdView.adSize = AdSize.SMART_BANNER
            mAdView.adUnitId = Constants.banner_id
            (adContainer as RelativeLayout).addView(mAdView)

            MobileAds.initialize(this)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)

        }


        inter_ad.setOnClickListener {
            AdsInit().mobileAds()
        }

        show_inter.setOnClickListener {
            AdsInit().showAd()
        }
    }
}