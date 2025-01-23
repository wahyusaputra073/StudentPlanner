package com.wahyusembiring.data.repository

import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface EventRepository yang meng-extend tiga interface lain: HomeworkRepository, ExamRepository, dan ReminderRepository
interface EventRepository : HomeworkRepository, ExamRepository, ReminderRepository {

    // Mendeklarasikan fungsi untuk mengambil semua event dalam bentuk Flow yang berisi daftar objek Any
    fun getAllEvent(): Flow<List<Any>>

}