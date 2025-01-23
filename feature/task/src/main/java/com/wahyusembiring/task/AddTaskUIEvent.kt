package com.wahyusembiring.task

import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Subject
import java.util.Date

// Sealed class yang digunakan untuk mengelompokkan berbagai jenis event UI yang terjadi di tampilan pembuatan tugas
sealed class AddTaskUIEvent {

    // Event yang dipicu ketika judul tugas diubah
    data class OnHomeworkTitleChanged(val title: String) : AddTaskUIEvent()

    // Event yang dipicu ketika deskripsi ujian diubah
    data class OnExamDescriptionChanged(val title: String) : AddTaskUIEvent()

    // Event yang dipicu ketika tombol simpan tugas diklik
    data object OnSaveHomeworkButtonClicked : AddTaskUIEvent()

    // Event yang dipicu ketika tombol konfirmasi simpan tugas diklik
    data object OnConfirmSaveHomeworkClick : AddTaskUIEvent()

    // Event yang dipicu ketika tombol untuk memilih tanggal tugas diklik
    data object OnPickDateButtonClicked : AddTaskUIEvent()

    // Event yang dipicu ketika tanggal dipilih
    data class OnDatePicked(val date: Date) : AddTaskUIEvent()

    // Event yang dipicu ketika waktu pengingat dipilih
    data class OnTimePicked(val time: Time) : AddTaskUIEvent()

    // Event yang dipicu ketika waktu deadline dipilih
    data class OnDeadlineTimePicked(val times: DeadlineTime) : AddTaskUIEvent()

    // Event yang dipicu ketika mata pelajaran dipilih
    data class OnSubjectPicked(val subject: Subject) : AddTaskUIEvent()

    // Event yang dipicu ketika lampiran dipilih
    data class OnAttachmentPicked(val attachments: List<Attachment>) : AddTaskUIEvent()

    // Event untuk memilih waktu pengingat
    data object OnPickTimeButtonClicked : AddTaskUIEvent()

    // Event untuk memilih waktu deadline
    data object OnPickDeadlineTimeButtonClicked : AddTaskUIEvent()

    // Event untuk memilih mata pelajaran
    data object OnPickSubjectButtonClicked : AddTaskUIEvent()

    // Event untuk memilih lampiran
    data object OnPickAttachmentButtonClicked : AddTaskUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan tanggal
    data object OnDismissDatePicker : AddTaskUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan waktu
    data object OnDismissTimePicker : AddTaskUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan waktu deadline
    data object OnDismissDeadlineTimePicker : AddTaskUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan mata pelajaran
    data object OnDismissSubjectPicker : AddTaskUIEvent()

    // Event untuk menutup (dismiss) popup yang terkait dengan pemilihan lampiran
    data object OnDismissAttachmentPicker : AddTaskUIEvent()

    // Event untuk menutup (dismiss) dialog konfirmasi simpan tugas
    data object OnDismissSaveConfirmationDialog : AddTaskUIEvent()

    // Event untuk menutup (dismiss) dialog yang menampilkan tugas yang telah disimpan
    data object OnDismissHomeworkSavedDialog : AddTaskUIEvent()

    // Event untuk menutup (dismiss) tampilan loading saat menyimpan tugas
    data object OnDismissSavingLoading : AddTaskUIEvent()

    // Event untuk menutup (dismiss) dialog error
    data object OnDismissErrorDialog : AddTaskUIEvent()

    data class OnEmailAddressChanged(val email: String) : AddTaskUIEvent()
    data object OnSendEmailButtonClicked : AddTaskUIEvent()
    data object OnDismissEmailSentDialog : AddTaskUIEvent()
}
