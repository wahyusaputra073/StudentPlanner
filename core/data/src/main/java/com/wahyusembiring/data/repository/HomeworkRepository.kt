package com.wahyusembiring.data.repository

import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Task
import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface HomeworkRepository yang berfungsi untuk mengelola data tugas rumah (homework)
interface HomeworkRepository {

    // Mendeklarasikan fungsi untuk mengambil semua tugas rumah beserta subjeknya dalam bentuk Flow yang berisi daftar objek HomeworkWithSubject
    // Fungsi ini menerima parameter optional minDate dan maxDate untuk memfilter tugas berdasarkan rentang tanggal
    fun getAllHomeworkWithSubject(
        minDate: Long? = null, // Tanggal minimal (opsional)
        maxDate: Long? = null  // Tanggal maksimal (opsional)
    ): Flow<List<HomeworkWithSubject>>

    // Mendeklarasikan fungsi untuk mengambil tugas rumah berdasarkan ID dalam bentuk Flow yang berisi objek HomeworkWithSubject atau null
    fun getHomeworkById(id: Int): Flow<HomeworkWithSubject?>

    // Fungsi suspend untuk menyimpan data tugas rumah dan mengembalikan ID tugas yang disimpan
    suspend fun saveHomework(homework: Task): Long

    // Fungsi suspend untuk memperbarui data tugas rumah
    suspend fun updateHomework(homework: Task)

    // Fungsi suspend untuk menghapus data tugas rumah
    suspend fun deleteHomework(homework: Task)

}