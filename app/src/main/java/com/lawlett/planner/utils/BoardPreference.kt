package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class BoardPreference(context: Context) {
    private val preferences: SharedPreferences
    val isShown: Boolean
        get() = preferences.getBoolean("isShown", false)

    fun saveShown() {
        preferences.edit().putBoolean("isShown", true).apply()
    }

    companion object {
        @Volatile
        var instance: BoardPreference? = null
        fun getInstance(context: Context): BoardPreference? {
            if (instance == null) BoardPreference(context)
            return instance
        }
    }

    init {
        instance = this
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}
