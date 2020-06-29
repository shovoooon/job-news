package com.job.news.ui.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.layout_intro.view.*


class IntroAdapter(private val context: Context, private val itemList: List<IntroItem>) :
    PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View =
            inflater.inflate(com.job.news.R.layout.layout_intro, null)

        layoutScreen.introTitle.text = itemList.get(position).title
        layoutScreen.introImg.setImageResource(itemList.get(position).imgId)
        container.addView(layoutScreen)
        return layoutScreen
    }
}