package com.wahyusembiring.common.util

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.NOTIFICATION_DESCRIPTION_EXTRA
import com.wahyusembiring.common.NOTIFICATION_DURATION_EXTRA
import com.wahyusembiring.common.NOTIFICATION_ID_EXTRA
import com.wahyusembiring.common.NOTIFICATION_TITLE_EXTRA
import com.wahyusembiring.common.NotificationBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KMutableProperty0


/**
 * Create a single instance of an object and store it in [volatileInstanceProp] property.
 *
 * @param volatileInstanceProp property to store the created instance
 * @param initializer a lambda function to create the instance
 *
 * @return [T] the created instance or the existing instance if it already exists
 * */


fun <T> createSingleton(
    volatileInstanceProp: KMutableProperty0<T?>,  // Properti mutable yang berfungsi sebagai tempat menyimpan instansi tunggal
    initializer: () -> T  // Fungsi untuk menginisialisasi objek jika belum ada
): T {
    return volatileInstanceProp.get() ?: synchronized(Any()) {  // Memastikan hanya ada satu instansi dengan synchronisasi
        volatileInstanceProp.get() ?: initializer().also {  // Jika instansi belum ada, inisialisasi dan simpan
            volatileInstanceProp.set(it)  // Menyimpan instansi yang baru dibuat
        }
    }
}

fun String?.isNotNullOrBlank(): Boolean = this?.isNotBlank() ?: false  // Mengecek apakah string tidak null dan tidak kosong (blank)

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {  // Meluncurkan Coroutine dalam ViewModel scope
    return viewModelScope.launch(block = block)  // Menjalankan block kode dalam scope ViewModel
}

fun Int.withZeroPadding(length: Int = 2): String = this.toString().padStart(length, '0')  // Memformat angka menjadi string dengan padding nol di kiri

fun getNotificationReminderPermission(): List<String> {  // Mengembalikan daftar izin yang dibutuhkan untuk pengingat notifikasi
    val permissions = mutableListOf<String>()

    // Android 33 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)  // Menambahkan izin untuk notifikasi
    }

    // Android 31 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
        permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM)  // Menambahkan izin untuk alarm yang tepat
    }

    return permissions  // Mengembalikan daftar izin
}

fun scheduleReminder(
    context: Context,
    localDateTime: LocalDateTime,
    title: String,
    description: String,
    duration: String,  // Add duration parameter
    reminderId: Int
) {
    val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
        putExtra(NOTIFICATION_ID_EXTRA, reminderId)
        putExtra(NOTIFICATION_TITLE_EXTRA, title)
        putExtra(NOTIFICATION_DESCRIPTION_EXTRA, description)
        putExtra(NOTIFICATION_DURATION_EXTRA, duration)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        reminderId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE  // Membuat PendingIntent untuk notifikasi
    )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,  // Mengonversi LocalDateTime ke epoch time dalam milidetik
        pendingIntent  // Mengatur alarm dengan PendingIntent yang sudah dibuat
    )
}

@Composable
fun <T> CollectAsOneTimeEvent(eventFlow: Flow<T>, onEvent: suspend (event: T) -> Unit) {  // Mengumpulkan event dari Flow dan menanganinya satu kali
    val lifecycleOwner = LocalLifecycleOwner.current  // Mendapatkan LifecycleOwner untuk komponen yang sedang aktif
    LaunchedEffect(eventFlow, lifecycleOwner) {  // Meluncurkan efek berdasarkan lifecycle dan eventFlow
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {  // Menunggu event sampai lifecycle berada dalam state STARTED
            eventFlow.collectLatest(onEvent)  // Mengumpulkan dan menangani event terbaru
        }
    }
}
