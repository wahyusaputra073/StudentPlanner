package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.model.entity.Subject

data class LecturerWithSubject(
    @Embedded val lecturer: Lecturer,  // Menyematkan data dosen dalam objek ini.

    @Relation(
        parentColumn = "id",  // Kolom id di tabel Lecturer sebagai referensi.
        entityColumn = "lecturer_id"  // Kolom lecturer_id di tabel Subject sebagai foreign key.
    )
    val subjects: List<Subject>  // Menyimpan daftar mata kuliah yang diajarkan oleh dosen.
)


