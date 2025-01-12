package com.wahyusembiring.exam

import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.data.model.entity.Subject
import java.util.Date

// Sealed class untuk mendefinisikan berbagai event yang terjadi di ExamScreenUI
sealed class ExamScreenUIEvent {
    // Event ketika nama ujian berubah
    data class OnExamNameChanged(val name: String) : ExamScreenUIEvent()

    // Event ketika deskripsi ujian berubah
    data class OnExamDescriptionChanged(val name: String) : ExamScreenUIEvent()

    // Event ketika pengguna mengklik picker tanggal ujian
    data object OnExamDatePickerClick : ExamScreenUIEvent()

    // Event ketika tanggal ujian dipilih
    data class OnDatePicked(val date: Date) : ExamScreenUIEvent()

    // Event ketika picker tanggal dibatalkan
    data object OnDatePickedDismiss : ExamScreenUIEvent()

    // Event ketika pengguna mengklik picker waktu ujian
    data object OnExamTimePickerClick : ExamScreenUIEvent()

    // Event ketika pengguna mengklik picker waktu deadline ujian
    data object OnExamDeadlineTimePickerClick : ExamScreenUIEvent()

    // Event ketika waktu ujian dipilih
    data class OnTimePicked(val time: Time) : ExamScreenUIEvent()

    // Event ketika waktu deadline ujian dipilih
    data class OnDeadlineTimePicked(val times: DeadlineTime) : ExamScreenUIEvent()

    // Event ketika picker waktu ujian dibatalkan
    data object OnTimePickedDismiss : ExamScreenUIEvent()

    // Event ketika picker waktu deadline ujian dibatalkan
    data object OnDeadlineTimePickedDismiss : ExamScreenUIEvent()

    // Event ketika pengguna mengklik picker mata pelajaran ujian
    data object OnExamSubjectPickerClick : ExamScreenUIEvent()

    // Event ketika mata pelajaran ujian dipilih
    data class OnSubjectPicked(val subject: Subject) : ExamScreenUIEvent()

    // Event ketika picker mata pelajaran dibatalkan
    data object OnSubjectPickedDismiss : ExamScreenUIEvent()

    // Event ketika pengguna mengklik picker kategori ujian
    data object OnExamCategoryPickerClick : ExamScreenUIEvent()

    // Event ketika kategori ujian dipilih
    data class OnCategoryPicked(val category: ExamCategory) : ExamScreenUIEvent()

    // Event ketika picker kategori ujian dibatalkan
    data object OnCategoryPickedDismiss : ExamScreenUIEvent()

    // Event ketika pengguna mengklik picker lampiran ujian
    data object OnExamAttachmentPickerClick : ExamScreenUIEvent()

    // Event ketika lampiran ujian dipilih
    data class OnAttachmentPicked(val attachments: List<Attachment>) : ExamScreenUIEvent()

    // Event ketika picker lampiran dibatalkan
    data object OnAttachmentPickedDismiss : ExamScreenUIEvent()

    // Event ketika pengguna mengklik tombol simpan ujian
    data object OnSaveExamButtonClick : ExamScreenUIEvent()

    // Event ketika pengguna mengonfirmasi untuk menyimpan ujian
    data object OnSaveExamConfirmClick : ExamScreenUIEvent()

    // Event ketika dialog konfirmasi simpan ujian dibatalkan
    data object OnSaveConfirmationDialogDismiss : ExamScreenUIEvent()

    // Event ketika dialog ujian berhasil disimpan dibatalkan
    data object OnExamSavedDialogDismiss : ExamScreenUIEvent()

    // Event ketika dialog error dibatalkan
    data object OnErrorDialogDismiss : ExamScreenUIEvent()
}
