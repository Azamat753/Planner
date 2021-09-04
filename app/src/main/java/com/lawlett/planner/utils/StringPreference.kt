package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences


class StringPreference(context: Context) {
    private val preferences: SharedPreferences

//    val getLanguage: String?
//        get() = preferences.getString("language_", "")

    fun getProfile(key: String): String? {
        return preferences.getString(key,"")
    }

    fun saveProfile(key: String, s: String) {
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
