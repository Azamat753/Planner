package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class BooleanPreference(context: Context) {
    private val preferences: SharedPreferences

    fun getBooleanData(key: String): Boolean {
        return preferences.getBoolean(key,false)
    }

    fun saveBooleanData(key: String, s: Boolean) {
        preferences.edit().putBoolean(key, s).apply()
    }

    companion object {
        @Volatile
        var instance: BooleanPreference? = null
        fun getInstance(context: Context): BooleanPreference? {
            if (instance == null) BooleanPreference(context)
            return instance
        }
    }

    init {
        instance = this
        preferences = context.getSharedPreferences("Boolean", Context.MODE_PRIVATE)
    }
}
