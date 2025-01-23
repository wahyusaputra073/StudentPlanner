package com.wahyusembiring.lecturer.screen.main

import androidx.navigation.NavController
import com.wahyusembiring.data.model.LecturerWithSubject

// Data class untuk menyimpan state UI pada LecturerScreen
data class LecturerScreenUIState(
    val listOfLecturerWithSubjects: List<LecturerWithSubject> = emptyList() // Daftar dosen beserta subjeknya
)

// Sealed class untuk mendefinisikan event UI yang terjadi pada LecturerScreen
sealed class LecturerScreenUIEvent {
    // Event saat tombol untuk menambah dosen diklik
    data class OnAddLecturerClick(val navController: NavController) : LecturerScreenUIEvent()

    // Event saat item dosen diklik untuk melihat detail
    data class OnLecturerClick(val lecturerWithSubjects: LecturerWithSubject) : LecturerScreenUIEvent()

    // Event saat tombol hapus dosen diklik
    data class OnDeleteLecturerClick(val lecturerWithSubjects: LecturerWithSubject) : LecturerScreenUIEvent() // Event untuk menghapus lecturer

    // Event saat nomor telepon dosen dihapus
    data class OnDeletePhoneNumberClick(val phoneNumber: String) : LecturerScreenUIEvent() // Event untuk menghapus nomor telepon
}

// Sealed class untuk menangani navigasi dalam LecturerScreen
sealed class LecturerScreenNavigationEvent {
    // Event untuk navigasi ke detail dosen
    data class NavigateToLecturerDetail(val lecturerId: Int) : LecturerScreenNavigationEvent()

    // Event untuk navigasi ke layar tambah dosen
    data object NavigateToAddLecturer : LecturerScreenNavigationEvent()
}
