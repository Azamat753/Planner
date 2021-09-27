package com.lawlett.planner.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import com.lawlett.planner.R
import com.lawlett.planner.data.room.models.LanguageModel
import com.lawlett.planner.utils.LanguagePreference
import com.lawlett.planner.utils.ThemePreference
import java.util.*

fun Activity.changeLanguage(position: Int) {
    when (position) {
        0 -> {
            setLocale("ru", this)
        }
        1 -> {
            setLocale("be", this)
        }
        2 -> {
            setLocale("uk", this)
        }
        3 -> {
            setLocale("en", this)
        }
        4 -> {
            setLocale("de", this)
        }
        5 -> {
            setLocale("pt", this)
        }
        6 -> {
            setLocale("fr", this)
        }
        7 -> {
            setLocale("it", this)
        }
        8 -> {
            setLocale("es", this)
        }
        9 -> {
            setLocale("ky", this)
        }
        10 -> {
            setLocale("kk", this)
        }
        11 -> {
            setLocale("tr", this)
        }
        12 -> {
            setLocale("ko", this)
        }
        13 -> {
            setLocale("zh", this)
        }
        14 -> {
            setLocale("ja", this)
        }
        15 -> {
            setLocale("hi", this)
        }
    }
    this.recreate()
}

fun getLanguageList(): List<LanguageModel> {
    val listModel: ArrayList<LanguageModel> = arrayListOf()
    listModel.add(LanguageModel("Русский"))
    listModel.add(LanguageModel("Беларускі"))
    listModel.add(LanguageModel("Український"))
    listModel.add(LanguageModel("English"))
    listModel.add(LanguageModel("Deutsche"))
    listModel.add(LanguageModel("Português"))
    listModel.add(LanguageModel("Français"))
    listModel.add(LanguageModel("Italiano"))
    listModel.add(LanguageModel("Español"))
    listModel.add(LanguageModel("Кыргызча"))
    listModel.add(LanguageModel("Қазақ тілі"))
    listModel.add(LanguageModel("Türk"))
    listModel.add(LanguageModel("한국어"))
    listModel.add(LanguageModel("中文"))
    listModel.add(LanguageModel("日本語"))
    listModel.add(LanguageModel("हिंदी"))
    return listModel
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
    val language: String? = LanguagePreference.getInstance(context)?.getLanguage
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
        getString(R.string.pink) -> {
            this.setTheme(R.style.AppTheme_Pink)
        }
        getString(R.string.violet) -> {
            this.setTheme(R.style.AppTheme_Violet)
        }
        getString(R.string.heavenly)->{
            this.setTheme(R.style.AppTheme_LightBlue)
        }
        "Тёмно-Синий"->{
            this.setTheme(R.style.AppTheme_DarkBlue)
        }
    }
}