package com.wahyusembiring.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Reminder
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.data.repository.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Menandai kelas ini sebagai ViewModel yang dikelola oleh Hilt untuk dependency injection
class CalendarScreenViewModel @Inject constructor( // ViewModel untuk layar kalender, menerima EventRepository sebagai dependensi
    private val eventRepository: EventRepository // Repository untuk mengambil dan memanipulasi data event
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarScreenUIState()) // State untuk UI kalender, mengandung list event
    val state = _state.asStateFlow() // Mengakses state UI secara tidak langsung untuk menghindari modifikasi langsung

    private val _navigationEvent = Channel<CalendarScreenNavigationEvent>() // Menyimpan event navigasi untuk dikirim ke UI
    val navigationEvent = _navigationEvent.receiveAsFlow() // Mengakses event navigasi sebagai flow

    private val events = eventRepository.getAllEvent() // Mengambil semua event dari repository

    init {
        viewModelScope.launch { // Mengambil data event secara asinkron saat ViewModel diinisialisasi
            events.collect { // Mendengarkan perubahan data event dari repository
                _state.update { uIstate -> // Memperbarui state UI jika ada perubahan
                    uIstate.copy(events = it) // Menyalin state UI yang lama dan memperbarui daftar event
                }
            }
        }
    }

    // Fungsi untuk menangani berbagai event UI
    fun onUIEvent(event: CalendarScreenUIEvent) {
        when (event) {
            is CalendarScreenUIEvent.OnDeleteEvent -> onDeleteEvent(event.event) // Menangani penghapusan event
            is CalendarScreenUIEvent.OnEventClick -> onEventClick(event.event) // Menangani klik pada event
            is CalendarScreenUIEvent.OnEventCompletedStateChange -> onEventCompletedStateChange(event.event, event.isChecked) // Menangani perubahan status selesai pada event
        }
    }

    // Fungsi untuk menghapus event
    private fun onDeleteEvent(event: Any) {
        viewModelScope.launch {
            when (event) {
                is HomeworkWithSubject -> {
                    eventRepository.deleteHomework(event.homework) // Menghapus tugas rumah
                }
                is ExamWithSubject -> {
                    eventRepository.deleteExam(event.exam) // Menghapus ujian
                }
                is Reminder -> {
                    eventRepository.deleteReminder(event) // Menghapus pengingat
                }
            }
        }
    }

    // Fungsi untuk mengubah status selesai pada event
    private fun onEventCompletedStateChange(event: Any, checked: Boolean) {
        viewModelScope.launch {
            when (event) {
                is HomeworkWithSubject -> {
                    eventRepository.updateHomework(event.homework.copy(completed = checked)) // Memperbarui status selesai tugas rumah
                }
                is ExamWithSubject -> {
                    eventRepository.updateExam(event.exam.copy()) // Memperbarui ujian (tidak ada perubahan spesifik dalam kode ini)
                }
                is Reminder -> {
                    eventRepository.updateReminder(event.copy(completed = checked)) // Memperbarui status selesai pengingat
                }
            }
        }
    }

    // Fungsi untuk menangani klik pada event
    private fun onEventClick(event: Any) {
        when (event) {
            is HomeworkWithSubject -> {
                _navigationEvent.trySend(CalendarScreenNavigationEvent.NavigateToHomeworkDetail(event.homework.id)) // Navigasi ke detail tugas rumah
            }
            is ExamWithSubject -> {
                _navigationEvent.trySend(CalendarScreenNavigationEvent.NavigateToExamDetail(event.exam.id)) // Navigasi ke detail ujian
            }
            is Reminder -> {
                _navigationEvent.trySend(CalendarScreenNavigationEvent.NavigateToReminderDetail(event.id)) // Navigasi ke detail pengingat
            }
        }
    }
}