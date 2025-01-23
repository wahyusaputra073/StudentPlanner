package com.wahyusembiring.data.repository

import com.wahyusembiring.data.model.LecturerWithSubject
import com.wahyusembiring.data.model.entity.Lecturer
import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface LecturerRepository yang berfungsi untuk mengelola data dosen
interface LecturerRepository {

    // Mendeklarasikan fungsi untuk mengambil semua dosen dalam bentuk Flow yang berisi daftar objek Lecturer
    fun getAllLecturer(): Flow<List<Lecturer>>

    // Mendeklarasikan fungsi untuk mengambil dosen berdasarkan ID dalam bentuk Flow yang berisi objek Lecturer atau null
    fun getLecturerById(id: Int): Flow<Lecturer?>

    // Mendeklarasikan fungsi untuk mengambil semua dosen beserta subjek yang diajar dalam bentuk Flow yang berisi daftar objek LecturerWithSubject
    fun getAllLecturerWithSubjects(): Flow<List<LecturerWithSubject>>

    // Fungsi suspend untuk menyimpan data dosen dan mengembalikan ID dosen yang disimpan
    suspend fun insertLecturer(lecturer: Lecturer): Long

    // Fungsi suspend untuk memperbarui data dosen
    suspend fun updateLecturer(lecturer: Lecturer)

    // Fungsi suspend untuk menghapus dosen berdasarkan ID
    suspend fun deleteLecturer(id: Int)

    // Fungsi suspend untuk menghapus nomor telepon dosen berdasarkan nomor telepon
    suspend fun deletePhoneNumber(phoneNumber: String)

}

