package com.wahyusembiring.lecturer.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.data.model.LecturerWithSubject
import com.wahyusembiring.data.repository.LecturerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LecturerScreenViewModel @Inject constructor(
    private val lecturerRepository: LecturerRepository // Injeksi repository untuk data dosen
) : ViewModel() {

    // State untuk menyimpan UI state di LecturerScreen
    private val _state = MutableStateFlow(LecturerScreenUIState())
    val state = _state.asStateFlow() // State yang dibaca oleh UI

    // Channel untuk mengirimkan event navigasi
    private val _navigationEvent = Channel<LecturerScreenNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow() // Mengamati event navigasi

    init {
        // Menyegarkan daftar dosen ketika ViewModel pertama kali diinisialisasi
        viewModelScope.launch {
            refreshLecturerList()
        }
    }

    // Fungsi untuk menangani event dari UI
    fun onUIEvent(event: LecturerScreenUIEvent) {
        when (event) {
            is LecturerScreenUIEvent.OnAddLecturerClick -> onAddLecturerClick() // Menambah dosen baru
            is LecturerScreenUIEvent.OnLecturerClick -> onLecturerClick(event.lecturerWithSubjects) // Klik pada dosen untuk melihat detail
            is LecturerScreenUIEvent.OnDeleteLecturerClick -> onDeleteLecturerClick(event.lecturerWithSubjects) // Menghapus dosen
            is LecturerScreenUIEvent.OnDeletePhoneNumberClick -> onDeletePhoneNumberClick(event.phoneNumber) // Menghapus nomor telepon dosen
        }
    }

    // Fungsi untuk navigasi ke detail dosen
    private fun onLecturerClick(lecturerWithSubjects: LecturerWithSubject) {
        _navigationEvent.trySend(
            LecturerScreenNavigationEvent.NavigateToLecturerDetail(lecturerWithSubjects.lecturer.id)
        )
    }

    // Fungsi untuk navigasi ke layar tambah dosen
    private fun onAddLecturerClick() {
        _navigationEvent.trySend(LecturerScreenNavigationEvent.NavigateToAddLecturer)
    }

    // Fungsi untuk menghapus dosen
    private fun onDeleteLecturerClick(lecturerWithSubjects: LecturerWithSubject) {
        viewModelScope.launch {
            Log.d("LecturerViewModel", "Deleting lecturer with ID: ${lecturerWithSubjects.lecturer.id}")
            withContext(Dispatchers.IO) {
                lecturerRepository.deleteLecturer(lecturerWithSubjects.lecturer.id) // Menghapus dosen dari repository
            }
            refreshLecturerList() // Menyegarkan daftar dosen setelah dihapus
        }
    }

    // Fungsi untuk menghapus nomor telepon dosen
    private fun onDeletePhoneNumberClick(phoneNumber: String) {
        viewModelScope.launch {
            Log.d("LecturerViewModel", "Deleting phone number: $phoneNumber")
            withContext(Dispatchers.IO) {
                lecturerRepository.deletePhoneNumber(phoneNumber) // Menghapus nomor telepon dari repository
            }
            refreshLecturerList() // Menyegarkan daftar dosen setelah dihapus
        }
    }

    // Fungsi untuk menyegarkan daftar dosen dan subjek
    private suspend fun refreshLecturerList() {
        Log.d("LecturerViewModel", "Refreshing lecturer list...")
        lecturerRepository.getAllLecturerWithSubjects().collect { listOfLecturerWithSubjects ->
            _state.update {
                it.copy(listOfLecturerWithSubjects = listOfLecturerWithSubjects) // Mengupdate state dengan daftar dosen terbaru
            }
        }
        Log.d("LecturerViewModel", "Lecturer list refreshed.")
    }
}
