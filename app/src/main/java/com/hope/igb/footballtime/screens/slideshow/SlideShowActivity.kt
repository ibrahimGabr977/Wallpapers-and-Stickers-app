package com.hope.igb.footballtime.screens.slideshow

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdView
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.adhelper.AdBannerHelper
import com.hope.igb.footballtime.adhelper.AdmobHelper
import com.hope.igb.footballtime.util.FullScreenInflater
import com.hope.igb.footballtime.util.ItemsListController

class SlideShowActivity : AppCompatActivity(), AdBannerHelper.AdmobBannerListener {

    private lateinit var viewPager: NonSwipedViewPager
    private lateinit var itemsList: List<Int>
    private lateinit var mainHandler: Handler
    private lateinit var adViewTop: AdView
    private lateinit var adViewBottom: AdView
    private lateinit var adBannerHelper1: AdBannerHelper
    private lateinit var adBannerHelper2: AdBannerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_slide_show)
        FullScreenInflater.setFullScreen(this)


        itemsList = if (intent.getStringExtra("CURRENT_ITEMS").equals("IMAGES"))
            ItemsListController.imagesList
        else
            ItemsListController.stickersList

        initPagerAdapter()
        mainHandler = Handler(Looper.getMainLooper())



        initAds()

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    mainHandler.removeCallbacksAndMessages(null)
                    finish()
                    finish()
                }
            })


    }


    private fun initAds(){
        adViewTop = findViewById(R.id.slideShowAdViewTop)
        adViewBottom = findViewById(R.id.slideShowAdViewBottom)


        adBannerHelper1 = AdBannerHelper(AdmobHelper.adBannerTop, adViewTop)
        adBannerHelper2 = AdBannerHelper(AdmobHelper.adBannerBottom, adViewBottom)

    }







    override fun onStart() {
        super.onStart()
        startSliding()

        adBannerHelper1.loadAd()
        adBannerHelper2.loadAd()
        adBannerHelper1.registerListener(this)
        adBannerHelper2.registerListener(this)
    }


    override fun onStop() {
        super.onStop()

        adBannerHelper1.unregisterListener(this)
        adBannerHelper2.unregisterListener(this)
    }



    private fun initPagerAdapter() {
        viewPager = findViewById(R.id.slideShowViewPager)
        val adapter = SlideShowAdapter(itemsList)
        viewPager.adapter = adapter
        viewPager.setSwipedEnabled(false)
    }


    private fun startSliding() {
        val runnable: Runnable = object : Runnable {
            var i: Int = 0
            override fun run() {


                viewPager.setCurrentItem(i, true)
                i++
                mainHandler.postDelayed(this, 790L)

                if (i >= itemsList.size)
                    i = 0
            }
        }

        mainHandler.postDelayed(runnable, 950L)
    }



    override fun onAdLoadedSuccess(adName: String) {

        if (adName == AdmobHelper.adBannerTop)
            adViewTop.visibility = View.VISIBLE

        else if (adName == AdmobHelper.adBannerBottom)
            adViewBottom.visibility = View.VISIBLE
    }




    override fun onAdLoadedFailed(adName: String) {

        if (adName == AdmobHelper.adBannerTop)
            adViewTop.visibility = View.GONE

        else if (adName == AdmobHelper.adBannerBottom)
            adViewBottom.visibility = View.GONE
    }

}