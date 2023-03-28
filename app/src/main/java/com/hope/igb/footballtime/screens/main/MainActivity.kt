package com.hope.igb.footballtime.screens.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.hope.igb.footballtime.R
import com.hope.igb.footballtime.adhelper.AdInterstitialHelper
import com.hope.igb.footballtime.screens.display.DisplayActivity
import com.hope.igb.footballtime.util.InAppReviewHelper
import com.hope.igb.footballtime.util.ItemsListController


@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity(),
    MainRecyclerAdapter.ImageClickedListener {

    private  var screenWidth = 0
    private lateinit var adInterstitial: AdInterstitialHelper

    private lateinit var bottomNavigationView: BottomNavigationView
    private  var imagesAdapter : MainRecyclerAdapter? = null
    private  var stickersAdapter : MainRecyclerAdapter? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var  layoutManager: GridLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        screenWidth = intent.getIntExtra("SCREEN_WIDTH", 0)

        fetchRecyclerView()

        adInterstitial = AdInterstitialHelper(this)


        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView.id = R.id.bnv_item_images
        fetchImages()

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                InAppReviewHelper.reviewApp(this@MainActivity)
                finishAffinity()
            }
        })



        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener {

            Snackbar.make(window.decorView, "Refreshing", Snackbar.LENGTH_INDEFINITE)
                .setAction("Done") {
                    swipeRefreshLayout.isRefreshing = false
                }.show()
        }

    }


    private fun fetchView(){



        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bnv_item_images -> fetchImages()
                R.id.bnv_item_stickers -> fetchStickers()

            }
            showInterstitialAdByChance()



            true
        }



    }




    private fun fetchRecyclerView() {
        recyclerView = findViewById(R.id.mainRecyclerView)
        recyclerView.setHasFixedSize(true)

        layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object :
           SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
//                if (noDecorationAddedTo(recyclerView))
//                    recyclerView.addItemDecoration(MyItemsMarginsDecoration(15,
//                        2, false))

                return if(position == 16 || position == 31 || position == 46 || position == 61 || position == 76) 2 else 1
            }

        }

        recyclerView.layoutManager = layoutManager






    }


    private fun fetchImages(){
        Thread{
            if (imagesAdapter == null)
                imagesAdapter =  MainRecyclerAdapter(this, imagesList(), screenWidth, this)

            runOnUiThread {
                recyclerView.adapter = imagesAdapter
                imagesAdapter!!.notifyDataSetChanged()
            }
        }.start()



    }

    @SuppressLint("SuspiciousIndentation")
    private fun fetchStickers(){

            if (stickersAdapter == null)
                stickersAdapter = MainRecyclerAdapter(this, stickersList(), screenWidth, this)

                recyclerView.adapter = stickersAdapter
                stickersAdapter!!.notifyDataSetChanged()



    }


    private fun noDecorationAddedTo(recyclerView: RecyclerView): Boolean {
        return recyclerView.itemDecorationCount == 0
    }


    override fun onStart() {
        super.onStart()
        fetchView()


        adInterstitial.initAd(getString(R.string.interstitial_ad_id1))
        showInterstitialAdByChance()


    }





    override fun onImageClicked(position: Int) {

//        toDisplayActivity(position)
        val smoothScroller: SmoothScroller =
            object : LinearSmoothScroller(this.applicationContext) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }

        smoothScroller.targetPosition = 150

        recyclerView.layoutManager?.startSmoothScroll(smoothScroller)






    }


    private fun imagesList(): List<Int> {
        return ItemsListController.imagesList
    }

    private fun stickersList(): List<Int> {
        return ItemsListController.stickersList
    }

    private fun toDisplayActivity(position: Int) {

        val displayIntent = Intent(this@MainActivity, DisplayActivity::class.java)
         val currentItems = if(bottomNavigationView.selectedItemId == R.id.bnv_item_images) "IMAGES" else "STICKERS"
        displayIntent.putExtra("SCREEN_WIDTH", window.decorView.width)
        displayIntent.putExtra("INITIAL_POSITION", position)
        displayIntent.putExtra("CURRENT_ITEMS", currentItems)
        startActivity(displayIntent)
    }




    private fun showInterstitialAdByChance(){
        val random = (Math.random() * 100).toInt()

        if (random % 7==0)
            adInterstitial.showAd()
    }





    //gif
//    override fun onImageSuccessLoading() {
//        if (currentImagePosition < images.size)
//            viewMvc.addImageToListView(images["all_images"]?.get(currentImagePosition++)!!)
//
//
//    }
//
//    override fun onImageFailedLoading(error: String?) {
//        TODO("Not yet implemented")
//    }



}