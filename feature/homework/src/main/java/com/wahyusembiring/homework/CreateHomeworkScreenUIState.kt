package com.wahyusembiring.homework

import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.util.UIText
import java.util.Date

data class CreateHomeworkScreenUIState(
    val isEditMode: Boolean = false, // Menandakan apakah tampilan dalam mode edit atau tidak
    val homeworkTitle: String = "", // Judul tugas rumah
    val date: Date? = null, // Tanggal tugas rumah yang dipilih (nullable)
    val time: Time? = null, // Waktu pengingat yang dipilih (nullable)
    val times: DeadlineTime? = null, // Waktu deadline yang dipilih (nullable)
    val subjects: List<Subject> = emptyList(), // Daftar subjek yang tersedia
    val subject: Subject? = null, // Subjek yang dipilih (nullable)
    val attachments: List<Attachment> = emptyList(), // Daftar lampiran untuk tugas rumah
    val isCompleted: Boolean = false, // Menandakan apakah tugas rumah sudah selesai atau belum
    val description: String = "", // Deskripsi tugas rumah

    // Status popup dialog
    val showDatePicker: Boolean = false, // Menandakan apakah date picker ditampilkan
    val showTimePicker: Boolean = false, // Menandakan apakah time picker ditampilkan
    val showDeadlineTimePicker: Boolean = false, // Menandakan apakah deadline time picker ditampilkan
    val showSubjectPicker: Boolean = false, // Menandakan apakah subject picker ditampilkan
    val showAttachmentPicker: Boolean = false, // Menandakan apakah attachment picker ditampilkan
    val showSaveConfirmationDialog: Boolean = false, // Menandakan apakah dialog konfirmasi penyimpanan ditampilkan
    val showHomeworkSavedDialog: Boolean = false, // Menandakan apakah dialog tugas rumah berhasil disimpan ditampilkan
    val showSavingLoading: Boolean = false, // Menandakan apakah loading saat menyimpan tugas rumah ditampilkan
    val errorMessage: UIText? = null, // Pesan kesalahan jika ada (nullable)
)