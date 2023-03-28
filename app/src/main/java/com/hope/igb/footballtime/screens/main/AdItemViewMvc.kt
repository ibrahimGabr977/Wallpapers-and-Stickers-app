package com.hope.igb.footballtime.screens.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.nativead.NativeAd
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.adhelper.adnaitive.AdNativeHelper
import com.hope.igb.footballtime.adhelper.adnaitive.TemplateView
import com.hope.igb.footballtime.comman.views.BaseViewMvc

class AdItemViewMvc(inflater: LayoutInflater, parent : ViewGroup) :
    BaseViewMvc(), AdNativeHelper.AdNativeListener {

//    private val adView1: AdView
//    private val adView2: AdView
//    private val adView3: AdView
//    private val adBanner1 : AdBannerHelper
//    private val adBanner2 : AdBannerHelper
//    private val adBanner3 : AdBannerHelper

    private val mainAdView: TemplateView
    private val adHelper: AdNativeHelper
    private var position: Int = 0



    init {
        setRootView(inflater.inflate(R.layout.ad_holder, parent, false))

        mainAdView = findViewById(R.id.mainAdView)
        adHelper = AdNativeHelper(getContext())




//        adView1 = findViewById(R.id.mainAdView1)
//        adBanner1  = AdBannerHelper(AdmobHelper.adBannerTop, adView1)
//
//        adView2 = findViewById(R.id.mainAdView2)
//        adBanner2  = AdBannerHelper(AdmobHelper.adBannerMid, adView2)
//
//        adView3 = findViewById(R.id.mainAdView3)
//        adBanner3  = AdBannerHelper(AdmobHelper.adBannerBottom, adView1)
    }



     fun bindAdViewAt(position : Int){



         this.position = position
         adHelper.buildAd(getContext().getString(R.string.native_ad_id))

//         when (position) {
//             26 -> emadBanner1.loadAd()
//             51 -> adBanner2.loadAd()
//             else -> adBanner3.loadAd()
//         }


    }

    fun registerAd(){
        adHelper.registerListener(this)
//        when (position){
//            26 ->    adBanner1.registerListener(this)
//            51 ->    adBanner2.registerListener(this)
//            else ->  adBanner3.registerListener(this)
//        }

    }


     fun unRegisterAd(){
         adHelper.unregisterListener(this)

//        adBanner1.unregisterListener(this)
//         adBanner2.unregisterListener(this)
//         adBanner3.unregisterListener(this)
    }

    override fun onAdsLoaded(ads: ArrayList<NativeAd>) {
        Toast.makeText(getContext(), "$position", Toast.LENGTH_SHORT).show()
        getRootView().visibility = View.VISIBLE
        mainAdView.setNativeAd(ads[position])

    }

    override fun onAdsFailedToLoaded(error: String) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show()
        getRootView().visibility = View.GONE
        mainAdView.destroyNativeAd()

    }


//    
//    override fun onAdLoadedSuccess(adName: String) {
//        getRootView().visibility = View.VISIBLE


//
//            when(adName){
//                AdmobHelper.adBannerTop ->  {
//                    adView1.visibility = View.VISIBLE
//                    adView2.visibility = View.GONE
//                    adView3.visibility = View.GONE
//                }
//                AdmobHelper.adBannerMid -> {
//                    adView2.visibility = View.VISIBLE
//                    adView1.visibility = View.GONE
//                    adView3.visibility = View.GONE
//                }
//
//                else -> {
//                    adView3.visibility = View.VISIBLE
//                    adView1.visibility = View.GONE
//                    adView2.visibility = View.GONE
//                }
//            }
//    }
//
//    override fun onAdLoadedFailed(adName: String) {
//        getRootView().visibility = View.GONE

//        when(adName){
//            AdmobHelper.adBannerTop ->  adView1.visibility = View.GONE
//            AdmobHelper.adBannerMid ->  adView2.visibility = View.GONE
//            else -> adView3.visibility = View.GONE
//        }

//    }
}