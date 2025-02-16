package com.wahyusembiring.data.repository

import com.wahyusembiring.data.model.entity.TaskThesis
import com.wahyusembiring.data.model.entity.Thesis
import com.wahyusembiring.data.model.ThesisWithTask
import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface ThesisRepository yang berfungsi untuk mengelola data skripsi dan tugas terkait
interface ThesisRepository {

    // Mendeklarasikan fungsi untuk mengambil semua skripsi beserta tugas terkait dalam bentuk Flow yang berisi daftar objek ThesisWithTask
    fun getAllThesis(): Flow<List<ThesisWithTask>>

    // Mendeklarasikan fungsi untuk mengambil skripsi berdasarkan ID dalam bentuk Flow yang berisi objek ThesisWithTask
    fun getThesisById(id: Int): Flow<ThesisWithTask>

    // Fungsi suspend untuk menyimpan data skripsi baru dan mengembalikan ID skripsi yang disimpan
    suspend fun saveNewThesis(thesis: Thesis): Long

    // Fungsi suspend untuk memperbarui data skripsi
    suspend fun updateThesis(thesis: Thesis)

    // Fungsi suspend untuk memperbarui judul skripsi berdasarkan ID
    suspend fun updateThesisTitleById(id: Int, title: String)

    // Fungsi suspend untuk menghapus data skripsi
    suspend fun deleteThesis(thesis: Thesis)

    // Fungsi suspend untuk menambahkan tugas baru pada skripsi dan mengembalikan ID tugas yang disimpan
    suspend fun addNewTask(taskThesis: TaskThesis): Long

    // Fungsi suspend untuk menghapus tugas terkait skripsi
    suspend fun deleteTask(taskThesis: TaskThesis)

    // Fungsi suspend untuk mengubah status tugas (selesai atau belum) pada skripsi
    suspend fun changeTaskCompletedStatus(taskThesis: TaskThesis, isCompleted: Boolean)

}