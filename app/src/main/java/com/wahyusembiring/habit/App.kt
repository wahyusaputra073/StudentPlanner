package com.wahyusembiring.habit

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.wahyusembiring.common.NOTIFICATION_CHANNEL_DESCRIPTION
import com.wahyusembiring.common.NOTIFICATION_CHANNEL_ID
import com.wahyusembiring.common.NOTIFICATION_CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp

// Mengindikasikan bahwa aplikasi menggunakan Hilt sebagai dependency injection framework
@HiltAndroidApp
class App : Application() {

    // Fungsi onCreate() dipanggil ketika aplikasi pertama kali dibuat
    override fun onCreate() {
        super.onCreate()

        // Mengecek apakah versi Android perangkat setidaknya adalah Oreo (API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Membuat sebuah NotificationChannel baru
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,         // ID unik untuk channel notifikasi
                NOTIFICATION_CHANNEL_NAME,       // Nama yang ditampilkan untuk channel
                NotificationManager.IMPORTANCE_HIGH // Level penting untuk notifikasi
            ).apply {
                description = NOTIFICATION_CHANNEL_DESCRIPTION // Deskripsi channel
            }

            // Mendapatkan instance NotificationManager dari sistem
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Mendaftarkan NotificationChannel ke sistem
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
