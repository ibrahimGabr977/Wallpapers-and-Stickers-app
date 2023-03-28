package com.hope.igb.footballtime.adhelper.adnaitive

import android.content.Context
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.hope.igb.footballtime.comman.views.BaseObservable

class AdNativeHelper(private val context: Context) :
    BaseObservable<AdNativeHelper.AdNativeListener>() {

    interface AdNativeListener {
        fun onAdsLoaded(ads: ArrayList<NativeAd>)
        fun onAdsFailedToLoaded(error: String)
    }

    private var adsList: ArrayList<NativeAd> = ArrayList(1)
    private lateinit var adLoader: AdLoader


    fun buildAd(adUnitID: String) {

        adsList.clear()


        adLoader = AdLoader.Builder(context, adUnitID)
            .forNativeAd {
                NativeAd.OnNativeAdLoadedListener {

                    // Show the ad.

                    adsList.add(it)
                    if (!adLoader.isLoading)
                        for (listener in getListeners())
                            listener.onAdsLoaded(adsList) }





            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                    for (listener in getListeners())
                        listener.onAdsFailedToLoaded(adError.message)
                }


            }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


}