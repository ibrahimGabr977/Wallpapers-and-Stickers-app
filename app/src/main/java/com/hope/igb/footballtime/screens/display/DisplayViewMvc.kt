package com.hope.igb.footballtime.screens.display

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdView
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.adhelper.adnaitive.TemplateView
import com.hope.igb.footballtime.comman.views.BaseObservableViewMvc
import java.util.Random
import kotlin.math.abs

@Suppress("UNUSED_EXPRESSION")
@SuppressLint("InflateParams")
class DisplayViewMvc(inflater: LayoutInflater) :
    BaseObservableViewMvc<DisplayViewMvc.DisplayListener>(),
    View.OnClickListener, ViewPager.PageTransformer {



    interface DisplayListener {
        fun onBackClicked()
        fun onSlideShowClicked()
        fun onSaveClicked()
        fun onShareClicked()
        fun onSetWallpaperClicked()
        fun onSetLockScreenClicked()
    }

    private val viewPager: ViewPager
    var currentPosition: Int = 0
    val adViewBottom: AdView
    val nativeAdView: TemplateView



    init {
        setRootView(inflater.inflate(R.layout.activity_display, null, false))

        viewPager = findViewById(R.id.displayViewPager)



        nativeAdView = findViewById(R.id.displayAdView)
        adViewBottom = findViewById(R.id.displayAdViewBottom)
//        adViewTop = findViewById(R.id.displayAdViewTop)

        initView()

    }



    private fun initView(){
        findViewById<ImageView>(R.id.back_button).setOnClickListener(this)
        findViewById<ImageView>(R.id.slideShow).setOnClickListener(this)

        val menuView: LinearLayout = findViewById(R.id.menu_view)

        menuView.findViewById<TextView>(R.id.menu_save).setOnClickListener{
            currentPosition = viewPager.currentItem
            for (listener in getListeners())
                listener.onSaveClicked()

        }
        menuView.findViewById<TextView>(R.id.menu_share).setOnClickListener{
            currentPosition = viewPager.currentItem
            for (listener in getListeners())
                listener.onShareClicked()
        }
        menuView.findViewById<TextView>(R.id.menu_homeScreen).setOnClickListener{
            currentPosition = viewPager.currentItem
            for (listener in getListeners())
                listener.onSetWallpaperClicked()
        }
        menuView.findViewById<TextView>(R.id.menu_lock).setOnClickListener{
            currentPosition = viewPager.currentItem
            for (listener in getListeners())
                listener.onSetLockScreenClicked()
        }

        menuView.findViewById<TextView>(R.id.menu_jump).setOnClickListener{ onJumpClicked() }
    }

    private fun onJumpClicked() {
        val randomPosition = Random().nextInt(viewPager.childCount)
        viewPager.setCurrentItem(randomPosition, true)
        currentPosition = randomPosition
    }


    @SuppressLint("ClickableViewAccessibility")
     fun fetchImages(initialPosition: Int, itemsList: List<Int>){
        val adapter = DisplayPagerAdapter(itemsList)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(initialPosition, true)

         viewPager.setPageTransformer(true, this)

     }



    override fun onClick(p0: View?) {
        for (listener in getListeners()){
            when(p0!!.id){
                R.id.back_button -> listener.onBackClicked()
                R.id.slideShow -> listener.onSlideShowClicked()
            }
        }


    }

    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        if (position < -1) {
            page.alpha = 0f
        } else if (position <= 0) {
            page.alpha = 1f
            page.pivotX = 0f
            page.rotationY = 90 * abs(position)
        } else if (position <= 1) {
            page.alpha = 1f
            page.pivotX = page.width.toFloat()
            page.rotationY = -90 * abs(position)
        } else page.alpha = 0f
    }


}