package com.wahyusembiring.data.model.entity

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subject",  // Menandakan bahwa kelas Subject akan dipetakan ke tabel "subject" di dalam database Room.
    foreignKeys = [
        ForeignKey(  // Menentukan relasi antara tabel subject dan lecturer melalui foreign key.
            entity = Lecturer::class,  // Entitas Lecturer yang menjadi referensi.
            parentColumns = ["id"],  // Kolom "id" pada tabel Lecturer menjadi kolom induk (parent).
            childColumns = ["lecturer_id"],  // Kolom "lecturer_id" pada tabel Subject menjadi kolom anak (child).
            onDelete = ForeignKey.CASCADE,  // Jika entitas Lecturer dihapus, maka entitas Subject yang terkait juga akan dihapus.
            onUpdate = ForeignKey.CASCADE  // Jika entitas Lecturer diperbarui, maka entitas Subject yang terkait juga akan diperbarui.
        )
    ]
)
data class Subject(
    @PrimaryKey(autoGenerate = true)  // Menandakan bahwa "id" adalah primary key dan nilainya akan otomatis di-generate oleh Room.
    val id: Int = 0,  // ID unik untuk setiap mata pelajaran, defaultnya adalah 0.

    val name: String,  // Nama dari mata pelajaran.

    val color: Color,  // Warna yang terkait dengan mata pelajaran, disimpan sebagai objek Color.

    val room: String,  // Ruang tempat pelajaran tersebut dilaksanakan.

    @ColumnInfo("lecturer_id")  // Menentukan nama kolom yang akan digunakan untuk foreign key pada tabel Subject.
    val lecturerId: Int,  // ID dari dosen yang mengajar mata pelajaran ini, yang terhubung ke tabel Lecturer.

    val description: String,  // Deskripsi mata pelajaran untuk memberikan detail lebih lanjut.
)
