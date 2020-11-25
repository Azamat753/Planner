package com.lawlett.planner.extensions

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.airbnb.lottie.utils.Logger.error
import com.lawlett.planner.R
import com.squareup.picasso.Picasso

fun Context.toastShow(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(uri: Uri?) {
    Picasso.get().load(uri).centerCrop().resize(this.measuredWidth,this.measuredHeight)
        .error(R.drawable.ic_camera)
        .placeholder(R.drawable.ic_camera).into(this)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}