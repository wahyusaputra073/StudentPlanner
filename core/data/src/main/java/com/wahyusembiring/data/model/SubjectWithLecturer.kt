package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.model.entity.Subject

data class SubjectWithLecturer(
    @Embedded val subject: Subject,  // Menyematkan data Subject dalam objek ini.

    @Relation(
        parentColumn = "lecturer_id",  // Kolom 'lecturer_id' di tabel Subject yang menjadi referensi.
        entityColumn = "id"  // Kolom 'id' di tabel Lecturer yang menjadi foreign key.
    )
    val lecturer: Lecturer  // Menyimpan objek Lecturer yang terkait dengan mata kuliah (Subject).
)