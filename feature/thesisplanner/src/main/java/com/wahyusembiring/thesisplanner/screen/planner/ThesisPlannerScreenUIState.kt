package com.wahyusembiring.thesisplanner.screen.planner

import com.wahyusembiring.data.model.File
import com.wahyusembiring.data.model.ThesisWithTask
import com.wahyusembiring.data.model.entity.TaskThesis

data class ThesisPlannerScreenUIState( // Menyimpan state untuk layar ThesisPlanner
    val thesis: ThesisWithTask? = null, // Menyimpan data tesis dengan tugas yang terkait, nullable
    val thesisTitle: String = "", // Menyimpan judul tesis
    val editedThesisTitle: String = "", // Menyimpan perubahan judul tesis sebelum disimpan
    val articles: List<File> = emptyList(), // Menyimpan daftar artikel yang terhubung dengan tesis
    val taskTheses: List<TaskThesis> = emptyList(), // Menyimpan daftar tugas yang terkait dengan tesis

    // Popup states
    val showDatePicker: Boolean = false, // Menandakan apakah date picker ditampilkan
    val showCreateTaskDialog: Boolean = false, // Menandakan apakah dialog pembuatan tugas ditampilkan
    val articlePendingDelete: File? = null, // Menyimpan artikel yang sedang menunggu konfirmasi penghapusan, nullable
    val taskThesisPendingDelete: TaskThesis? = null, // Menyimpan tugas yang sedang menunggu konfirmasi penghapusan, nullable
)