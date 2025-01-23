package com.wahyusembiring.data.repository

import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.entity.Exam
import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface ExamRepository yang berfungsi untuk mengelola data ujian
interface ExamRepository {

    // Mendeklarasikan fungsi untuk mengambil semua ujian beserta subjeknya dalam bentuk Flow yang berisi daftar objek ExamWithSubject
    fun getAllExamWithSubject(): Flow<List<ExamWithSubject>>

    // Mendeklarasikan fungsi untuk mengambil ujian berdasarkan ID dalam bentuk Flow yang berisi objek ExamWithSubject atau null
    fun getExamById(id: Int): Flow<ExamWithSubject?>

    // Fungsi suspend untuk menyimpan data ujian dan mengembalikan ID ujian yang disimpan
    suspend fun saveExam(exam: Exam): Long

    // Fungsi suspend untuk memperbarui data ujian
    suspend fun updateExam(exam: Exam)

    // Fungsi suspend untuk menghapus data ujian
    suspend fun deleteExam(exam: Exam)

}
