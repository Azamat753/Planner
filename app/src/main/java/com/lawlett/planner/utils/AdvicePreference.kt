package com.lawlett.planner.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.*


class AdvicePreference(context: Context) {
    private val preferences: SharedPreferences
    private val calendar:Calendar = Calendar.getInstance()

     fun getAdvice(): Int {
        return preferences.getInt(Constants.CURRENT_DAY,0)
    }

    fun setAdvice(s: Int) {
        preferences.edit().putInt(Constants.CURRENT_DAY, s).apply()
    }

    companion object {
        @Volatile
        var instance: AdvicePreference? = null
        fun getInstance(context: Context): AdvicePreference? {
            if (instance == null) AdvicePreference(context)

            return instance
        }
    }

    init {
        instance = this
        preferences = context.getSharedPreferences("advice", Context.MODE_PRIVATE)
        val today = calendar.get(Calendar.DAY_OF_MONTH)
        if (today != getAdvice()){
            val random = Random()
            val advicePosition = random.nextInt(47)
            setAdvice(advicePosition)
        }
    }
}
