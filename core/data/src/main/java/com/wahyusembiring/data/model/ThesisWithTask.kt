package com.wahyusembiring.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.data.model.entity.Thesis

data class ThesisWithTask(
    @Embedded val thesis: Thesis,  // Menyematkan objek Thesis dalam objek ThesisWithTask. Ini berarti semua informasi yang ada di dalam entitas Thesis akan dibawa ke dalam objek ThesisWithTask, seperti title dan articles.

    @Relation(
        parentColumn = "id",  // Kolom 'id' pada tabel Thesis yang akan berfungsi sebagai foreign key. Kolom ini menjadi acuan untuk mencari data terkait di tabel Task.
        entityColumn = "thesis_id"  // Kolom 'thesis_id' pada tabel Task yang menjadi referensi ke kolom 'id' di tabel Thesis.
    )
    val tasks: List<Task>  // Menghubungkan objek Thesis dengan beberapa entitas Task yang terkait. Di sini, kita mendapatkan daftar (list) tugas yang terkait dengan thesis tertentu.
)
