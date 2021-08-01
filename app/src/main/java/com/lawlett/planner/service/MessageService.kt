package com.lawlett.planner.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lawlett.planner.R
import com.lawlett.planner.ui.main.MainActivity
import com.lawlett.planner.utils.Constants

class MessageService : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val title: String? = p1?.getStringExtra(Constants.TITLE)
        val text: String? = p1?.getStringExtra(Constants.TEXT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = p0?.let { NotificationManagerCompat.from(it) }
            if (notificationManager?.getNotificationChannel(Constants.CHANNEL_ID_HOURS) == null) {
                notificationManager?.createNotificationChannel(
                    NotificationChannel(
                        Constants.CHANNEL_ID_HOURS,
                        "Whatever",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }
            val intent1 = Intent(p0, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(p0, 2, intent1, PendingIntent.FLAG_ONE_SHOT)
            val notification = p0?.let {
                NotificationCompat.Builder(it, Constants.CHANNEL_ID_HOURS)
                    .setSmallIcon(R.mipmap.ic_planner)
                    .setContentTitle(title).setContentText(text)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .build()
            }
            if (notification != null) {
                notificationManager?.notify(2, notification)
            }
        } else {
            val intent1 = Intent(p0, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(p0, 2, intent1, PendingIntent.FLAG_ONE_SHOT)
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val builder = p0?.let {
                NotificationCompat.Builder(it)
                    .setSmallIcon(R.mipmap.ic_planner)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setContentIntent(pendingIntent)
            }
            val notification = builder?.build()

            val notificationManager =
                p0?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2, notification)
        }
    }
}