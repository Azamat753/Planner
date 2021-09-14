package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class StringPreference(context: Context) {
    private val preferences: SharedPreferences

    fun getStringData(key: String): String? {
        return preferences.getString(key,"")
    }

    fun saveStringData(key: String, s: String) {
        preferences.edit().putString(key, s).apply()
    }

    companion object {
        @Volatile
        var instance: StringPreference? = null
        fun getInstance(context: Context): StringPreference? {
            if (instance == null) StringPreference(context)
            return instance
        }
    }

    init {
        instance = this
        preferences = context.getSharedPreferences("profile", Context.MODE_PRIVATE)
    }
}
