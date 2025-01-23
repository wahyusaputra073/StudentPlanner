package com.wahyusembiring.subject.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.SubjectRepository
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
class SubjectScreenViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,  // Mengambil data subject dari repository
) : ViewModel() {

    // Mengambil data subject, exam, dan homework pada saat inisialisasi ViewModel
    init {
        viewModelScope.launch {
            subjectRepository.getAllSubjectWithExamAndHomework()
            .collect { subjectWithExamAndHomework ->  // Mengambil dan mengupdate data
                println("Broooooo ${subjectWithExamAndHomework.size}")
                _uiState.update {
                    it.copy(
                        subjects = subjectWithExamAndHomework  // Menyimpan hasil ke dalam state
                    )
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(SubjectScreenUIState())  // Menyimpan state UI terkait subject
    val uiState = _uiState.asStateFlow()  // Mengakses state untuk UI

    private val _navigationEvent = Channel<SubjectScreenNavigationEvent>()  // Menyimpan event navigasi
    val navigationEvent = _navigationEvent.receiveAsFlow()  // Mengakses event navigasi

    // Menangani event yang dipicu oleh UI
    fun onUIEvent(event: SubjectScreenUIEvent) {
        when (event) {
            is SubjectScreenUIEvent.OnExamClick -> {}  // Event klik exam (belum ditangani)
            is SubjectScreenUIEvent.OnFloatingActionButtonClick -> {}  // Event klik FAB (belum ditangani)
            is SubjectScreenUIEvent.OnHamburgerMenuClick -> {}  // Event klik menu (belum ditangani)
            is SubjectScreenUIEvent.OnHomeworkClick -> {}  // Event klik homework (belum ditangani)
            is SubjectScreenUIEvent.OnSubjectClick -> onSubjectClick(event.subject)  // Navigasi ke detail subject
            is SubjectScreenUIEvent.OnDeleteSubjectClick -> onDeleteSubjectClick(event.subject)  // Hapus subject
        }
    }

    // Navigasi ke halaman detail subject
    private fun onSubjectClick(subject: Subject) {
        _navigationEvent.trySend(SubjectScreenNavigationEvent.NavigateToSubjectDetail(subject))  // Kirim event navigasi
    }

    // Menghapus subject melalui repository
    private fun onDeleteSubjectClick(subject: Subject) {
        viewModelScope.launch {
            Log.d("LecturerViewModel", "Deleting lecturer with ID: ${subject.id}")
            withContext(Dispatchers.IO) {
                subjectRepository.onDeleteSubject(subject)  // Panggil metode delete pada repository
            }
        }
    }
}