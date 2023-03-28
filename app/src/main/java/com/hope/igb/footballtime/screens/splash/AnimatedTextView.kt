package com.hope.igb.footballtime.screens.splash

import android.annotation.SuppressLint
import android.os.Looper
import android.widget.TextView
import com.hope.igb.footballtime.comman.views.BaseObservable

class AnimatedTextView
 : BaseObservable<AnimatedTextView.AnimationListener>() {

 interface AnimationListener{
  fun onAnimationFinished(textViewCount: Int)
 }


  @SuppressLint("SetTextI18n")
  fun startTextAnimation(animatedTextView: TextView, animatedText: String,
                         delay : Long, textViewCount: Int) {

   val charArray: CharArray = animatedText.toCharArray()
   animatedTextView.text = "" + charArray[0]


   val mainHandler = android.os.Handler(Looper.getMainLooper())
   val runnable: Runnable = object : Runnable {
    var i: Int = 1
    override fun run() {
     if (i < charArray.size) {
      animatedTextView.text = animatedTextView.text.toString() + "" + charArray[i]
      i++

      mainHandler.postDelayed(this, delay)
     } else
      for (listener in getListeners())
       listener.onAnimationFinished(textViewCount)
    }

   }

   mainHandler.postDelayed(runnable, 100L)
  }



}