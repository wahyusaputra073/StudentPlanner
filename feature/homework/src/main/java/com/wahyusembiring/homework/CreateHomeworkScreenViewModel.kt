package com.wahyusembiring.homework

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.scheduleReminder
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Homework
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.data.repository.HomeworkRepository
import com.wahyusembiring.data.repository.SubjectRepository
import com.wahyusembiring.ui.util.UIText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

@HiltViewModel(assistedFactory = CreateHomeworkScreenViewModel.Factory::class) // Menandakan ViewModel ini menggunakan Hilt untuk dependency injection dengan factory assisted
class CreateHomeworkScreenViewModel @AssistedInject constructor(
    @Assisted private val homeworkId: Int = -1, // Mendapatkan homeworkId dari parameter assisted
    private val eventRepository: EventRepository, // Repository untuk operasi event (misalnya, menyimpan tugas)
    private val subjectRepository: SubjectRepository, // Repository untuk operasi mata kuliah
    private val application: Application // Aplikasi untuk akses context
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(homeworkId: Int = -1): CreateHomeworkScreenViewModel // Factory untuk membuat instance ViewModel
    }

    private var getAllSubjectJob: Job? = null // Job untuk mengambil data subjek

    private val _state = MutableStateFlow(CreateHomeworkScreenUIState()) // MutableStateFlow untuk mengelola UI state
    val state = _state.asStateFlow() // StateFlow untuk dibaca oleh UI

    // Fungsi untuk menangani event UI yang dikirimkan
    fun onUIEvent(event: CreateHomeworkUIEvent) {
        viewModelScope.launch {
            when (event) {
                is CreateHomeworkUIEvent.OnHomeworkTitleChanged -> onHomeworkTitleChanged(event.title) // Menangani perubahan judul tugas
                is CreateHomeworkUIEvent.OnExamDescriptionChanged -> onExamDescriptionChanged(event.title) // Menangani perubahan deskripsi ujian
                is CreateHomeworkUIEvent.OnSaveHomeworkButtonClicked -> onSaveHomeworkButtonClick() // Menangani klik tombol simpan tugas
                is CreateHomeworkUIEvent.OnPickDateButtonClicked -> onDatePickerClick() // Menangani klik pemilih tanggal
                is CreateHomeworkUIEvent.OnPickTimeButtonClicked -> onTimePickerClick() // Menangani klik pemilih waktu
                is CreateHomeworkUIEvent.OnPickDeadlineTimeButtonClicked -> onDeadlineTimePickerClick() // Menangani klik pemilih waktu batas akhir
                is CreateHomeworkUIEvent.OnPickSubjectButtonClicked -> onSubjectPickerClick() // Menangani klik pemilih subjek
                is CreateHomeworkUIEvent.OnPickAttachmentButtonClicked -> onAttachmentPickerClick() // Menangani klik pemilih lampiran
                is CreateHomeworkUIEvent.OnAttachmentPicked -> onAttachmentPicked(event.attachments) // Menangani lampiran yang dipilih
                is CreateHomeworkUIEvent.OnConfirmSaveHomeworkClick -> onConfirmSaveHomeworkClick() // Menangani konfirmasi simpan tugas
                is CreateHomeworkUIEvent.OnDatePicked -> onDateSelected(event.date) // Menangani tanggal yang dipilih
                is CreateHomeworkUIEvent.OnDismissAttachmentPicker -> onDismissAttachmentPicker() // Menangani penutupan pemilih lampiran
                is CreateHomeworkUIEvent.OnDismissDatePicker -> onDismissDatePicker() // Menangani penutupan pemilih tanggal
                is CreateHomeworkUIEvent.OnDismissHomeworkSavedDialog -> onDismissHomeworkSavedDialog() // Menangani penutupan dialog tugas tersimpan
                is CreateHomeworkUIEvent.OnDismissSaveConfirmationDialog -> onDismissSaveConfirmationDialog() // Menangani penutupan dialog konfirmasi simpan
                is CreateHomeworkUIEvent.OnDismissSubjectPicker -> onDismissSubjectPicker() // Menangani penutupan pemilih subjek
                is CreateHomeworkUIEvent.OnDismissTimePicker -> onDismissTimePicker() // Menangani penutupan pemilih waktu
                is CreateHomeworkUIEvent.OnDismissDeadlineTimePicker -> onDismissDeadlineTimePicker() // Menangani penutupan pemilih waktu batas akhir
                is CreateHomeworkUIEvent.OnSubjectPicked -> onSubjectSelected(event.subject) // Menangani subjek yang dipilih
                is CreateHomeworkUIEvent.OnTimePicked -> onTimeSelected(event.time) // Menangani waktu yang dipilih
                is CreateHomeworkUIEvent.OnDeadlineTimePicked -> onDeadlineTimeSelected(event.times) // Menangani waktu batas akhir yang dipilih
                is CreateHomeworkUIEvent.OnDismissErrorDialog -> onDismissErrorDialog() // Menangani penutupan dialog error
                is CreateHomeworkUIEvent.OnDismissSavingLoading -> onDismissSavingLoading() // Menangani penutupan dialog loading
            }
        }
    }

    // Fungsi untuk menonaktifkan dialog loading
    private fun onDismissSavingLoading() {
        _state.update { it.copy(showSavingLoading = false) }
    }

    // Fungsi untuk menonaktifkan dialog error
    private fun onDismissErrorDialog() {
        _state.update { it.copy(errorMessage = null) }
    }

    // Fungsi untuk menonaktifkan pemilih waktu
    private fun onDismissTimePicker() {
        _state.update { it.copy(showTimePicker = false) }
    }

    // Fungsi untuk menonaktifkan pemilih waktu batas akhir
    private fun onDismissDeadlineTimePicker() {
        _state.update { it.copy(showDeadlineTimePicker = false) }
    }

    // Fungsi untuk menonaktifkan pemilih subjek
    private fun onDismissSubjectPicker() {
        _state.update { it.copy(showSubjectPicker = false) }
    }

    // Fungsi untuk menonaktifkan dialog konfirmasi simpan
    private fun onDismissSaveConfirmationDialog() {
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    // Fungsi untuk menonaktifkan dialog tugas tersimpan
    private fun onDismissHomeworkSavedDialog() {
        _state.update { it.copy(showHomeworkSavedDialog = false) }
    }

    // Fungsi untuk menonaktifkan pemilih tanggal
    private fun onDismissDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }

    // Fungsi untuk menonaktifkan pemilih lampiran
    private fun onDismissAttachmentPicker() {
        _state.update { it.copy(showAttachmentPicker = false) }
    }

    // Fungsi untuk menangani lampiran yang dipilih
    private fun onAttachmentPicked(attachments: List<Attachment>) {
        _state.update { it.copy(attachments = attachments) }
    }

    // Fungsi untuk menampilkan pemilih lampiran
    private suspend fun onAttachmentPickerClick() {
        _state.update { it.copy(showAttachmentPicker = true) }
    }

    // Fungsi untuk menampilkan pemilih subjek
    private suspend fun onSubjectPickerClick() {
        _state.update { it.copy(showSubjectPicker = true) }
    }

    // Fungsi untuk menampilkan pemilih waktu
    private suspend fun onTimePickerClick() {
        _state.update { it.copy(showTimePicker = true) }
    }

    // Fungsi untuk menampilkan pemilih waktu batas akhir
    private fun onDeadlineTimePickerClick() {
        _state.update { it.copy(showDeadlineTimePicker = true) }
    }

    // Fungsi untuk menampilkan pemilih tanggal
    private suspend fun onDatePickerClick() {
        _state.update { it.copy(showDatePicker = true) }
    }

    // Fungsi untuk menampilkan dialog konfirmasi simpan
    private suspend fun onSaveHomeworkButtonClick() {
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    // Fungsi untuk mengonfirmasi penyimpanan tugas
    private suspend fun onConfirmSaveHomeworkClick() {
        _state.update { it.copy(showSavingLoading = true) }
        try {
            val homework = Homework(
                id = if (homeworkId == -1) 0 else homeworkId, // Menentukan id tugas baru atau lama
                title = _state.value.homeworkTitle.ifBlank { throw MissingRequiredFieldException.Title() }, // Menangani judul tugas kosong
                dueDate = _state.value.date ?: throw MissingRequiredFieldException.Date(), // Menangani tanggal kosong
                subjectId = _state.value.subject?.id ?: throw MissingRequiredFieldException.Subject(), // Menangani subjek kosong
                reminder = _state.value.time, // Mengambil waktu pengingat
                deadline = _state.value.times, // Mengambil waktu deadline
                description = _state.value.description, // Mengambil deskripsi tugas
                attachments = _state.value.attachments, // Mengambil lampiran
                completed = _state.value.isCompleted // Menangani status tugas
            )
            val newHomeworkId = if (homeworkId == -1) {
                eventRepository.saveHomework(homework) // Menyimpan tugas baru
            } else {
                eventRepository.updateHomework(homework) // Memperbarui tugas yang ada
                homeworkId
            }
            scheduleReminder( // Menjadwalkan pengingat untuk tugas
                context = application.applicationContext,
                localDateTime = LocalDateTime.of(
                    LocalDate.ofInstant(homework.dueDate.toInstant(), ZoneId.systemDefault()),
                    LocalTime.of(homework.reminder!!.hour, homework.reminder!!.minute)
                ),
                title = homework.title,
                reminderId = newHomeworkId.toInt()
            )
            _state.update { it.copy(showSavingLoading = false, showHomeworkSavedDialog = true) } // Menampilkan dialog tugas tersimpan
        } catch (e: MissingRequiredFieldException) { // Menangani pengecualian jika ada kolom yang kosong
            _state.update { it.copy(showSavingLoading = false) }
            val errorMessage = when (e) {
                is MissingRequiredFieldException.Title -> UIText.StringResource(R.string.homework_title_is_required) // Menampilkan pesan error judul
                is MissingRequiredFieldException.Date -> UIText.StringResource(R.string.due_date_is_required) // Menampilkan pesan error tanggal
                is MissingRequiredFieldException.Subject -> UIText.StringResource(R.string.subject_is_required) // Menampilkan pesan error subjek
            }
            _state.update { it.copy(errorMessage = errorMessage) }
        }
    }

    // Fungsi untuk menangani perubahan judul tugas
    private fun onHomeworkTitleChanged(title: String) {
        _state.update { it.copy(homeworkTitle = title) }
    }

    // Fungsi untuk menangani perubahan deskripsi ujian
    private fun onExamDescriptionChanged(description: String) {
        _state.update { it.copy(description = description) }
    }

    // Fungsi untuk menangani tanggal yang dipilih
    private fun onDateSelected(date: Date) {
        _state.update { it.copy(date = date) }
    }

    // Fungsi untuk menangani waktu yang dipilih
    private fun onTimeSelected(time: Time) {
        _state.update { it.copy(time = time) }
    }

    // Fungsi untuk menangani waktu deadline yang dipilih
    private fun onDeadlineTimeSelected(time: DeadlineTime) {
        _state.update { it.copy(times = time) }
    }

    // Fungsi untuk menangani subjek yang dipilih
    private fun onSubjectSelected(subject: Subject) {
        _state.update { it.copy(subject = subject) }
    }

    // Fungsi untuk menangani lampiran yang dikonfirmasi
    private fun onAttachmentsConfirmed(attachments: List<Attachment>) {
        _state.update { it.copy(attachments = attachments) }
    }

    // Fungsi inisialisasi untuk mengambil data homework jika ada dan mengambil semua subjek
    init {
        if (homeworkId != -1) {
            viewModelScope.launch {
                eventRepository.getHomeworkById(homeworkId).collect { homeworkDto ->
                    if (homeworkDto == null) return@collect
                    _state.update {
                        it.copy(
                            isEditMode = true,
                            homeworkTitle = homeworkDto.homework.title,
                            date = homeworkDto.homework.dueDate,
                            time = homeworkDto.homework.reminder,
                            times = homeworkDto.homework.deadline,
                            subject = homeworkDto.subject,
                            attachments = homeworkDto.homework.attachments,
                            isCompleted = homeworkDto.homework.completed,
                            description = homeworkDto.homework.description
                        )
                    }
                }
            }
        }
        getAllSubjectJob = viewModelScope.launch {
            subjectRepository.getAllSubject().collect { subjects ->
                _state.update { it.copy(subjects = subjects) } // Menyimpan daftar subjek
            }
        }
    }

    // Fungsi untuk membersihkan resources saat ViewModel di-clear
    override fun onCleared() {
        getAllSubjectJob?.cancel() // Membatalkan job mengambil subjek
        getAllSubjectJob = null
        super.onCleared()
    }
}
