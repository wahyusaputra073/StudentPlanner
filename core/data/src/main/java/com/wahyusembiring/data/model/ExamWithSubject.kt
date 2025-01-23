package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.Subject

// Data class ExamWithSubject digunakan untuk menghubungkan objek Exam dengan objek Subject.
// Ini adalah cara untuk menangani hubungan antar entitas dalam Room Database.
data class ExamWithSubject(
    // Properti exam bertipe Exam dan menggunakan anotasi @Embedded
    // yang menandakan bahwa data dari kelas Exam akan disematkan di dalam kelas ExamWithSubject.
    @Embedded val exam: Exam,  // Menyimpan data exam, dengan kolom-kolom yang ada dalam tabel Exam.

    // Properti subject bertipe Subject dan menggunakan anotasi @Relation
    // untuk mendefinisikan hubungan antara exam dan subject.
    @Relation(
        parentColumn = "subject_id",  // Kolom di tabel Exam yang berfungsi sebagai foreign key untuk menghubungkan dengan tabel Subject.
        entityColumn = "id"  // Kolom di tabel Subject yang menjadi target untuk foreign key.
    )
    val subject: Subject  // Menyimpan data subject yang terkait dengan exam melalui relasi ini.
)

