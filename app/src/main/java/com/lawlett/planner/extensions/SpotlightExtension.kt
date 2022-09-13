package com.lawlett.planner.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.PointF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.lawlett.planner.R
import com.lawlett.planner.utils.BooleanPreference
import com.lawlett.planner.utils.Constants
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.RippleEffect
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun setSpotLightTarget(
    targetView: View,
    backLayoutView: View,
    description: String,
    counterPair: Pair<Int, Int>
): Target {
    val target = Target.Builder()
        .setAnchor(targetView)
        .setShape(
            RoundedRectangle(
                targetView.height.toFloat() + 20,
                targetView.width.toFloat() + 20,
                30F
            )
        )
        .setOverlay(backLayoutView)
        .setOnTargetListener(object : OnTargetListener {
            val descriptionView = backLayoutView.findViewById<TextView>(R.id.textTarget)

            @SuppressLint("SetTextI18n")
            override fun onStarted() {
                if (descriptionView != null) {

                    descriptionView.text =
                        description.removeBrackets()
                    backLayoutView.findViewById<TextView>(R.id.counterTv).text =
                        "${counterPair.first}/${counterPair.second}"
                }
            }

            override fun onEnded() {}
        })
        .build()
    return target
}

fun Activity.showSpotlight(scope: CoroutineScope, vararg map: Map<View, String>) {
    if (BooleanPreference.getInstance(this)
            ?.getBooleanData(Constants.SKIP_ALL_INSTRUCTION) == false
    ) {
        scope.launch {
            delay(1000)
            val mapList = ArrayList<Map<View, String>>()
            map.forEach {
                mapList.add(it)
            }
            setSpotLightBuilder(
                this@showSpotlight,
                mapList
            )
        }
    }
}

private fun setSpotLightBuilder(
    activity: Activity,
    targets: ArrayList<Map<View, String>>,
) {
    val root = FrameLayout(activity)
    val layout = activity.layoutInflater.inflate(R.layout.layout_target, root)
    val targetList: ArrayList<Target> = arrayListOf()
    val viewsSize = targets.size
    targets.forEachIndexed { index, target ->
        targetList.add(
            setSpotLightTarget(
                target.keys.first(),
                layout,
                target.values.toString(),
                counterPair = Pair(index + 1, viewsSize)
            )
        )
    }

    targetList.let {
        val spotlight = Spotlight.Builder(activity)
            .setTargets(it)
            .setDuration(1000L)
            .setAnimation(DecelerateInterpolator(14f))
            .setOnSpotlightListener(object : OnSpotlightListener {
                override fun onStarted() {}
                override fun onEnded() {}
            }).build()
        spotlight.start()
        layout.findViewById<TextView>(R.id.next).setOnClickListener { spotlight.next() }
        layout.findViewById<TextView>(R.id.skipButton).setOnClickListener {
            BooleanPreference.getInstance(activity)
                ?.saveBooleanData(Constants.SKIP_ALL_INSTRUCTION, true)
            spotlight.finish()
        }
    }
}