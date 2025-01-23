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
    tableName = "exam",  // Nama tabel dalam database
    foreignKeys = [  // Menambahkan foreign key untuk hubungan antar tabel
        ForeignKey(
            entity = Subject::class,  // Entitas yang dijadikan referensi (Subject)
            parentColumns = ["id"],  // Kolom di entitas Subject yang digunakan sebagai referensi
            childColumns = ["subject_id"],  // Kolom di entitas Exam yang merujuk ke kolom parent
            onDelete = ForeignKey.CASCADE,  // Hapus data di Exam jika data Subject terkait dihapus
            onUpdate = ForeignKey.CASCADE  // Perbarui data di Exam jika data Subject terkait diperbarui
        )
    ]
)

data class Exam(  // Mendefinisikan data class untuk entitas Exam
    @PrimaryKey(autoGenerate = true)  // Menandai kolom id sebagai primary key yang otomatis di-generate
    val id: Int = 0,

    val title: String,  // Judul ujian

    val date: Date,  // Tanggal ujian

    val reminder: Time?,  // Waktu pengingat (opsional)

    val deadline: DeadlineTime?,  // Waktu batas (opsional)

    @ColumnInfo(name = "subject_id")  // Menandakan bahwa kolom ini berhubungan dengan subject_id
    val subjectId: Int,  // ID subjek yang terkait dengan ujian

    val category: ExamCategory,  // Kategori ujian (Written, Oral, Practical)

    val score: Int? = null,  // Nilai ujian (opsional)

    val attachments: List<Attachment>,  // Daftar lampiran terkait ujian

    val description: String  // Deskripsi ujian
)

enum class ExamCategory {  // Enum untuk kategori ujian
    WRITTEN,  // Ujian tertulis
    ORAL,  // Ujian lisan
    PRACTICAL  // Ujian praktik
}
