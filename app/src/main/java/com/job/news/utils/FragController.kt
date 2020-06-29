package com.job.news.utils

import android.util.Log
import com.job.news.MainActivity
import com.job.news.R
import com.job.news.ui.CategoryOpenFragment

class FragController {

    companion object{
        const val containerId = R.id.frame_layout
    }

    fun navigateToOpenCat(mainActivity: MainActivity) {

            try {
                val fragment = CategoryOpenFragment()
                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .commitAllowingStateLoss()
            } catch (e: Exception) {
                Log.e("Can't replace fragment.", e.message.toString())
            }
    }
}