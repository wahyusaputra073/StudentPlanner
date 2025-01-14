package com.wahyusembiring.data.repository

import com.wahyusembiring.data.model.entity.Reminder
import kotlinx.coroutines.flow.Flow

// Mendeklarasikan interface ReminderRepository yang berfungsi untuk mengelola data pengingat (reminder)
interface ReminderRepository {

    // Mendeklarasikan fungsi untuk mengambil semua pengingat dalam bentuk Flow yang berisi daftar objek Reminder
    // Fungsi ini menerima parameter optional minDate dan maxDate untuk memfilter pengingat berdasarkan rentang tanggal
    fun getAllReminder(
        minDate: Long? = null, // Tanggal minimal (opsional)
        maxDate: Long? = null  // Tanggal maksimal (opsional)
    ): Flow<List<Reminder>>

    // Mendeklarasikan fungsi untuk mengambil pengingat berdasarkan ID dalam bentuk Flow yang berisi objek Reminder atau null
    fun getReminderById(id: Int): Flow<Reminder?>

    // Fungsi suspend untuk menyimpan data pengingat dan mengembalikan ID pengingat yang disimpan
    suspend fun saveReminder(reminder: Reminder): Long

    // Fungsi suspend untuk memperbarui data pengingat
    suspend fun updateReminder(reminder: Reminder)

    // Fungsi suspend untuk menghapus data pengingat
    suspend fun deleteReminder(reminder: Reminder)

}