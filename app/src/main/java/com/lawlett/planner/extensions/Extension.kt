package com.lawlett.planner.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.lawlett.planner.R
import com.lawlett.planner.ui.activity.MainActivity
import tyrantgit.explosionfield.ExplosionField
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


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

fun View.explosionView(explosionField: ExplosionField) {
    explosionField.explode(this)
}

fun getTodayDate(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    return sdf.format(Date()).toString()
}

fun theMonth(month: Int, context: Context): String {
    val monthNames = arrayOf(
        context.getString(R.string.january),
        context.getString(R.string.february),
        context.getString(R.string.march),
        context.getString(R.string.april),
        context.getString(R.string.may),
        context.getString(R.string.june),
        context.getString(R.string.july),
        context.getString(R.string.august),
        context.getString(R.string.september),
        context.getString(R.string.october),
        context.getString(R.string.november),
        context.getString(R.string.december)
    )
    return monthNames[month]
}

fun Double.toDecimal(): String {
    return DecimalFormat("##.#").format(this)
}

fun Context.getDialog(layout: Int): Dialog {
    val inflater: LayoutInflater = LayoutInflater.from(this)
    val view: View = inflater.inflate(layout, null)
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(view)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return dialog
}

fun Activity.getCurrentLevel(): Int? {
    val activity: MainActivity? = this as MainActivity?
    return activity?.getCurrentLevelFromActivity()
}

