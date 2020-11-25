package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class ThemePreference(context: Context) {
    private val preferences: SharedPreferences

    val getLightTheme: Boolean
        get() = preferences.getBoolean("theme_mode", false)

    fun saveLightTrue() {
        preferences.edit().putBoolean("theme_mode", true).apply()
    }

    fun saveLightFalse() {
        preferences.edit().putBoolean("theme_mode", false).apply()
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
