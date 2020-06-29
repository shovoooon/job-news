package com.job.news.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import com.job.news.MainActivity
import com.job.news.R
import com.job.news.utils.saveBooleanData
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private lateinit var adapter : IntroAdapter
    private var introList = ArrayList<IntroItem>()
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_intro)

        loadList()

        adapter = IntroAdapter(this, introList)
        viewPager.adapter = adapter

        introTab.setupWithViewPager(viewPager)

        introNext.setOnClickListener{
            position = viewPager.currentItem
            if (position < introList.size){
                position++
                viewPager.currentItem = position

            }

            if (position == introList.size -1){
                loadGetStarted()
            }
        }

        btnGetStarted.setOnClickListener {
            Log.v("LOG","get started 2")
            saveBooleanData("intro", true)
            startActivity(Intent(this, MainActivity::class.java))
        }

        introTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == introList.size - 1){
                    loadGetStarted()
                }else{
                    unLoadGetStarted()
                    Log.v("LOG","get started")
                }
            }

        })

    }

    private fun loadGetStarted() {
        btnGetStarted.visibility = VISIBLE
        introTab.visibility = INVISIBLE
        introNext.visibility = INVISIBLE
    }

    private fun unLoadGetStarted() {
        btnGetStarted.visibility = INVISIBLE
        introTab.visibility = VISIBLE
        introNext.visibility = VISIBLE
    }

    private fun loadList() {
        introList.add(IntroItem("itle 1", R.drawable.job))
        introList.add(IntroItem("itle 2", R.drawable.jobseeker))
        introList.add(IntroItem("itle 3", R.drawable.resume))
        introList.add(IntroItem("itle 4", R.drawable.pdf))
    }

}