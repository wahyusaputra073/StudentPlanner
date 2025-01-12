package com.wahyusembiring.homework

import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Subject
import java.util.Date

// Sealed class yang digunakan untuk mengelompokkan berbagai jenis event UI yang terjadi di tampilan pembuatan tugas
sealed class CreateHomeworkUIEvent {

    // Event yang dipicu ketika judul tugas diubah
    data class OnHomeworkTitleChanged(val title: String) : CreateHomeworkUIEvent()

    // Event yang dipicu ketika deskripsi ujian diubah
    data class OnExamDescriptionChanged(val title: String) : CreateHomeworkUIEvent()

    // Event yang dipicu ketika tombol simpan tugas diklik
    data object OnSaveHomeworkButtonClicked : CreateHomeworkUIEvent()

    // Event yang dipicu ketika tombol konfirmasi simpan tugas diklik
    data object OnConfirmSaveHomeworkClick : CreateHomeworkUIEvent()

    // Event yang dipicu ketika tombol untuk memilih tanggal tugas diklik
    data object OnPickDateButtonClicked : CreateHomeworkUIEvent()

    // Event yang dipicu ketika tanggal dipilih
    data class OnDatePicked(val date: Date) : CreateHomeworkUIEvent()

    // Event yang dipicu ketika waktu pengingat dipilih
    data class OnTimePicked(val time: Time) : CreateHomeworkUIEvent()

    // Event yang dipicu ketika waktu deadline dipilih
    data class OnDeadlineTimePicked(val times: DeadlineTime) : CreateHomeworkUIEvent()

    // Event yang dipicu ketika mata pelajaran dipilih
    data class OnSubjectPicked(val subject: Subject) : CreateHomeworkUIEvent()

    // Event yang dipicu ketika lampiran dipilih
    data class OnAttachmentPicked(val attachments: List<Attachment>) : CreateHomeworkUIEvent()

    // Event untuk memilih waktu pengingat
    data object OnPickTimeButtonClicked : CreateHomeworkUIEvent()

    // Event untuk memilih waktu deadline
    data object OnPickDeadlineTimeButtonClicked : CreateHomeworkUIEvent()

    // Event untuk memilih mata pelajaran
    data object OnPickSubjectButtonClicked : CreateHomeworkUIEvent()

    // Event untuk memilih lampiran
    data object OnPickAttachmentButtonClicked : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan tanggal
    data object OnDismissDatePicker : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan waktu
    data object OnDismissTimePicker : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan waktu deadline
    data object OnDismissDeadlineTimePicker : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan mata pelajaran
    data object OnDismissSubjectPicker : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan lampiran
    data object OnDismissAttachmentPicker : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) dialog konfirmasi simpan tugas
    data object OnDismissSaveConfirmationDialog : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) dialog yang menampilkan tugas yang telah disimpan
    data object OnDismissHomeworkSavedDialog : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) tampilan loading saat menyimpan tugas
    data object OnDismissSavingLoading : CreateHomeworkUIEvent()

    // Event untuk menutup (dismiss) dialog error
    data object OnDismissErrorDialog : CreateHomeworkUIEvent()
}
