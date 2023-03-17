package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class IntPreference(context: Context) {
    private val preferences: SharedPreferences

    fun getInt(key: String): Int {
        return preferences.getInt(key,0)
    }

    fun saveInt(key: String, s: Int) {
        preferences.edit().putInt(key, s).apply()
    }

    companion object {
        @Volatile
        var instance: IntPreference? = null
        fun getInstance(context: Context): IntPreference? {
            if (instance == null) IntPreference(context)
            return instance
        }
    }

    init {
        instance = this
        preferences = context.getSharedPreferences("Int", Context.MODE_PRIVATE)
    }
}
