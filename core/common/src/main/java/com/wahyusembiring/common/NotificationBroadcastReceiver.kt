package com.wahyusembiring.common

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return

        val id = intent.getIntExtra(NOTIFICATION_ID_EXTRA, 1)
        val title = intent.getStringExtra(NOTIFICATION_TITLE_EXTRA)
        val description = intent.getStringExtra(NOTIFICATION_DESCRIPTION_EXTRA)
        val duration = intent.getStringExtra(NOTIFICATION_DURATION_EXTRA)

        val notificationStyle = NotificationCompat.BigTextStyle()
            .setBigContentTitle(title)
            .bigText("$duration\n$description")

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle(title)
            .setStyle(notificationStyle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(id, notification)
    }
}