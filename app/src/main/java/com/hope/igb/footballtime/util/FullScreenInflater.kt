package com.hope.igb.footballtime.util

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager

@Suppress("DEPRECATION")
abstract class FullScreenInflater {

    companion object{
        //set activity full screen ---> remove title bar
         fun setFullScreen(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val insetsController = activity.window.insetsController
                insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                activity.window.setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
    }



}