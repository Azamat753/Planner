package com.lawlett.planner.extensions

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.lawlett.planner.R
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.RoundedRectangle

fun setSpotLightTarget(targetView: View, backLayoutView: View,targets: ArrayList<com.takusemba.spotlight.Target>, discription: String ){
    android.os.Handler().postDelayed({
        val target1 = com.takusemba.spotlight.Target.Builder()
            .setAnchor(targetView)
            .setShape(RoundedRectangle(targetView.height.toFloat(), targetView.width.toFloat(), 30F))
            .setEffect(RippleEffect(100f, 200f, Color.argb(30, 124, 255, 90)))
            .setOverlay(backLayoutView)
            .setOnTargetListener(object : OnTargetListener {
                override fun onStarted() {
                    backLayoutView.findViewById<TextView>(R.id.text_target).text = discription
                }

                override fun onEnded() {

                }
            })
            .build()
        targets.add(target1)
    }, 1000)
}


fun setSpotLightBuilder(activity: Activity, targets: ArrayList<Target>, backLayoutView: View){
    android.os.Handler().postDelayed({
        val spotlight = Spotlight.Builder(activity)
            .setTargets(targets)
            .setBackgroundColor(R.color.background)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(2f))
            .setOnSpotlightListener(object : OnSpotlightListener {
                override fun onStarted() {

                }

                override fun onEnded() {

                }
            })
            .build()

        spotlight.start()

        backLayoutView.findViewById<TextView>(R.id.next).setOnClickListener { spotlight.next() }
//        backLayoutView.findViewById<TextView>(R.id.finish).setOnClickListener { spotlight.finish() }

    }, 1000)
}

fun changeBubbleView(parentLayout: ConstraintLayout, firstView: Int, secondView: Int){
    val parent = parentLayout
    val mConstraintSet = ConstraintSet()
    mConstraintSet.clone(parent)
//    mConstraintSet.clear(R.id.bubbleTriangle, ConstraintSet.BOTTOM)
//    mConstraintSet.clear(R.id.bubbleTriangle, ConstraintSet.END)
//    mConstraintSet.clear(R.id.bubbleTriangle, ConstraintSet.START)
    mConstraintSet.connect(firstView, ConstraintSet.BOTTOM,
        secondView, ConstraintSet.TOP)
    mConstraintSet.connect(firstView, ConstraintSet.START,
        secondView, ConstraintSet.START)
    mConstraintSet.connect(firstView, ConstraintSet.END,
        secondView, ConstraintSet.END)
    mConstraintSet.applyTo(parent)
}