package com.hope.igb.footballtime.screens.display.util

import android.app.Activity
import com.hope.igb.footballtime.comman.views.BaseObservable

abstract class MyAsyncTask<PROGRESS_LISTENER>(private val activity: Activity?): BaseObservable<PROGRESS_LISTENER>() {


    private lateinit var thread: Thread


    private fun startBackground() {
        thread = Thread {
            doInBackground()

            activity!!.runOnUiThread { onPostExecute() }
        }

        thread.start()
    }

    fun execute() {
        startBackground()
    }
    fun cancel(){
        thread.interrupt()
    }

    abstract fun doInBackground()
    abstract fun onPostExecute()

}