package com.hope.igb.footballtime.screens.slideshow

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class NonSwipedViewPager(context: Context, attr: AttributeSet?) : ViewPager(context, attr) {

    private var swipedEnabled: Boolean = true



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (swipedEnabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return if (swipedEnabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setSwipedEnabled(enabled: Boolean) {
        this.swipedEnabled = enabled
    }
}