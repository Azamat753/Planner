package com.lawlett.planner.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
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

fun Activity.checkedTheme() {
    when (ThemePreference.getInstance(this)?.getTheme) {
        0 -> {
            this.setTheme(R.style.AppTheme_Orange)
        }
        1 -> {
            this.setTheme(R.style.AppTheme_Green)
        }
    }
}