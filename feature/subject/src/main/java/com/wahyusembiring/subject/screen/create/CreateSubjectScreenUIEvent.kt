package com.wahyusembiring.subject.screen.create

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.wahyusembiring.data.model.entity.Lecturer

sealed class CreateSubjectScreenUIEvent {  // Mendefinisikan sealed class untuk event UI pada CreateSubjectScreen
    data object OnSaveButtonClicked : CreateSubjectScreenUIEvent()  // Event ketika tombol simpan diklik
    data class OnSubjectNameChanged(val name: String) : CreateSubjectScreenUIEvent()  // Event ketika nama mata kuliah diubah
    data class OnRoomChanged(val room: String) : CreateSubjectScreenUIEvent()  // Event ketika ruang diubah
    data object OnPickColorButtonClicked : CreateSubjectScreenUIEvent()  // Event ketika tombol pilih warna diklik
    data class OnColorPicked(val color: Color) : CreateSubjectScreenUIEvent()  // Event ketika warna dipilih
    data object OnColorPickerDismiss : CreateSubjectScreenUIEvent()  // Event ketika dialog pemilih warna dibatalkan
    data class OnLecturerSelected(val lecturer: Lecturer) : CreateSubjectScreenUIEvent()  // Event ketika pengajar dipilih
    data object OnSaveConfirmationDialogConfirm : CreateSubjectScreenUIEvent()  // Event ketika konfirmasi simpan diklik
    data object OnSaveConfirmationDialogDismiss : CreateSubjectScreenUIEvent()  // Event ketika dialog konfirmasi simpan dibatalkan
    data object OnSubjectSavedDialogDismiss : CreateSubjectScreenUIEvent()  // Event ketika dialog mata kuliah tersimpan dibatalkan
    data object OnErrorDialogDismiss : CreateSubjectScreenUIEvent()  // Event ketika dialog error dibatalkan
}

