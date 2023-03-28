package com.hope.igb.footballtime.util

import android.app.Activity
import android.widget.Toast
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task


class InAppReviewHelper {



    companion object{
        fun reviewApp(activity: Activity) {

            val manager = ReviewManagerFactory.create(activity)
            val request: Task<ReviewInfo> = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        // We can get the ReviewInfo object
                        val reviewInfo: ReviewInfo = task.result
                        val flow: Task<Void> = manager.launchReviewFlow(activity, reviewInfo)
                        flow.addOnCompleteListener {
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown. Thus, no
                            // matter the result, we continue our app flow.

                        }
                    } else {
                        // There was some problem, continue regardless of the result.

                    }
                } catch (ex: Exception) {
                    Toast.makeText(activity, "Unknown error, Would you mind to open google play and give us a review?",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }




//        manager = ReviewManagerFactory.create(activity)
//
//        manager.requestReviewFlow()
//            .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(activity, "task is successful", Toast.LENGTH_LONG).show()
//                // We got the ReviewInfo object
//                val reviewInfo = task.result
//
//                val flow = manager.launchReviewFlow(activity, reviewInfo)
//
//
//                flow.addOnCompleteListener {}
//            }
//        }
    }


}