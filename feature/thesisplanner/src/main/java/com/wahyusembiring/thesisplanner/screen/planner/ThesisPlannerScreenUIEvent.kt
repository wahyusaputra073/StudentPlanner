package com.wahyusembiring.thesisplanner.screen.planner

import android.net.Uri
import com.wahyusembiring.data.model.File
import com.wahyusembiring.data.model.entity.Task

sealed class ThesisPlannerScreenUIEvent { // Menyimpan event-event UI yang terkait dengan layar ThesisPlanner
    data class OnArticleClick(val article: File) : ThesisPlannerScreenUIEvent() // Event ketika artikel diklik
    class OnDocumentPickerResult(val files: List<File>) : ThesisPlannerScreenUIEvent() // Event saat hasil pemilihan dokumen diterima
    data class OnDeleteArticleClick(val article: File) : ThesisPlannerScreenUIEvent() // Event ketika tombol hapus artikel diklik
    data class OnSaveTaskClick(val task: Task) : ThesisPlannerScreenUIEvent() // Event saat tombol simpan tugas diklik
    data class OnTaskCompletedStatusChange(val task: Task, val isCompleted: Boolean) : ThesisPlannerScreenUIEvent() // Event saat status tugas selesai berubah
    data class OnDeleteTaskClick(val task: Task) : ThesisPlannerScreenUIEvent() // Event ketika tombol hapus tugas diklik
    data class OnThesisTitleChange(val thesisName: String) : ThesisPlannerScreenUIEvent() // Event saat judul tesis berubah
    data object OnCreateTaskButtonClick : ThesisPlannerScreenUIEvent() // Event saat tombol buat tugas diklik
    data object OnCreateTaskDialogDismiss : ThesisPlannerScreenUIEvent() // Event saat dialog pembuatan tugas ditutup
    data object OnDatePickerButtonClick : ThesisPlannerScreenUIEvent() // Event saat tombol untuk memilih tanggal diklik
    data object OnDatePickerDismiss : ThesisPlannerScreenUIEvent() // Event saat date picker ditutup
    data class OnDeleteArticleConfirm(val article: File) : ThesisPlannerScreenUIEvent() // Event saat konfirmasi penghapusan artikel
    data object OnArticleDeleteDialogDismiss : ThesisPlannerScreenUIEvent() // Event saat dialog konfirmasi penghapusan artikel ditutup
    data class OnTaskDeleteConfirm(val task: Task) : ThesisPlannerScreenUIEvent() // Event saat konfirmasi penghapusan tugas
    data object OnTaskDeleteDialogDismiss : ThesisPlannerScreenUIEvent() // Event saat dialog konfirmasi penghapusan tugas ditutup
}
