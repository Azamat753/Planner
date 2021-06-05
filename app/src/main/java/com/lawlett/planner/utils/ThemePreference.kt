package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class ThemePreference(context: Context) {

    private val preferences: SharedPreferences

    val getTheme: Int
        get() = preferences.getInt("theme", 0)

    fun saveTheme(theme:Int) {
        preferences.edit().putInt("theme", theme).apply()
    }

    companion object {
        @Volatile
        var instance: ThemePreference? = null
        fun getInstance(context: Context): ThemePreference? {
            if (instance == null) ThemePreference(context)
            return instance
        }
    }

    init {
        instance = this
        preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE)
    }
}
