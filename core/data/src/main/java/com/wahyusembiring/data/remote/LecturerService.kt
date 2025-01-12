package com.wahyusembiring.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wahyusembiring.data.exception.UserIsNotSignInException
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.remote.util.USER_COLLECTION_ID
import com.wahyusembiring.data.remote.util.toHashMap
import com.wahyusembiring.data.remote.util.toLecturer
import com.wahyusembiring.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LecturerService @Inject constructor(  // Kelas LecturerService dengan dependensi injection untuk Converter dan AuthRepository
    private val converter: Converter,  // Converter untuk mengonversi data
    private val authRepository: AuthRepository  // AuthRepository untuk autentikasi
) {

    companion object {
        private const val LECTURER_COLLECTION_ID = "lecturer"  // ID koleksi lecturer di Firestore
    }

    private val db by lazy { Firebase.firestore }  // Lazy initialization untuk instance Firebase Firestore

    suspend fun getAllLecturer(): List<Lecturer> {  // Fungsi untuk mengambil semua data lecturer
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login, atau throw exception jika tidak ada
        val query = db  // Mendapatkan snapshot koleksi 'lecturer' untuk user tertentu
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(LECTURER_COLLECTION_ID)
            .get()
            .await()  // Mengambil data dari Firestore
        return query.documents.map { it.toLecturer(converter) }  // Mengonversi setiap document menjadi objek Lecturer
    }

    suspend fun saveLecturer(lecturer: Lecturer) {  // Fungsi untuk menyimpan lecturer
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login
        val newLecturer = lecturer.toHashMap(converter)  // Mengonversi objek Lecturer ke HashMap
        db.collection(USER_COLLECTION_ID)  // Mendapatkan referensi koleksi dan dokumen untuk menyimpan lecturer
            .document(user.id)
            .collection(LECTURER_COLLECTION_ID)
            .document(lecturer.id.toString()).set(newLecturer)  // Menyimpan lecturer berdasarkan ID
            .await()  // Menunggu hingga operasi selesai
    }
}
