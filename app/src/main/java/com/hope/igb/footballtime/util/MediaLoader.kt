package com.hope.igb.footballtime.util




import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator


class MediaLoader(private val viewDims: Int){




    fun loadImage(imageRes: Int): RequestCreator? {
            return Picasso.get()
                    .load(imageRes)
                    .noPlaceholder()
                    .resize(viewDims, viewDims)
        }








}


