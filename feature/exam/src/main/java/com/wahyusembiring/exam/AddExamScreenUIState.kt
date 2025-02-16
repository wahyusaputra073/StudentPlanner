package com.wahyusembiring.exam

import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.util.UIText
import java.util.Date

// Data class untuk menyimpan state UI dari ExamScreen
data class AddExamScreenUIState(
    // Menyimpan status apakah dalam mode edit atau tidak
    val isEditMode: Boolean = false,

    // Menyimpan nama ujian
    val name: String = "",

    // Menyimpan deskripsi ujian
    val description: String = "",

    // Menyimpan tanggal ujian (nullable karena bisa kosong)
    val date: Date? = null,

    // Menyimpan waktu ujian (nullable karena bisa kosong)
    val time: Time? = null,

    // Menyimpan waktu deadline ujian (nullable karena bisa kosong)
    val times: DeadlineTime? = null,

    // Menyimpan daftar mata pelajaran yang terkait dengan ujian
    val subjects: List<Subject> = emptyList(),

    // Menyimpan mata pelajaran yang dipilih
    val subject: Subject? = null,

    // Menyimpan nilai ujian (nullable karena bisa kosong)
    val score: Int? = null,

    // Menyimpan kategori ujian, default adalah WRITTEN
    val category: ExamCategory = ExamCategory.WRITTEN,

    // Menyimpan daftar lampiran ujian
    val attachments: List<Attachment> = emptyList(),

    // Popups dan dialog untuk mengatur tampilan UI
    val showDatePicker: Boolean = false, // Menampilkan date picker
    val showTimePicker: Boolean = false, // Menampilkan time picker
    val showDeadlineTimePicker: Boolean = false, // Menampilkan deadline time picker
    val showSubjectPicker: Boolean = false, // Menampilkan subject picker
    val showAttachmentPicker: Boolean = false, // Menampilkan attachment picker
    val showSaveConfirmationDialog: Boolean = false, // Menampilkan dialog konfirmasi simpan ujian
    val showExamSavedDialog: Boolean = false, // Menampilkan dialog ujian berhasil disimpan
    val showSavingLoading: Boolean = false, // Menampilkan dialog loading saat menyimpan ujian
    val showCategoryPicker: Boolean = false, // Menampilkan category picker
    val errorMessage: UIText? = null, // Menyimpan pesan error yang ditampilkan

    val emailAddress: String = "",
    val showEmailSentDialog: Boolean = false,
)
