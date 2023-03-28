package com.hope.igb.footballtime.screens.display

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.comman.views.BaseViewMvc

class DisplayItemViewMvc(inflater: LayoutInflater, parent:ViewGroup) : BaseViewMvc() {

    private val imageView:ImageView

    init {
        setRootView(inflater.inflate(R.layout.display_adapter, parent, false))

        imageView=findViewById(R.id.display_adapter_image)

    }


     fun bindViewAdapter(imageRes: Int){
        imageView.setImageResource(imageRes)
    }


}