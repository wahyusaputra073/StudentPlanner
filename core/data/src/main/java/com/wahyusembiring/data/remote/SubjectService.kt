package com.wahyusembiring.data.remote

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wahyusembiring.data.exception.UserIsNotSignInException
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.remote.util.USER_COLLECTION_ID
import com.wahyusembiring.data.remote.util.toHashMap
import com.wahyusembiring.data.remote.util.toSubject
import com.wahyusembiring.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SubjectService @Inject constructor(  // Kelas SubjectService dengan dependensi injection untuk Converter dan AuthRepository
    private val converter: Converter,  // Converter untuk mengonversi data
    private val authRepository: AuthRepository  // AuthRepository untuk autentikasi
) {
    companion object {
        const val SUBJECT_COLLECTION_ID = "subject"  // ID koleksi subject di Firestore
    }

    private val db by lazy { Firebase.firestore }  // Lazy initialization untuk instance Firebase Firestore

    suspend fun getAllSubject(): List<Subject> {  // Fungsi untuk mengambil semua data subject
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login, atau throw exception jika tidak ada
        val querySnapshot = db  // Mendapatkan snapshot koleksi 'subject' untuk user tertentu
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(SUBJECT_COLLECTION_ID).get().await()  // Mengambil data dari Firestore
        return querySnapshot.documents.map { it.toSubject(converter) }  // Mengonversi setiap document menjadi objek Subject
    }

    suspend fun saveSubject(subject: Subject) {  // Fungsi untuk menyimpan subject
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login
        val newSubject = subject.toHashMap(converter)  // Mengonversi objek Subject ke HashMap
        val document = db  // Mendapatkan referensi koleksi dan dokumen untuk menyimpan subject
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(SUBJECT_COLLECTION_ID).document(subject.id.toString())  // Menyimpan subject berdasarkan ID
        document
            .set(newSubject)  // Menyimpan data subject di Firestore
            .await()  // Menunggu hingga operasi selesai
    }
}