package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.model.entity.Task

// Data class HomeworkWithSubject digunakan untuk menghubungkan objek Homework dengan objek Subject.
// Ini mirip dengan ExamWithSubject, di mana kita menyambungkan dua entitas, Homework dan Subject, dalam satu objek.
data class HomeworkWithSubject(
    // Properti homework bertipe Homework dan menggunakan anotasi @Embedded
    // yang menandakan bahwa data dari kelas Homework akan disematkan di dalam kelas HomeworkWithSubject.
    @Embedded val homework: Task,  // Menyimpan data homework, dengan kolom-kolom yang ada dalam tabel Homework.

    // Properti subject bertipe Subject dan menggunakan anotasi @Relation
    // untuk mendefinisikan hubungan antara homework dan subject.
    @Relation(
        parentColumn = "subject_id",  // Kolom di tabel Homework yang berfungsi sebagai foreign key untuk menghubungkan dengan tabel Subject.
        entityColumn = "id"  // Kolom di tabel Subject yang menjadi target untuk foreign key.
    )
    val subject: Subject  // Menyimpan data subject yang terkait dengan homework melalui relasi ini.
)

