package com.wahyusembiring.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import java.util.Date

@Entity(  // Menandai kelas ini sebagai entitas untuk Room Database
    tableName = "homework",  // Nama tabel dalam database
    foreignKeys = [  // Menambahkan foreign key untuk hubungan antar tabel
        ForeignKey(
            entity = Subject::class,  // Entitas yang dijadikan referensi (Subject)
            parentColumns = ["id"],  // Kolom di entitas Subject yang digunakan sebagai referensi
            childColumns = ["subject_id"],  // Kolom di entitas Homework yang merujuk ke kolom parent
            onDelete = ForeignKey.CASCADE,  // Hapus data di Homework jika data Subject terkait dihapus
            onUpdate = ForeignKey.CASCADE  // Perbarui data di Homework jika data Subject terkait diperbarui
        )
    ]
)
data class Homework(  // Mendefinisikan data class untuk entitas Homework
    @PrimaryKey(autoGenerate = true)  // Menandai kolom id sebagai primary key yang otomatis di-generate
    val id: Int = 0,

    val title: String,  // Judul tugas rumah

    @ColumnInfo(name = "due_date")  // Menandakan bahwa kolom ini berhubungan dengan due_date
    val dueDate: Date,  // Tanggal batas tugas rumah

    val reminder: Time?,  // Waktu pengingat (opsional)

    val deadline: DeadlineTime?,  // Waktu batas (opsional)

    @ColumnInfo(name = "subject_id")  // Menandakan bahwa kolom ini berhubungan dengan subject_id
    val subjectId: Int,  // ID subjek yang terkait dengan tugas rumah

    val completed: Boolean = false,  // Status apakah tugas rumah sudah diselesaikan

    val attachments: List<Attachment>,  // Daftar lampiran terkait tugas rumah

    val description: String,  // Deskripsi tugas rumah

    val score: Int? = null  // Nilai tugas rumah (opsional)
)
