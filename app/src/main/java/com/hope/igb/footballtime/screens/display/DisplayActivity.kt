package com.hope.igb.footballtime.screens.display

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.os.StrictMode.VmPolicy
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.nativead.NativeAd
import com.gw.swipeback.SwipeBackLayout
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.adhelper.AdBannerHelper
import com.hope.igb.footballtime.adhelper.AdInterstitialHelper
import com.hope.igb.footballtime.adhelper.AdmobHelper
import com.hope.igb.footballtime.adhelper.adnaitive.AdNativeHelper
import com.hope.igb.footballtime.comman.messagehelper.SystemMessageHelper
import com.hope.igb.footballtime.screens.display.util.ImageSaver
import com.hope.igb.footballtime.screens.display.util.WallpaperSetter
import com.hope.igb.footballtime.screens.slideshow.SlideShowActivity
import com.hope.igb.footballtime.util.InAppReviewHelper
import com.hope.igb.footballtime.util.ItemsListController
import com.hope.igb.footballtime.util.PermissionHandler
import java.io.File


class DisplayActivity : AppCompatActivity(), DisplayViewMvc.DisplayListener,
    PermissionHandler.PermissionGrantedListener, WallpaperSetter.WallpaperSetterListener,
    AdBannerHelper.AdmobBannerListener, AdNativeHelper.AdNativeListener {


    private var initialPosition = 0
    private lateinit var currentItems : List<Int>
    private lateinit var viewMvc: DisplayViewMvc
    private lateinit var imageSaver: ImageSaver
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var savedImageFileName: String
    private  var isFirstLaunch : Boolean = true
    private  var wallpaperSetter : WallpaperSetter? =null
//    private lateinit var adBannerTop: AdBannerHelper
    private lateinit var adBannerBottom: AdBannerHelper
    private lateinit var adInterstitial: AdInterstitialHelper
    private lateinit var adNativeHelper: AdNativeHelper
    private lateinit var systemMessageHelper: SystemMessageHelper





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val mSwipeBackLayout = SwipeBackLayout(this)
        mSwipeBackLayout.directionMode = SwipeBackLayout.FROM_TOP
        mSwipeBackLayout.maskAlpha = 255
        mSwipeBackLayout.swipeBackFactor = 1.0f


        viewMvc = DisplayViewMvc(layoutInflater)




        mSwipeBackLayout.addView(viewMvc.getRootView())

        val layoutParams = viewMvc.getRootView().layoutParams
        layoutParams.width = intent.getIntExtra("SCREEN_WIDTH", 0)
        mSwipeBackLayout.layoutParams = layoutParams

        setContentView(mSwipeBackLayout)



        AdmobHelper.initializeAdmob(this)


        initialPosition = intent.getIntExtra("INITIAL_POSITION", 0)
        val current = intent.getStringExtra("CURRENT_ITEMS")


        currentItems = if (current.equals("IMAGES")) ItemsListController.imagesList else ItemsListController.stickersList
        imageSaver = ImageSaver(this, if (current.equals("IMAGES")) "Photos" else "Stickers")





        permissionHandler = PermissionHandler(this)

//        adBannerTop = AdBannerHelper(AdmobHelper.adBannerTop, viewMvc.adViewTop)
        adBannerBottom = AdBannerHelper(AdmobHelper.adBannerBottom, viewMvc.adViewBottom)

        adNativeHelper = AdNativeHelper(this)
        adNativeHelper.buildAd(getString(R.string.native_ad_id))
        adNativeHelper.registerListener(this)

        adInterstitial = AdInterstitialHelper(this)





        systemMessageHelper = SystemMessageHelper(this)



    }






    
    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
        viewMvc.fetchImages(initialPosition, currentItems)

//        adBannerTop.loadAd()
        adBannerBottom.loadAd()
        adInterstitial.initAd(getString(R.string.interstitial_ad_id1))
//        adBannerTop.registerListener(this)
        adBannerBottom.registerListener(this)


    }


    @SuppressLint("SuspiciousIndentation")
    override fun onStop() {
        super.onStop()

        viewMvc.unregisterListener(this)


        if (wallpaperSetter != null) {
            wallpaperSetter?.unregisterListener(this)
            wallpaperSetter?.cancel()
            wallpaperSetter = null
        }


//        adBannerTop.unregisterListener(this)
        adBannerBottom.unregisterListener(this)
        adNativeHelper.unregisterListener(this)
    }









    override fun onSaveClicked() {
        val imageFileName = "Football_"+System.currentTimeMillis()

        grantPermissionAndSave(imageFileName)

    }



    override fun onShareClicked() {
        grantPermissionAndSave("temp_image")
    }





    private fun grantPermissionAndSave(fileName: String){

        if (permissionHandler.isStoragePermissionGranted()){

            val  imageFile = imageSaver.saveImage(fileName, currentItems[viewMvc.currentPosition])
            isFirstLaunch = false
            onImageImageSaved(imageFile, fileName)


        }else{
            savedImageFileName = fileName
            isFirstLaunch = true
            permissionHandler.registerListener(this)
        }

    }






    override fun onPermissionGranted() {
        permissionHandler.unregisterListener(this)
        systemMessageHelper.showShortMessage("Saving your image, that may take few seconds on first launch")

        grantPermissionAndSave(savedImageFileName)

    }


    override fun onPermissionNotGranted() {
        permissionHandler.unregisterListener(this)

        systemMessageHelper.showLongMessage("Please give a storage permission to share or save an image")
    }



    private fun onImageImageSaved(imageFile: File, fileName: String){
        if (fileName != "temp_image"){
            systemMessageHelper.showLongMessage("The image saved successfully to ${imageFile.path}")
            adInterstitial.showAd()


            if (isFirstLaunch || isChanceToShowReviewCome())
                InAppReviewHelper.reviewApp(this@DisplayActivity)



            refreshGalleryToShowTheSavedImage(imageFile)
        }

        else
            shareFile(imageFile)
    }

    private fun isChanceToShowReviewCome(): Boolean {
        val random = (Math.random() * 100).toInt()

        return random != 0 && random % 3==0

    }


    private fun refreshGalleryToShowTheSavedImage(imageFile: File){
        MediaScannerConnection.scanFile(
            this@DisplayActivity, arrayOf(imageFile.path), null,
            null
        )
    }



    private fun shareFile(file: File){
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpg"
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
        startActivity(Intent.createChooser(share, "Share image"))
    }







    override fun onSlideShowClicked() {

        val intent = Intent(this@DisplayActivity, SlideShowActivity::class.java)
        intent.putExtra("CURRENT_ITEMS", getIntent().getStringExtra("CURRENT_ITEMS"))
        startActivity(intent)
    }





    override fun onSetWallpaperClicked() {
        wallpaperSetter = WallpaperSetter(this, currentItems[viewMvc.currentPosition], "home")
        wallpaperSetter?.registerListener(this)
        wallpaperSetter?.execute()



    }



    override fun onSetLockScreenClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wallpaperSetter = WallpaperSetter(this, currentItems[viewMvc.currentPosition],  "lock")
            wallpaperSetter?.registerListener(this)
            wallpaperSetter?.execute()




        }else
            systemMessageHelper.showLongMessage("Setting lock screen wallpaper need android 7 or higher.")
    }


    override fun onStartSetting(type: String) {
        runOnUiThread{systemMessageHelper.showLongMessage("Setting $type screen wallpaper....") }
    }



    override fun onSuccessFullySet(type: String) {
        if (type == "lock")
            systemMessageHelper.showLongMessage("Lock screen wallpaper setting successfully")
        else
            systemMessageHelper.showLongMessage("Home wallpaper setting successfully")

        wallpaperSetter?.unregisterListener(this)

        adInterstitial.showAd()
    }












    override fun onAdLoadedSuccess(adName: String) {
        if (adName == AdmobHelper.adBannerBottom)
            viewMvc.adViewBottom.visibility = View.VISIBLE
//        else
//            viewMvc.adViewTop.visibility = View.VISIBLE
    }

    override fun onAdLoadedFailed(adName: String) {
        if (adName == AdmobHelper.adBannerBottom)
            viewMvc.adViewBottom.visibility = View.INVISIBLE
//        else
//            viewMvc.adViewTop.visibility = View.INVISIBLE
    }

    override fun onBackClicked() {
        wallpaperSetter?.cancel()
        onBackPressedDispatcher.onBackPressed()
    }




    override fun onAdsLoaded(ads: ArrayList<NativeAd>) {
        viewMvc.nativeAdView.visibility = View.VISIBLE
        viewMvc.nativeAdView.setNativeAd(ads[0])
    }

    override fun onAdsFailedToLoaded(error: String) {
        systemMessageHelper.showLongMessage(error)
    }


}