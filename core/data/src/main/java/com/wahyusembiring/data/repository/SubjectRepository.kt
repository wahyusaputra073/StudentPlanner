package com.wahyusembiring.data.repository

import com.wahyusembiring.data.model.SubjectWithExam
import com.wahyusembiring.data.model.SubjectWithExamAndHomework
import com.wahyusembiring.data.model.SubjectWithLecturer
import com.wahyusembiring.data.model.entity.Subject
import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface SubjectRepository yang berfungsi untuk mengelola data mata kuliah (subject)
interface SubjectRepository {

    // Mendeklarasikan fungsi untuk mengambil semua mata kuliah dalam bentuk Flow yang berisi daftar objek Subject
    fun getAllSubject(): Flow<List<Subject>>

    // Mendeklarasikan fungsi untuk mengambil mata kuliah beserta dosennya berdasarkan ID dalam bentuk Flow yang berisi objek SubjectWithLecturer atau null
    fun getSubjectWithLecturerById(id: Int): Flow<SubjectWithLecturer?>

    // Mendeklarasikan fungsi untuk mengambil semua mata kuliah beserta ujian yang terkait dalam bentuk Flow yang berisi daftar objek SubjectWithExam
    fun getAllSubjectWithExam(): Flow<List<SubjectWithExam>>

    // Mendeklarasikan fungsi untuk mengambil semua mata kuliah beserta ujian dan tugas rumah dalam bentuk Flow yang berisi daftar objek SubjectWithExamAndHomework
    fun getAllSubjectWithExamAndHomework(): Flow<List<SubjectWithExamAndHomework>>

    // Fungsi untuk mengambil semua mata kuliah beserta ujian dan tugas rumah yang sudah dinilai, dalam bentuk Flow yang berisi daftar objek SubjectWithExamAndHomework
    fun getAllSubjectWithExamAndHomework(scored: Boolean): Flow<List<SubjectWithExamAndHomework>>

    // Fungsi suspend untuk menyimpan data mata kuliah
    suspend fun saveSubject(subject: Subject)

    // Fungsi suspend untuk memperbarui data mata kuliah
    suspend fun updateSubject(subject: Subject)

    // Fungsi suspend untuk menghapus mata kuliah berdasarkan ID
    suspend fun onDeleteSubject(id: Subject)

}