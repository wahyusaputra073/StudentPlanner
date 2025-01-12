package com.wahyusembiring.thesisplanner.screen.thesisselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.launch
import com.wahyusembiring.data.repository.ThesisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Menandakan bahwa ViewModel ini menggunakan dependency injection dengan Hilt
class ThesisSelectionScreenViewModel @Inject constructor( // ViewModel untuk layar seleksi tesis
    private val thesisRepository: ThesisRepository // Menginjeksi repository untuk mengelola data tesis
) : ViewModel() {

    init { // Inisialisasi ViewModel, menjalankan operasi untuk mendapatkan daftar tesis
        viewModelScope.launch {
            thesisRepository.getAllThesis().collect { listOfThesis -> // Mengambil semua tesis dari repository
                _uiState.update { // Memperbarui state UI dengan daftar tesis yang diterima
                    it.copy(listOfThesis = listOfThesis)
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(ThesisSelectionScreenUIState()) // State UI yang menyimpan daftar tesis
    val uiState = _uiState.asStateFlow() // Mengubah _uiState menjadi StateFlow untuk diobservasi di UI

    fun onUIEvent(event: ThesisSelectionScreenUIEvent) { // Fungsi untuk menangani event UI
        when (event) {
            is ThesisSelectionScreenUIEvent.OnCreateNewThesisClick -> launch { // Jika event klik buat tesis baru
                onCreateNewThesisClick(
                    event.onNavigateToThesisPlanner // Menangani navigasi setelah tesis dibuat
                )
            }

            is ThesisSelectionScreenUIEvent.OnDeleteThesisClick -> launch { // Jika event klik hapus tesis
                onDeleteThesisClick(
                    event.thesis // Menangani penghapusan tesis
                )
            }
        }
    }

    private suspend fun onDeleteThesisClick(thesis: Thesis) { // Fungsi untuk menghapus tesis
        thesisRepository.deleteThesis(thesis.thesis) // Memanggil repository untuk menghapus tesis
    }

    private suspend fun onCreateNewThesisClick(onNavigateToThesisPlanner: (thesisId: Int) -> Unit) { // Fungsi untuk membuat tesis baru
        val newThesis = com.wahyusembiring.data.model.entity.Thesis( // Membuat objek tesis baru dengan judul default
            title = "Untitled Thesis",
            articles = emptyList() // Daftar artikel kosong
        )
        val newThesisId = thesisRepository.saveNewThesis(newThesis) // Menyimpan tesis baru ke repository
        onNavigateToThesisPlanner(newThesisId.toInt()) // Mengarahkan ke layar perencanaan tesis setelah penyimpanan
    }
}