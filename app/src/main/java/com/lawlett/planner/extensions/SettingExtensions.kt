package com.lawlett.planner.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import com.lawlett.planner.R
import com.lawlett.planner.utils.LanguagePreference
import com.lawlett.planner.utils.ThemePreference
import java.util.*

fun Activity.changeLanguage() {
    val listItems = arrayOf("English", "Кыргызский", "Русский")
    val mBuilder = AlertDialog.Builder(this)
    mBuilder.setTitle(R.string.choose_language)
    mBuilder.setSingleChoiceItems(listItems, -1) { dialog, which ->
        when (which) {
            0 -> {
                setLocale("en", this)
            }
            1 -> {
                setLocale("ky", this)
            }
            2 -> {
                setLocale("ru", this)
            }
        }
        this.recreate()
        dialog.dismiss()
    }
    val mDialog = mBuilder.create()
    mDialog.show()
}

private fun setLocale(s: String, context: Context) {
    val locale = Locale(s)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    context.resources.updateConfiguration(
        config,
        context.resources.displayMetrics
    )
    LanguagePreference.getInstance(context)?.saveLanguage(s)
}

fun loadLocale(context: Context) {
    var language: String? = LanguagePreference.getInstance(context)?.getLanguage
    if (language != null) {
        setLocale(language, context)
    }
}

fun Context.checkedTheme() {
    when (ThemePreference.getInstance(this)?.getTheme.toString()) {
        getString(R.string.blue) -> {
            this.setTheme(R.style.AppTheme_BlueLight)
        }
        getString(R.string.green) -> {
            this.setTheme(R.style.AppTheme_GreenLight)
        }
        getString(R.string.red) -> {
            this.setTheme(R.style.AppTheme_Red)
        }
        getString(R.string.black) -> {
            this.setTheme(R.style.AppTheme_Black)
        }
        getString(R.string.yellow) -> {
            this.setTheme(R.style.AppTheme_Yellow)
        }
        getString(R.string.orange) -> {
            this.setTheme(R.style.AppTheme_Orange)
        }
        getString(R.string.white) -> {
            this.setTheme(R.style.AppTheme_White)
        }
    }
}