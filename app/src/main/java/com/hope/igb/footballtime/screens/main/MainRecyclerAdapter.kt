package com.hope.igb.footballtime.screens.main

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MainRecyclerAdapter(
    private val activity: Activity, private val items: List<Int>,
    private val screenWidth: Int, private val listener: ImageClickedListener) :
    RecyclerView.Adapter<ViewHolder>(),
    MainAdapterItemViewMvc.MainAdapterItemListener {



    interface ImageClickedListener {
        fun onImageClicked(position: Int)
    }


//    private val imageModels: ArrayList<ImageModel> = ArrayList()
//
//
//    fun addItem(image: ImageModel){
//        imageModels.add(image)
//        this.notifyItemInserted(imageModels.size-1)
//
//    }

    class MainViewHolder(val itemViewMvc: MainAdapterItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())


    class MainAdHolder(val itemViewMvc: AdItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())

    object ViewType{
        const val IMAGE = 0
        const val AD_BANNER = 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ViewType.IMAGE) {
            val itemViewMvc = MainAdapterItemViewMvc(
                LayoutInflater.from(activity),
                parent, screenWidth
            )
            MainViewHolder(itemViewMvc)

        }else{
            val itemViewMvc = AdItemViewMvc(LayoutInflater.from(activity), parent)
            MainAdHolder(itemViewMvc)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 16 || position == 31 || position == 46 || position == 61 || position == 76)
            ViewType.AD_BANNER else ViewType.IMAGE
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        if (holder is MainViewHolder)
            holder.itemViewMvc.unregisterListener(this)
        else
            (holder as MainAdHolder).itemViewMvc.unRegisterAd()

    }


    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder is MainViewHolder)
            holder.itemViewMvc.registerListener(this)
        else
            (holder as MainAdHolder).itemViewMvc.registerAd()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adUnitIndex = when (position) {
            in 0..16 -> 0
            in 16..31 -> 1
            in 31..46 -> 2
            in 46..61 -> 3
            in 61..76 -> 4
            else -> 5
        }



        if (holder is MainViewHolder)
            holder.itemViewMvc.bindAdapterItem(items[position - adUnitIndex], position - adUnitIndex)
        else
            (holder as MainAdHolder).itemViewMvc.bindAdViewAt(adUnitIndex)


    }


    override fun getItemCount(): Int {
        return items.size + 5
    }


    override fun onImageClicked(position: Int) {
        listener.onImageClicked(position)
    }






}