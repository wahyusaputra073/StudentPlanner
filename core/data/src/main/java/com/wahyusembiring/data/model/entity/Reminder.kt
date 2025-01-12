package com.wahyusembiring.data.model.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.OfficeHour
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import java.util.Date


@Entity(tableName = "reminder")  // Menandakan bahwa class Reminder akan digunakan untuk tabel "reminder" di Room Database.
data class Reminder(
    @PrimaryKey(autoGenerate = true)  // Menandakan bahwa id akan menjadi primary key, dan akan di-generate secara otomatis.
    val id: Int = 0,  // ID unik untuk setiap reminder, dengan nilai default 0.

    val title: String,  // Judul dari reminder.

    val date: Date,  // Tanggal dari reminder, disimpan dalam format Date.

    val duration: SpanTime,  // Durasi pengingat, disimpan sebagai objek SpanTime (mungkin berisi jam dan menit).

    val time: Time,  // Waktu pengingat, disimpan sebagai objek Time yang berisi jam dan menit.

    val color: Color,  // Warna pengingat, disimpan sebagai objek Color (biasanya digunakan untuk menandai pengingat).

    val completed: Boolean = false,  // Status apakah pengingat sudah selesai atau belum, dengan nilai default false.

    val attachments: List<Attachment>,  // Daftar lampiran yang dapat disertakan pada pengingat (seperti file atau URI).

    val description: String,  // Deskripsi pengingat yang dapat memberikan detail lebih lanjut mengenai pengingat.
)
