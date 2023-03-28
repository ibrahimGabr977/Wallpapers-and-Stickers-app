package com.hope.igb.footballtime.screens.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.adhelper.AdmobHelper
import com.hope.igb.footballtime.screens.main.MainActivity
import com.hope.igb.footballtime.util.FullScreenInflater

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), AnimatedTextView.AnimationListener {
    private lateinit var animatedTextView : AnimatedTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
        FullScreenInflater.setFullScreen(this@SplashActivity)


        val textView : TextView = findViewById(R.id.splash_text)

        animatedTextView = AnimatedTextView()
        animatedTextView.startTextAnimation(textView, getString(R.string.app_name),
                75L, 1)


        if (savedInstanceState == null)
            AdmobHelper.initializeAdmob(this)

    }




    override fun onStart() {
        super.onStart()
        animatedTextView.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        animatedTextView.unregisterListener(this)
    }


    override fun onAnimationFinished(textViewCount: Int) {
        if (textViewCount != 2)
            startWelcomeSentenceAnimation()
        else
            toMainActivity()
    }



    private fun toMainActivity(){
        Handler(Looper.getMainLooper())
                .postDelayed({
                     val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("SCREEN_WIDTH", this.window.decorView.width)
                    startActivity(intent)
                },

                        25L)
    }



    private fun startWelcomeSentenceAnimation() {
        val welcomeTextView : TextView = findViewById(R.id.splash_welcome_text)
        animatedTextView.startTextAnimation(welcomeTextView,
                getString(R.string.welcome),
                25L,
                2)

    }
}