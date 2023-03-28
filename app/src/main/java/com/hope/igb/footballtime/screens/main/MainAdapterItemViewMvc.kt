package com.hope.igb.footballtime.screens.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.comman.views.BaseObservableViewMvc
import com.hope.igb.footballtime.util.MediaLoader
import com.squareup.picasso.Callback
import java.lang.Exception

class MainAdapterItemViewMvc (inflater: LayoutInflater, parent: ViewGroup, screen_width : Int)
    : BaseObservableViewMvc<MainAdapterItemViewMvc.MainAdapterItemListener>(), Callback {



    interface MainAdapterItemListener {
        fun onImageClicked(position: Int)
//        fun onImageSuccessLoading()
//        fun onImageFailedLoading(error: String?)
    }


    private var  imageView:ImageView
    private var errorView:TextView
    private var mediaLoader: MediaLoader


    init {
        setRootView(inflater.inflate(R.layout.image_holder, parent, false))

        val params = getRootView().layoutParams as ViewGroup.LayoutParams


        val viewDims = ((screen_width * 0.95 / 2.0) - 8.0).toInt()



        params.height = viewDims



        getRootView().layoutParams = params

        imageView = findViewById(R.id.image_holder_image)

        errorView = findViewById(R.id.error_view)

        mediaLoader = MediaLoader(((viewDims) * 1.15).toInt())



    }



     fun bindAdapterItem(imageRes: Int, position: Int){


             mediaLoader.loadImage(imageRes)?.into(imageView,this)
         getRootView().setOnClickListener {

             for (listener in getListeners())
                 listener.onImageClicked(position)
         }

     }






    override fun onSuccess() {


        getRootView().visibility = View.VISIBLE
        imageView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
    }

    override fun onError(e: Exception?) {
        getRootView().visibility = View.VISIBLE
        errorView.visibility = View.VISIBLE
        imageView.visibility = View.GONE
    }

//
//    fun bindAdapterItem(imageModel: ImageModel, position: Int){
//
//
//        mediaLoader.loadImage(imageModel.mainSizeUrl, this).into(imageView)
//
//
//
//
//        getRootView().setOnClickListener {
//
//            for (listener in getListeners())
//                listener.onImageClicked(position, imageModel.id)
//        }
//
//    }








//    override fun onLoadFailed(
//        e: GlideException?,
//        model: Any?,
//        target: Target<Drawable?>?,
//        isFirstResource: Boolean
//    ): Boolean {
//        for (listener in getListeners())
//            listener.onImageFailedLoading(e?.message)
//        return false
//    }
//
//    override fun onResourceReady(
//        resource: Drawable?,
//        model: Any?,
//        target: Target<Drawable?>?,
//        dataSource: DataSource?,
//        isFirstResource: Boolean
//    ): Boolean {
//
//        for (listener in getListeners())
//            listener.onImageSuccessLoading()
//        return false
//    }


}