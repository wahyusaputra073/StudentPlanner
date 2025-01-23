package com.wahyusembiring.common

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationBroadcastReceiver : BroadcastReceiver() {  // Receiver untuk menangani penerimaan notifikasi

    override fun onReceive(context: Context?, intent: Intent?) {  // Fungsi yang dipanggil saat notifikasi diterima
        if (context == null) return  // Memastikan context tidak null
        if (intent == null) return  // Memastikan intent tidak null

        val id = intent.getIntExtra(NOTIFICATION_ID_EXTRA, 1)  // Mendapatkan ID notifikasi dari intent, default 1
        val title = intent.getStringExtra(NOTIFICATION_TITLE_EXTRA)  // Mendapatkan judul notifikasi dari intent

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)  // Membuat notifikasi
            .setSmallIcon(R.drawable.app_icon)  // Menetapkan ikon kecil untuk notifikasi
            .setContentTitle(title)  // Menetapkan judul notifikasi
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Menetapkan prioritas notifikasi
            .build()  // Membangun objek notifikasi

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager  // Mendapatkan sistem manajer notifikasi

        notificationManager.notify(id, notification)  // Menampilkan notifikasi menggunakan ID yang diberikan
    }
}