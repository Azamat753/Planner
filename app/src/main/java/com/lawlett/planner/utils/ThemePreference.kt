package com.lawlett.planner.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.lawlett.planner.R


class ThemePreference(var context: Context) {

    private val preferences: SharedPreferences

    val getTheme: String?
        get() {
            return preferences.getString("theme",context.getString(R.string.blue))
        }

    fun saveTheme(theme:String) {
        preferences.edit().putString("theme", theme).apply()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
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
