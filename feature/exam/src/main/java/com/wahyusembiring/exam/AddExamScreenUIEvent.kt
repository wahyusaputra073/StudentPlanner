package com.wahyusembiring.exam

import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.data.model.entity.Subject
import java.util.Date

// Sealed class untuk mendefinisikan berbagai event yang terjadi di ExamScreenUI
sealed class AddExamScreenUIEvent {
    // Event ketika nama ujian berubah
    data class OnExamNameChanged(val name: String) : AddExamScreenUIEvent()

    // Event ketika deskripsi ujian berubah
    data class OnExamDescriptionChanged(val name: String) : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik picker tanggal ujian
    data object OnExamDatePickerClick : AddExamScreenUIEvent()

    // Event ketika tanggal ujian dipilih
    data class OnDatePicked(val date: Date) : AddExamScreenUIEvent()

    // Event ketika picker tanggal dibatalkan
    data object OnDatePickedDismiss : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik picker waktu ujian
    data object OnExamTimePickerClick : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik picker waktu deadline ujian
    data object OnExamDeadlineTimePickerClick : AddExamScreenUIEvent()

    // Event ketika waktu ujian dipilih
    data class OnTimePicked(val time: Time) : AddExamScreenUIEvent()

    // Event ketika waktu deadline ujian dipilih
    data class OnDeadlineTimePicked(val times: DeadlineTime) : AddExamScreenUIEvent()

    // Event ketika picker waktu ujian dibatalkan
    data object OnTimePickedDismiss : AddExamScreenUIEvent()

    // Event ketika picker waktu deadline ujian dibatalkan
    data object OnDeadlineTimePickedDismiss : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik picker mata pelajaran ujian
    data object OnExamSubjectPickerClick : AddExamScreenUIEvent()

    // Event ketika mata pelajaran ujian dipilih
    data class OnSubjectPicked(val subject: Subject) : AddExamScreenUIEvent()

    // Event ketika picker mata pelajaran dibatalkan
    data object OnSubjectPickedDismiss : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik picker kategori ujian
    data object OnExamCategoryPickerClick : AddExamScreenUIEvent()

    // Event ketika kategori ujian dipilih
    data class OnCategoryPicked(val category: ExamCategory) : AddExamScreenUIEvent()

    // Event ketika picker kategori ujian dibatalkan
    data object OnCategoryPickedDismiss : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik picker lampiran ujian
    data object OnExamAttachmentPickerClick : AddExamScreenUIEvent()

    // Event ketika lampiran ujian dipilih
    data class OnAttachmentPicked(val attachments: List<Attachment>) : AddExamScreenUIEvent()

    // Event ketika picker lampiran dibatalkan
    data object OnAttachmentPickedDismiss : AddExamScreenUIEvent()

    // Event ketika pengguna mengklik tombol simpan ujian
    data object OnSaveExamButtonClick : AddExamScreenUIEvent()

    // Event ketika pengguna mengonfirmasi untuk menyimpan ujian
    data object OnSaveExamConfirmClick : AddExamScreenUIEvent()

    // Event ketika dialog konfirmasi simpan ujian dibatalkan
    data object OnSaveConfirmationDialogDismiss : AddExamScreenUIEvent()

    // Event ketika dialog ujian berhasil disimpan dibatalkan
    data object OnExamSavedDialogDismiss : AddExamScreenUIEvent()

    // Event ketika dialog error dibatalkan
    data object OnErrorDialogDismiss : AddExamScreenUIEvent()
}
