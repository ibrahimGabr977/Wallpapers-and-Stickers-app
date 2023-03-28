package com.hope.igb.footballtime.screens.slideshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.hope.igb.footballtime.R

class SlideShowAdapter( private val itemList: List<Int>): PagerAdapter() {


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val rootView = LayoutInflater.from(container.context).inflate(R.layout.display_adapter, container, false)
        container.addView(rootView)


        val imageView:ImageView = rootView.findViewById(R.id.display_adapter_image)

        imageView.setImageResource(itemList[position])



        return rootView
    }




    override fun getCount(): Int {
        return itemList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}