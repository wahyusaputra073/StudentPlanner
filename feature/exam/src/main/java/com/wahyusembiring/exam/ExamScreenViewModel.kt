package com.wahyusembiring.exam

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.launch
import com.wahyusembiring.common.util.scheduleReminder
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.EventRepository
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

// ViewModel untuk layar ExamScreen yang mengelola state UI dan event terkait ujian
@HiltViewModel(assistedFactory = ExamScreenViewModel.Factory::class)
class ExamScreenViewModel @AssistedInject constructor(
    @Assisted val examId: Int = -1, // ID ujian untuk edit mode
    private val eventRepository: EventRepository, // Repository untuk mengelola data ujian
    private val subjectRepository: SubjectRepository, // Repository untuk mengelola mata pelajaran
    private val application: Application // Akses ke aplikasi untuk keperluan pengingat
) : ViewModel() {

    // Factory untuk membuat instance ViewModel dengan examId sebagai parameter
    @AssistedFactory
    interface Factory {
        fun create(examId: Int = -1): ExamScreenViewModel
    }

    // State yang menyimpan status UI untuk layar ExamScreen
    private val _state = MutableStateFlow(ExamScreenUIState())
    val state = _state.asStateFlow()

    private var getAllSubjectJob: Job? = null // Job untuk mengambil daftar mata pelajaran

    // Fungsi untuk menangani event yang terjadi di UI
    fun onUIEvent(event: ExamScreenUIEvent) {
        when (event) {
            // Event untuk perubahan nama ujian
            is ExamScreenUIEvent.OnExamNameChanged -> onExamNameChanged(event.name)
            // Event untuk perubahan deskripsi ujian
            is ExamScreenUIEvent.OnExamDescriptionChanged -> onExamDescriptionChanged(event.name)
            // Event untuk klik picker tanggal ujian
            is ExamScreenUIEvent.OnExamDatePickerClick -> launch { onExamDatePickerClick() }
            // Event untuk klik picker waktu ujian
            is ExamScreenUIEvent.OnExamTimePickerClick -> launch { onExamTimePickerClick() }
            // Event untuk klik picker waktu deadline ujian
            is ExamScreenUIEvent.OnExamDeadlineTimePickerClick -> launch { onExamDeadlineTimePickerClick() }
            // Event untuk klik picker mata pelajaran
            is ExamScreenUIEvent.OnExamSubjectPickerClick -> launch { onExamSubjectPickerClick() }
            // Event untuk klik picker lampiran ujian
            is ExamScreenUIEvent.OnExamAttachmentPickerClick -> launch { onExamAttachmentPickerClick() }
            // Event untuk klik picker kategori ujian
            is ExamScreenUIEvent.OnExamCategoryPickerClick -> launch { onExamCategoryPickerClick() }
            // Event untuk klik tombol simpan ujian
            is ExamScreenUIEvent.OnSaveExamButtonClick -> launch { onSaveExamButtonClick() }
            // Event untuk lampiran yang dipilih
            is ExamScreenUIEvent.OnAttachmentPicked -> onAttachmentPicked(event.attachments)
            // Event untuk menutup popup lampiran
            is ExamScreenUIEvent.OnAttachmentPickedDismiss -> onAttachmentPickedDismiss()
            // Event untuk kategori yang dipilih
            is ExamScreenUIEvent.OnCategoryPicked -> onCategoryPicked(event.category)
            // Event untuk menutup popup kategori
            is ExamScreenUIEvent.OnCategoryPickedDismiss -> onCategoryPickedDismiss()
            // Event untuk tanggal yang dipilih
            is ExamScreenUIEvent.OnDatePicked -> onDatePicked(event.date)
            // Event untuk menutup popup tanggal
            is ExamScreenUIEvent.OnDatePickedDismiss -> onDatePickedDismiss()
            // Event untuk menutup dialog error
            is ExamScreenUIEvent.OnErrorDialogDismiss -> onErrorDialogDismiss()
            // Event untuk menutup dialog ujian tersimpan
            is ExamScreenUIEvent.OnExamSavedDialogDismiss -> onExamSavedDialogDismiss()
            // Event untuk menutup dialog konfirmasi simpan ujian
            is ExamScreenUIEvent.OnSaveConfirmationDialogDismiss -> onSaveConfirmationDialogDismiss()
            // Event untuk klik konfirmasi simpan ujian
            is ExamScreenUIEvent.OnSaveExamConfirmClick -> launch { onSaveExamConfirmClick() }
            // Event untuk mata pelajaran yang dipilih
            is ExamScreenUIEvent.OnSubjectPicked -> onSubjectPicked(event.subject)
            // Event untuk menutup popup mata pelajaran
            is ExamScreenUIEvent.OnSubjectPickedDismiss -> onSubjectPickedDismiss()
            // Event untuk waktu yang dipilih
            is ExamScreenUIEvent.OnTimePicked -> onTimePicked(event.time)
            // Event untuk waktu deadline yang dipilih
            is ExamScreenUIEvent.OnDeadlineTimePicked -> onDeadlineTimePicked(event.times)
            // Event untuk menutup popup waktu
            is ExamScreenUIEvent.OnTimePickedDismiss -> onTimePickedDismiss()
            // Event untuk menutup popup waktu deadline
            is ExamScreenUIEvent.OnDeadlineTimePickedDismiss -> onDeadlineTimePickedDismiss()
        }
    }

    // Fungsi untuk menutup picker waktu
    private fun onTimePickedDismiss() {
        _state.update { it.copy(showTimePicker = false) }
    }

    // Fungsi untuk menutup picker waktu deadline
    private fun onDeadlineTimePickedDismiss() {
        _state.update { it.copy(showDeadlineTimePicker = false) }
    }

    // Fungsi untuk menyimpan waktu deadline yang dipilih
    private fun onDeadlineTimePicked(times: DeadlineTime) {
        _state.update { it.copy(times = times) }
    }

    // Fungsi untuk menyimpan waktu yang dipilih
    private fun onTimePicked(time: Time) {
        _state.update { it.copy(time = time) }
    }

    // Fungsi untuk menutup picker mata pelajaran
    private fun onSubjectPickedDismiss() {
        _state.update { it.copy(showSubjectPicker = false) }
    }

    // Fungsi untuk menyimpan mata pelajaran yang dipilih
    private fun onSubjectPicked(subject: Subject) {
        _state.update { it.copy(subject = subject) }
    }

    // Fungsi untuk menutup dialog konfirmasi simpan ujian
    private fun onSaveConfirmationDialogDismiss() {
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    // Fungsi untuk menutup dialog ujian tersimpan
    private fun onExamSavedDialogDismiss() {
        _state.update { it.copy(showExamSavedDialog = false) }
    }

    // Fungsi untuk menutup dialog error
    private fun onErrorDialogDismiss() {
        _state.update { it.copy(errorMessage = null) }
    }

    // Fungsi untuk menutup picker tanggal
    private fun onDatePickedDismiss() {
        _state.update { it.copy(showDatePicker = false) }
    }

    // Fungsi untuk menyimpan tanggal yang dipilih
    private fun onDatePicked(date: Date) {
        _state.update { it.copy(date = date) }
    }

    // Fungsi untuk menutup picker kategori ujian
    private fun onCategoryPickedDismiss() {
        _state.update { it.copy(showCategoryPicker = false) }
    }

    // Fungsi untuk menyimpan kategori ujian yang dipilih
    private fun onCategoryPicked(category: ExamCategory) {
        _state.update { it.copy(category = category) }
    }

    // Fungsi untuk menutup picker lampiran ujian
    private fun onAttachmentPickedDismiss() {
        _state.update { it.copy(showAttachmentPicker = false) }
    }

    // Fungsi untuk menyimpan lampiran ujian yang dipilih
    private fun onAttachmentPicked(attachments: List<Attachment>) {
        _state.update { it.copy(attachments = attachments) }
    }

    // Inisialisasi: memuat data ujian jika examId ada dan memuat daftar mata pelajaran
    init {
        if (examId != -1) {
            viewModelScope.launch {
                eventRepository.getExamById(examId).collect { examWithSubject ->
                    if (examWithSubject == null) return@collect
                    _state.update {
                        it.copy(
                            isEditMode = true,
                            name = examWithSubject.exam.title,
                            date = examWithSubject.exam.date,
                            time = examWithSubject.exam.reminder,
                            times = examWithSubject.exam.deadline,
                            subject = examWithSubject.subject,
                            category = examWithSubject.exam.category,
                            score = examWithSubject.exam.score,
                            attachments = examWithSubject.exam.attachments,
                            description = examWithSubject.exam.description
                        )
                    }
                }
            }
        }
        // Memulai job untuk mengambil daftar mata pelajaran
        getAllSubjectJob = viewModelScope.launch {
            subjectRepository.getAllSubject().collect { subjects ->
                _state.update { it.copy(subjects = subjects) }
            }
        }
    }

    // Fungsi untuk menampilkan dialog konfirmasi simpan ujian
    private suspend fun onSaveExamButtonClick() {
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    // Fungsi untuk menyimpan atau mengupdate ujian
    private suspend fun onSaveExamConfirmClick() {
        _state.update { it.copy(showSavingLoading = true) }
        try {
            // Membuat objek Exam dari state UI
            val exam = Exam(
                id = if (examId != -1) examId else 0,
                title = _state.value.name.ifBlank { throw MissingRequiredFieldException.Title() },
                date = _state.value.date ?: throw MissingRequiredFieldException.Date(),
                reminder = _state.value.time ?: throw MissingRequiredFieldException.Time(),
                deadline = _state.value.times ?: throw MissingRequiredFieldException.Times(),
                subjectId = _state.value.subject?.id ?: throw MissingRequiredFieldException.Subject(),
                category = _state.value.category,
                description = _state.value.description,
                attachments = _state.value.attachments,
                score = _state.value.score
            )
            val newExamId = if (examId == -1) {
                eventRepository.saveExam(exam)
            } else {
                eventRepository.updateExam(exam)
                examId
            }
            // Menjadwalkan pengingat untuk ujian yang baru disimpan
            scheduleReminder(
                context = application.applicationContext,
                localDateTime = LocalDateTime.of(
                    LocalDate.ofInstant(exam.date.toInstant(), ZoneId.systemDefault()),
                    LocalTime.of(exam.reminder!!.hour, exam.reminder!!.minute)
                ),
                title = exam.title,
                reminderId = newExamId.toInt()
            )
            _state.update {
                it.copy(
                    showSavingLoading = false,
                    showExamSavedDialog = true
                )
            }
        } // Menangani exception MissingRequiredFieldException yang terjadi saat ada field wajib yang kosong
        catch (e: MissingRequiredFieldException) {
            // Menghentikan tampilan loading saat terjadi error
            _state.update { it.copy(showSavingLoading = false) }
            // Menghentikan loading dan menampilkan pesan error jika ada field yang hilang
            val errorMessage = when (e) {
                is MissingRequiredFieldException.Date -> UIText.StringResource(R.string.date_cannot_be_empty)
                is MissingRequiredFieldException.Subject -> UIText.StringResource(R.string.subject_cannot_be_empty)
                is MissingRequiredFieldException.Time -> UIText.StringResource(R.string.time_cannot_be_empty)
                is MissingRequiredFieldException.Times -> UIText.StringResource(R.string.deadline_time_cannot_be_empty)
                is MissingRequiredFieldException.Title -> UIText.StringResource(R.string.exam_title_cannot_be_empty)
            }
            // Memperbarui state dengan pesan error yang sesuai
            _state.update { it.copy(errorMessage = errorMessage) }
        }
    }

    // Fungsi untuk mengubah nama ujian
    private fun onExamNameChanged(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    // Fungsi untuk mengubah deskripsi ujian
    private fun onExamDescriptionChanged(description: String) {
        _state.value = _state.value.copy(description = description)
    }

    // Fungsi untuk membuka picker tanggal ujian
    private fun onExamDatePickerClick() {
        _state.update { it.copy(showDatePicker = true) }
    }

    // Fungsi untuk membuka picker waktu ujian
    private fun onExamTimePickerClick() {
        _state.update { it.copy(showTimePicker = true) }
    }

    // Fungsi untuk membuka picker waktu deadline ujian
    private fun onExamDeadlineTimePickerClick() {
        _state.update { it.copy(showDeadlineTimePicker = true) }
    }

    // Fungsi untuk membuka picker mata pelajaran
    private fun onExamSubjectPickerClick() {
        _state.update { it.copy(showSubjectPicker = true) }
    }

    // Fungsi untuk membuka picker kategori ujian
    private fun onExamCategoryPickerClick() {
        _state.update { it.copy(showCategoryPicker = true) }
    }

    // Fungsi untuk membuka picker lampiran ujian
    private fun onExamAttachmentPickerClick() {
        _state.update { it.copy(showAttachmentPicker = true) }
    }

    // Menangani pembatalan atau pembersihan resource ketika ViewModel dihancurkan
    override fun onCleared() {
        getAllSubjectJob?.cancel()
        getAllSubjectJob = null
        super.onCleared()
    }
}