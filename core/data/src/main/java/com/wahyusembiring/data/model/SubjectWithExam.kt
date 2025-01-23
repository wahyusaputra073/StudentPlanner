package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.Subject

data class SubjectWithExam(
    @Embedded val subject: Subject,  // Menyematkan data Subject dalam objek ini.

    @Relation(
        parentColumn = "id",  // Kolom 'id' di tabel Subject yang menjadi referensi.
        entityColumn = "subject_id"  // Kolom 'subject_id' di tabel Exam yang menjadi foreign key.
    )
    val exams: List<Exam>  // Menyimpan daftar ujian (Exam) yang terkait dengan mata kuliah (Subject).
)
