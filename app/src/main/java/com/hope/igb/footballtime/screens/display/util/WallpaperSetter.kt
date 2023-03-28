package com.hope.igb.footballtime.screens.display.util

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import androidx.annotation.RequiresApi


class WallpaperSetter(private val activity: Activity, private val imageRes: Int, private val type: String):
    MyAsyncTask<WallpaperSetter.WallpaperSetterListener>(activity) {


    interface WallpaperSetterListener {
        fun onStartSetting(type: String)
        fun onSuccessFullySet(type: String)
    }

    private val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(activity)


    private fun getFittedBitmapToScreenSize(): Bitmap {

        val bitmap = BitmapFactory.decodeResource(activity.resources, imageRes)
        val width = getScreenDims()[0]



        return Bitmap.createScaledBitmap(bitmap, width, bitmap.height, true)
    }





    @Suppress("deprecation")
    fun getScreenDims(): Array<Int> {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics

            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())

            arrayOf(
                (windowMetrics.bounds.width() - insets.left - insets.right),
                (windowMetrics.bounds.height() - insets.bottom - insets.top)
            )

        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

            arrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)


        }
    }






    @RequiresApi(Build.VERSION_CODES.N)
    override fun doInBackground() {
        for (listener in getListeners())
            listener.onStartSetting(type)


        if (type == "lock")
            setLockScreen()
        else
            setWallpaper()



    }

    private fun setWallpaper() {
        wallpaperManager.setBitmap(getFittedBitmapToScreenSize())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setLockScreen() {
        wallpaperManager.setBitmap(getFittedBitmapToScreenSize(), null,true, WallpaperManager.FLAG_LOCK)

    }

    override fun onPostExecute() {
        for (listener in getListeners())
            listener.onSuccessFullySet(type)
    }




}