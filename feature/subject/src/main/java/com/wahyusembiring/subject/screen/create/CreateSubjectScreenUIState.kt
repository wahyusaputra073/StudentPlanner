package com.wahyusembiring.subject.screen.create

import androidx.compose.ui.graphics.Color
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.ui.theme.primaryLight
import com.wahyusembiring.ui.util.UIText

data class CreateSubjectScreenUIState(  // Mendefinisikan data class untuk menyimpan state UI pada CreateSubjectScreen
    val isEditMode: Boolean = false,  // Menyimpan status apakah dalam mode edit atau tidak
    val name: String = "",  // Menyimpan nama mata kuliah
    val color: Color = primaryLight,  // Menyimpan warna yang dipilih untuk mata kuliah
    val room: String = "",  // Menyimpan ruang tempat mata kuliah diajarkan
    val description: String = "",  // Menyimpan deskripsi mata kuliah
    val lecturer: Lecturer? = null,  // Menyimpan pengajar yang dipilih
    val lecturers: List<Lecturer> = emptyList(),  // Menyimpan daftar pengajar yang tersedia

    // popup
    val showColorPicker: Boolean = false,  // Menyimpan status apakah dialog pemilih warna ditampilkan
    val showSaveConfirmationDialog: Boolean = false,  // Menyimpan status apakah dialog konfirmasi simpan ditampilkan
    val showSavingLoading: Boolean = false,  // Menyimpan status apakah loading sedang aktif
    val showSubjectSavedDialog: Boolean = false,  // Menyimpan status apakah dialog mata kuliah tersimpan ditampilkan
    val errorMessage: UIText? = null  // Menyimpan pesan error jika ada
)

