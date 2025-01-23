package com.wahyusembiring.thesisplanner.screen.planner

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.launch
import com.wahyusembiring.data.model.File
import com.wahyusembiring.data.model.ThesisWithTask
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.data.model.entity.Thesis
import com.wahyusembiring.data.repository.ThesisRepository
import com.wahyusembiring.ui.util.UIText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.wahyusembiring.thesisplanner.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ThesisPlannerScreenViewModel.Factory::class) // Menandai kelas ini sebagai ViewModel yang di-inject oleh Hilt
class ThesisPlannerScreenViewModel @AssistedInject constructor( // ViewModel untuk mengelola state dan logika layar Thesis Planner
    @Assisted private val thesisId: Int, // Menyimpan ID tesis yang diambil dari argumen
    private val thesisRepository: ThesisRepository // Menyimpan repository yang bertanggung jawab mengakses data tesis
) : ViewModel() { // Kelas turunan ViewModel untuk mengelola state UI dan logika bisnis


    @AssistedFactory // Menandai bahwa ini adalah factory untuk membuat instance ThesisPlannerScreenViewModel dengan parameter thesisId
    interface Factory {
        fun create(thesisId: Int): ThesisPlannerScreenViewModel // Fungsi untuk membuat ThesisPlannerScreenViewModel dengan thesisId sebagai parameter
    }

    private val debounceDuration = 1000L // Durasi waktu tunggu (debounce) dalam milidetik untuk mencegah update berulang-ulang terlalu cepat
    private var savingTitleJob: Job? = null // Menyimpan job yang digunakan untuk menunda perubahan judul tesis
    private lateinit var thesis: ThesisWithTask // Menyimpan data tesis yang diambil dari repository

    init {
        // Mengambil data tesis berdasarkan thesisId dan mengupdate UIState setelah data tesis berhasil diambil
        viewModelScope.launch {
            thesisRepository.getThesisById(thesisId).collect { thesis -> // Mengambil data tesis secara asinkron
                this@ThesisPlannerScreenViewModel.thesis = thesis // Menyimpan data tesis yang diterima
                _uiState.update { // Memperbarui UIState dengan data tesis yang baru
                    it.copy(
                        thesisTitle = thesis.thesis.title, // Mengupdate judul tesis
                        editedThesisTitle = thesis.thesis.title, // Mengupdate judul tesis yang sedang diedit
                        articles = thesis.thesis.articles, // Mengupdate daftar artikel tesis
                        tasks = thesis.tasks // Mengupdate daftar tugas terkait tesis
                    )
                }
            }
        }
    }


    private val _uiState = MutableStateFlow(ThesisPlannerScreenUIState()) // Membuat StateFlow untuk menyimpan dan mengelola state UI
    val uiState: StateFlow<ThesisPlannerScreenUIState> = _uiState.asStateFlow() // Membuat UIState yang hanya bisa dibaca di luar class ini

    fun onUIEvent(event: ThesisPlannerScreenUIEvent) { // Fungsi untuk menangani berbagai event UI yang diterima
        when (event) { // Memeriksa jenis event dan memanggil fungsi terkait untuk setiap event
            is ThesisPlannerScreenUIEvent.OnArticleClick -> onArticleClick(event.article) // Menangani klik pada artikel
            is ThesisPlannerScreenUIEvent.OnDeleteArticleClick -> launch { // Menangani permintaan penghapusan artikel
                onDeleteArticleClick(event.article) // Memanggil fungsi penghapusan artikel
            }
            is ThesisPlannerScreenUIEvent.OnDocumentPickerResult -> onDocumentPickerResult(event.files) // Menangani hasil pemilihan dokumen
            is ThesisPlannerScreenUIEvent.OnThesisTitleChange -> onThesisTitleChange(event.thesisName) // Menangani perubahan judul tesis
            is ThesisPlannerScreenUIEvent.OnSaveTaskClick -> launch { // Menangani penyimpanan tugas baru
                onSaveTaskClick(event.task) // Memanggil fungsi untuk menyimpan tugas
            }
            is ThesisPlannerScreenUIEvent.OnTaskCompletedStatusChange -> launch { // Menangani perubahan status tugas (selesai/tidak)
                onTaskCompletedStatusChange(event.task, event.isCompleted) // Memanggil fungsi untuk memperbarui status tugas
            }
            is ThesisPlannerScreenUIEvent.OnArticleDeleteDialogDismiss -> onArticleDeleteDialogDismiss() // Menangani penutupan dialog konfirmasi penghapusan artikel
            is ThesisPlannerScreenUIEvent.OnCreateTaskButtonClick -> onCreateTaskButtonClick() // Menangani klik tombol untuk membuat tugas baru
            is ThesisPlannerScreenUIEvent.OnCreateTaskDialogDismiss -> onCreateTaskDialogDismiss() // Menangani penutupan dialog pembuatan tugas baru
            is ThesisPlannerScreenUIEvent.OnDatePickerButtonClick -> onDatePickerButtonClick() // Menangani klik tombol untuk memilih tanggal
            is ThesisPlannerScreenUIEvent.OnDatePickerDismiss -> onDatePickerDismiss() // Menangani penutupan dialog pemilih tanggal
            is ThesisPlannerScreenUIEvent.OnDeleteArticleConfirm -> onDeleteArticleConfirm(event.article) // Menangani konfirmasi penghapusan artikel
            is ThesisPlannerScreenUIEvent.OnDeleteTaskClick -> onDeleteTaskClick(event.task) // Menangani klik penghapusan tugas
            is ThesisPlannerScreenUIEvent.OnTaskDeleteConfirm -> onTaskDeleteConfirm(event.task) // Menangani konfirmasi penghapusan tugas
            is ThesisPlannerScreenUIEvent.OnTaskDeleteDialogDismiss -> onTaskDeleteDialogDismiss() // Menangani penutupan dialog konfirmasi penghapusan tugas
        }
    }


    private fun onTaskDeleteDialogDismiss() { // Menangani penutupan dialog konfirmasi penghapusan tugas
        _uiState.update { it.copy(taskPendingDelete = null) } // Menghapus tugas yang tertunda untuk dihapus dari UI state
    }

    private fun onTaskDeleteConfirm(task: Task) { // Menangani konfirmasi penghapusan tugas
        viewModelScope.launch {
            thesisRepository.deleteTask(task) // Memanggil repository untuk menghapus tugas dari database
        }
    }

    private fun onDeleteTaskClick(task: Task) { // Menangani klik penghapusan tugas
        _uiState.update {
            it.copy(taskPendingDelete = task) // Menyimpan tugas yang akan dihapus di UI state
        }
    }

    private fun onDeleteArticleConfirm(article: File) { // Menangani konfirmasi penghapusan artikel
        viewModelScope.launch {
            thesisRepository.updateThesis( // Memperbarui tesis setelah artikel dihapus
                thesis.thesis.let {
                    it.copy(articles = it.articles - article) // Menghapus artikel dari daftar artikel tesis
                }
            )
        }
    }

    private fun onDatePickerDismiss() { // Menangani penutupan dialog pemilih tanggal
        _uiState.update { it.copy(showDatePicker = false) } // Menyembunyikan dialog pemilih tanggal
    }

    private fun onDatePickerButtonClick() { // Menangani klik tombol untuk membuka dialog pemilih tanggal
        _uiState.update { it.copy(showDatePicker = true) } // Menampilkan dialog pemilih tanggal
    }

    private fun onCreateTaskDialogDismiss() { // Menangani penutupan dialog pembuatan tugas baru
        _uiState.update { it.copy(showCreateTaskDialog = false) } // Menyembunyikan dialog pembuatan tugas baru
    }

    private fun onCreateTaskButtonClick() { // Menangani klik tombol untuk membuka dialog pembuatan tugas baru
        _uiState.update { it.copy(showCreateTaskDialog = true) } // Menampilkan dialog pembuatan tugas baru
    }

    private fun onArticleDeleteDialogDismiss() { // Menangani penutupan dialog penghapusan artikel
        _uiState.update { it.copy(articlePendingDelete = null) } // Menghapus artikel yang tertunda untuk dihapus dari UI state
    }

    private fun onArticleClick(article: File) { // Menangani klik artikel (fungsi kosong)
        // Implementasi untuk menanggapi klik artikel dapat ditambahkan di sini
    }

    private suspend fun onTaskCompletedStatusChange(task: Task, completed: Boolean) { // Menangani perubahan status tugas selesai
        thesisRepository.changeTaskCompletedStatus(task, completed) // Memperbarui status tugas di repository
    }

    private suspend fun onSaveTaskClick(task: Task) { // Menangani klik simpan tugas
        val updatedTask = task.copy(thesisId = thesisId) // Menyalin tugas dengan menambahkan thesisId
        thesisRepository.addNewTask(updatedTask) // Menambahkan tugas baru ke repository
    }


    private fun onThesisTitleChange(thesisName: String) { // Menangani perubahan judul tesis
        _uiState.update { // Memperbarui UI state dengan judul tesis yang diedit
            it.copy(
                editedThesisTitle = thesisName // Menyimpan judul tesis yang baru
            )
        }
        savingTitleJob = viewModelScope.launch { // Menunda proses pembaruan judul tesis agar tidak terlalu sering dilakukan
            if (savingTitleJob?.isActive == true) savingTitleJob?.cancel() // Membatalkan tugas sebelumnya jika ada
            delay(debounceDuration) // Menunggu selama durasi debounce untuk menghindari pembaruan yang terlalu cepat
            thesisRepository.updateThesisTitleById(thesisId, thesisName) // Memperbarui judul tesis di repository
        }
    }

    private fun onDeleteArticleClick(article: File) { // Menangani klik untuk menghapus artikel
        _uiState.update { // Menyimpan artikel yang tertunda untuk dihapus dalam UI state
            it.copy(articlePendingDelete = article)
        }
    }

    private fun onDocumentPickerResult(articles: List<File>) { // Menangani hasil pemilihan dokumen
        viewModelScope.launch {
            articles.forEach { // Menambahkan setiap artikel yang dipilih ke dalam daftar artikel tesis
                thesisRepository.updateThesis(
                    thesis = thesis.thesis.copy(articles = thesis.thesis.articles + it) // Memperbarui daftar artikel dalam tesis
                )
            }
        }
    }
}