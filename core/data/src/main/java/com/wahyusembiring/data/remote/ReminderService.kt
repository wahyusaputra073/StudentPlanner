package com.wahyusembiring.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wahyusembiring.data.exception.UserIsNotSignInException
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.data.remote.util.USER_COLLECTION_ID
import com.wahyusembiring.data.remote.util.toHashMap
import com.wahyusembiring.data.remote.util.toReminder
import com.wahyusembiring.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReminderService @Inject constructor(  // Kelas ReminderService dengan dependensi injection untuk Converter dan AuthRepository
    private val converter: Converter,  // Converter untuk mengonversi data
    private val authRepository: AuthRepository  // AuthRepository untuk autentikasi
) {

    companion object {
        private const val REMINDER_COLLECTION_ID = "reminder"  // ID koleksi reminder di Firestore
    }

    private val db by lazy { Firebase.firestore }  // Lazy initialization untuk instance Firebase Firestore

    suspend fun getAllReminder(): List<Agenda> {  // Fungsi untuk mengambil semua data reminder
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login, atau throw exception jika tidak ada
        val querySnapshot = db  // Mendapatkan snapshot koleksi 'reminder' untuk user tertentu
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(REMINDER_COLLECTION_ID).get().await()  // Mengambil data dari Firestore
        return querySnapshot.documents.map { it.toReminder(converter) }  // Mengonversi setiap document menjadi objek Reminder
    }

    suspend fun saveReminder(reminder: Agenda) {  // Fungsi untuk menyimpan reminder
        val newReminder = reminder.toHashMap(converter)  // Mengonversi objek Reminder ke HashMap
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login
        val document = db  // Mendapatkan referensi koleksi dan dokumen untuk menyimpan reminder
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(REMINDER_COLLECTION_ID)
            .document(reminder.id.toString())  // Menyimpan reminder berdasarkan ID
        document
            .set(newReminder)  // Menyimpan data reminder di Firestore
            .await()  // Menunggu hingga operasi selesai
    }
}