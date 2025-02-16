package com.wahyusembiring.agenda

import androidx.compose.ui.graphics.Color
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.theme.primaryLight
import com.wahyusembiring.ui.util.UIText
import java.util.Date

data class AddAgendaScreenUIState(
    // Menentukan apakah layar dalam mode edit atau mode tambah pengingat baru
    val isEditMode: Boolean = false,

    // Menyimpan judul pengingat
    val title: String = "",

    // Menyimpan tanggal yang dipilih untuk pengingat
    val date: Date? = null,

    // Menyimpan waktu yang dipilih untuk pengingat
    val time: Time? = null,

    val startTime: Time? = null,

    // Menyimpan durasi waktu yang dipilih untuk pengingat
    val spanTime: SpanTime? = null,

    // Menyimpan warna yang dipilih untuk pengingat
    val color: Color = primaryLight,

    // Menentukan apakah pengingat sudah selesai atau belum
    val isCompleted: Boolean = false,

    // Menyimpan daftar lampiran yang ditambahkan ke pengingat
    val attachments: List<Attachment> = emptyList(),

    // Menyimpan deskripsi pengingat
    val description: String = "",

    // Kontrol tampilan popup
    val showDatePicker: Boolean = false, // Menentukan apakah DatePicker ditampilkan
    val showTimePicker: Boolean = false, // Menentukan apakah TimePicker ditampilkan
    val showDuraPicker: Boolean = false, // Menentukan apakah DurationPicker ditampilkan
    val showColorPicker: Boolean = false, // Menentukan apakah ColorPicker ditampilkan
    val showAttachmentPicker: Boolean = false, // Menentukan apakah AttachmentPicker ditampilkan
    val showSaveConfirmationDialog: Boolean = false, // Menentukan apakah dialog konfirmasi penyimpanan ditampilkan
    val showSavingLoading: Boolean = false, // Menentukan apakah indikator proses penyimpanan ditampilkan
    val showReminderSavedDialog: Boolean = false, // Menentukan apakah dialog notifikasi pengingat tersimpan ditampilkan

    // Menyimpan pesan error jika terjadi kesalahan
    val errorMessage: UIText? = null,

    val emailAddress: String = "",
    val showEmailSentDialog: Boolean = false,
)