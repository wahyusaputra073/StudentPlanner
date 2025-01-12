package com.wahyusembiring.reminder

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.util.launch
import com.wahyusembiring.common.util.scheduleReminder
import com.wahyusembiring.data.model.Attachment
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.data.model.entity.Reminder
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.ui.util.UIText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@HiltViewModel(assistedFactory = CreateReminderScreenViewModel.Factory::class)
class CreateReminderScreenViewModel @AssistedInject constructor(
    @Assisted val reminderId: Int = -1, // ID pengingat yang sedang diolah, default -1 jika tidak ada
    private val eventRepository: EventRepository, // Repository untuk mengelola data terkait event/pengingat
    private val application: Application // Referensi aplikasi untuk mendapatkan konteks atau resource
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        // Factory untuk membuat instance ViewModel dengan parameter reminderId
        fun create(reminderId: Int = -1): CreateReminderScreenViewModel
    }

    // State internal untuk menyimpan data UI yang dapat berubah
    private val _state = MutableStateFlow(CreateReminderScreenUIState())
    // State eksternal yang hanya dapat diobservasi, tidak dapat diubah dari luar
    val state = _state.asStateFlow()


    init {
        // Mengecek apakah reminderId bukan -1, jika iya berarti ada pengingat yang sedang diedit
        if (reminderId != -1) {
            // Melakukan pengambilan data pengingat dari repository secara asinkron
            viewModelScope.launch {
                // Mengambil pengingat berdasarkan ID dan melakukan pengolahan hasilnya
                eventRepository.getReminderById(reminderId).collect { reminderDto ->
                    // Jika data pengingat tidak ditemukan, hentikan proses
                    if (reminderDto == null) return@collect
                    // Memperbarui state dengan data pengingat yang diterima
                    _state.update {
                        it.copy(
                            isEditMode = true, // Menandakan mode edit aktif
                            title = reminderDto.title, // Menetapkan judul pengingat
                            date = reminderDto.date, // Menetapkan tanggal pengingat
                            time = reminderDto.time, // Menetapkan waktu pengingat
                            spanTime = reminderDto.duration, // Menetapkan durasi pengingat
                            color = reminderDto.color, // Menetapkan warna pengingat
                            attachments = reminderDto.attachments, // Menetapkan lampiran pengingat
                            description = reminderDto.description, // Menetapkan deskripsi pengingat
                            isCompleted = reminderDto.completed // Menetapkan status selesai
                        )
                    }
                }
            }
        }
    }


    fun onUIEvent(event: CreateReminderScreenUIEvent) {
        // Menangani berbagai event UI untuk mengubah state aplikasi
        when (event) {
            is CreateReminderScreenUIEvent.OnTitleChanged -> onTitleChanged(event.title) // Mengubah judul pengingat
            is CreateReminderScreenUIEvent.OnReminderDescriptionChanged -> onReminderDescriptionChanged(event.title) // Mengubah deskripsi pengingat
            is CreateReminderScreenUIEvent.OnDatePickerButtonClick -> launch { onDatePickerButtonClick() } // Menampilkan date picker
            is CreateReminderScreenUIEvent.OnTimePickerButtonClick -> launch { onTimePickerButtonClick() } // Menampilkan time picker
            is CreateReminderScreenUIEvent.OnTimePickerDismiss -> onTimePickerDismiss() // Menutup time picker
            is CreateReminderScreenUIEvent.OnDurationTimePicker -> launch { onDurationTimePicker() } // Menampilkan duration time picker
            is CreateReminderScreenUIEvent.OnDurationTimePicked -> onDurationTimePicked(event.span) // Menangani pemilihan durasi waktu
            is CreateReminderScreenUIEvent.OnDurationTimePickerDismiss -> onDurationTimePickerDismiss() // Menutup duration time picker
            is CreateReminderScreenUIEvent.OnColorPickerButtonClick -> launch { onColorPickerButtonClick() } // Menampilkan color picker
            is CreateReminderScreenUIEvent.OnAttachmentPickerButtonClick -> launch { onAttachmentPickerButtonClick() } // Menampilkan attachment picker
            is CreateReminderScreenUIEvent.OnSaveButtonClicked -> launch { onSaveButtonClicked() } // Menyimpan pengingat
            is CreateReminderScreenUIEvent.OnAttachmentPicked -> onAttachmentPicked(event.attachments) // Menambahkan lampiran yang dipilih
            is CreateReminderScreenUIEvent.OnAttachmentPickerDismiss -> onAttachmentPickerDismiss() // Menutup attachment picker
            is CreateReminderScreenUIEvent.OnColorPicked -> onColorPicked(event.color) // Menetapkan warna yang dipilih
            is CreateReminderScreenUIEvent.OnColorPickerDismiss -> onColorPickerDismiss() // Menutup color picker
            is CreateReminderScreenUIEvent.OnDatePicked -> onDatePicked(event.date) // Menetapkan tanggal yang dipilih
            is CreateReminderScreenUIEvent.OnDatePickerDismiss -> onDatePickerDismiss() // Menutup date picker
            is CreateReminderScreenUIEvent.OnErrorDialogDismiss -> onErrorDialogDismiss() // Menutup error dialog
            is CreateReminderScreenUIEvent.OnReminderSavedDialogDismiss -> onReminderSavedDialogDismiss() // Menutup reminder saved dialog
            is CreateReminderScreenUIEvent.OnSaveConfirmationDialogDismiss -> onSaveConfirmationDialogDismiss() // Menutup save confirmation dialog
            is CreateReminderScreenUIEvent.OnSaveReminderConfirmClick -> launch { onSaveReminderConfirmClick() } // Konfirmasi penyimpanan pengingat
            is CreateReminderScreenUIEvent.OnTimePicked -> onTimePicked(event.time) // Menetapkan waktu yang dipilih
        }
    }

    private fun onTimePickerDismiss() {
        // Menutup time picker dan mengubah state showTimePicker menjadi false
        _state.update { it.copy(showTimePicker = false) }
    }

    private fun onTimePicked(time: Time) {
        // Memperbarui waktu yang dipilih dan menyimpannya dalam state
        _state.update { it.copy(time = time) }
    }

    private fun onDurationTimePickerDismiss() {
        // Menutup duration time picker dan mengubah state showDuraPicker menjadi false
        _state.update { it.copy(showDuraPicker = false) }
    }

    private fun onDurationTimePicker() {
        // Menampilkan duration time picker dengan mengubah state showDuraPicker menjadi true
        _state.update { it.copy(showDuraPicker = true) }
    }

    private fun onDurationTimePicked(span: SpanTime) {
        // Memperbarui span waktu yang dipilih dan menyimpannya dalam state
        _state.update { it.copy(spanTime = span) }
    }

    private fun onSaveConfirmationDialogDismiss() {
        // Menutup save confirmation dialog dengan mengubah state showSaveConfirmationDialog menjadi false
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    private fun onReminderSavedDialogDismiss() {
        // Menutup reminder saved dialog dengan mengubah state showReminderSavedDialog menjadi false
        _state.update { it.copy(showReminderSavedDialog = false) }
    }

    private fun onErrorDialogDismiss() {
        // Menghapus pesan error dan mengubah state errorMessage menjadi null
        _state.update { it.copy(errorMessage = null) }
    }

    private fun onDatePickerDismiss() {
        // Menutup date picker dan mengubah state showDatePicker menjadi false
        _state.update { it.copy(showDatePicker = false) }
    }

    private fun onDatePicked(date: Date) {
        // Memperbarui tanggal yang dipilih dan menyimpannya dalam state
        _state.update { it.copy(date = date) }
    }

    private fun onColorPickerDismiss() {
        // Menutup color picker dan mengubah state showColorPicker menjadi false
        _state.update { it.copy(showColorPicker = false) }
    }

    private fun onColorPicked(color: Color) {
        // Memperbarui warna yang dipilih dan menyimpannya dalam state
        _state.update { it.copy(color = color) }
    }

    private fun onAttachmentPickerDismiss() {
        // Menutup attachment picker dan mengubah state showAttachmentPicker menjadi false
        _state.update { it.copy(showAttachmentPicker = false) }
    }

    private fun onAttachmentPicked(attachments: List<Attachment>) {
        // Memperbarui lampiran yang dipilih dan menyimpannya dalam state
        _state.update { it.copy(attachments = attachments) }
    }

    private fun onSaveButtonClicked() {
        // Menampilkan dialog konfirmasi penyimpanan dengan mengubah state showSaveConfirmationDialog menjadi true
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    private suspend fun onSaveReminderConfirmClick() {
        // Menampilkan loading saat proses penyimpanan berlangsung
        _state.update { it.copy(showSavingLoading = true) }
        try {
            // Membuat objek Reminder dengan data dari UI state
            val reminder = Reminder(
                id = if (reminderId != -1) reminderId else 0,
                title = _state.value.title.ifBlank { throw MissingRequiredFieldException.Title() }, // Validasi title
                date = _state.value.date ?: throw MissingRequiredFieldException.Date(), // Validasi date
                time = _state.value.time ?: throw MissingRequiredFieldException.Time(), // Validasi time
                duration = _state.value.spanTime ?: throw MissingRequiredFieldException.Range(), // Validasi duration
                color = _state.value.color,
                attachments = _state.value.attachments,
                description = _state.value.description,
                completed = _state.value.isCompleted
            )
            // Menyimpan atau memperbarui reminder berdasarkan ID yang ada
            val savedReminderId = if (reminderId == -1) {
                eventRepository.saveReminder(reminder) // Menyimpan reminder baru
            } else {
                eventRepository.updateReminder(reminder) // Memperbarui reminder yang sudah ada
                reminderId
            }
            // Menjadwalkan reminder di perangkat
            scheduleReminder(
                context = application.applicationContext,
                localDateTime = LocalDateTime.of(
                    LocalDate.ofInstant(reminder.date.toInstant(), ZoneId.systemDefault()),
                    LocalTime.of(reminder.time.hour, reminder.time.minute)
                ),
                title = reminder.title,
                reminderId = savedReminderId.toInt()
            )
            // Menyembunyikan loading dan menampilkan dialog reminder tersimpan
            _state.update {
                it.copy(
                    showSavingLoading = false,
                    showReminderSavedDialog = true
                )
            }
        } catch (e: MissingRequiredFieldException) {
            // Menghentikan loading dan menampilkan pesan error jika ada field yang hilang
            _state.update { it.copy(showSavingLoading = false) }
            val errorMessage = when (e) {
                is MissingRequiredFieldException.Title -> UIText.StringResource(R.string.title_is_required)
                is MissingRequiredFieldException.Date -> UIText.StringResource(R.string.date_is_required)
                is MissingRequiredFieldException.Time -> UIText.StringResource(R.string.time_is_required)
                is MissingRequiredFieldException.Range -> UIText.StringResource(R.string.range_is_required)
            }
            // Memperbarui state dengan pesan error yang sesuai
            _state.update { it.copy(errorMessage = errorMessage) }
        }
    }

    private fun onTitleChanged(title: String) {
        // Memperbarui state dengan title baru yang dimasukkan
        _state.update {
            it.copy(title = title)
        }
    }

    private fun onReminderDescriptionChanged(description: String) {
        // Memperbarui state dengan deskripsi baru yang dimasukkan
        _state.update {
            it.copy(description = description)
        }
    }

    private fun onDatePickerButtonClick() {
        // Menampilkan date picker saat tombol di klik
        _state.update { it.copy(showDatePicker = true) }
    }

    private fun onTimePickerButtonClick() {
        // Menampilkan time picker saat tombol di klik
        _state.update { it.copy(showTimePicker = true) }
    }

    private fun onColorPickerButtonClick() {
        // Menampilkan color picker saat tombol di klik
        _state.update { it.copy(showColorPicker = true) }
    }

    private fun onAttachmentPickerButtonClick() {
        // Menampilkan attachment picker saat tombol di klik
        _state.update { it.copy(showAttachmentPicker = true) }
    }
}