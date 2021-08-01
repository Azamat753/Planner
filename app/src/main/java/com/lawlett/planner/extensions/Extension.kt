package com.lawlett.planner.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.lawlett.planner.R


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImage(uri: Uri?) {
    Glide.with(this.context).load(uri).placeholder(R.drawable.ic_choose_image).into(this)
}

fun ImageView.loadImage(uri: Int?) {
    Glide.with(this.context).load(uri).placeholder(R.drawable.ic_choose_image).into(this)
}

fun ImageView.loadImage(uri: String) {
    Glide.with(this.context).load(uri).placeholder(R.drawable.ic_choose_image).into(this)
}

fun ImageView.loadImage(drawable: Drawable?) {
    Glide.with(this.context).load(drawable).placeholder(R.drawable.ic_choose_image).into(this)
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

fun EditText.clearField() {
    this.text.clear()
}