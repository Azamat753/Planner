package com.lawlett.planner.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.lawlett.planner.utils.Const.Constants.CHANNEL_ID
import com.lawlett.planner.utils.Const.Constants.CHANNEL_ID_HOURS
import java.util.*

public class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Example Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val channel2 = NotificationChannel(
                CHANNEL_ID_HOURS, "Example Channel2",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            val notificationChannels: MutableList<NotificationChannel> = ArrayList()
            notificationChannels.add(channel)
            notificationChannels.add(channel2)
            manager.createNotificationChannels(notificationChannels)
        }
    }}
