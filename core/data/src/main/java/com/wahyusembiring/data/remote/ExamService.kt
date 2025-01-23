package com.wahyusembiring.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.wahyusembiring.data.exception.UserIsNotSignInException
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.remote.util.USER_COLLECTION_ID
import com.wahyusembiring.data.remote.util.toExam
import com.wahyusembiring.data.remote.util.toHashMap
import com.wahyusembiring.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExamService @Inject constructor(  // Kelas ExamService dengan dependensi injection untuk Converter dan AuthRepository
    private val converter: Converter,  // Converter untuk mengonversi data
    private val authRepository: AuthRepository  // AuthRepository untuk autentikasi
) {

    companion object {
        private const val EXAM_COLLECTION_ID = "exam"  // ID koleksi exam di Firestore
    }

    private val db by lazy { Firebase.firestore }  // Lazy initialization untuk instance Firebase Firestore

    suspend fun getAllExam(): List<Exam> {  // Fungsi untuk mengambil semua data exam
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login, atau throw exception jika tidak ada
        val querySnapshot = db  // Mendapatkan snapshot koleksi 'exam' untuk user tertentu
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(EXAM_COLLECTION_ID).get().await()  // Mengambil data dari Firestore
        return querySnapshot.documents.map { it.toExam(converter) }  // Mengonversi setiap document menjadi objek Exam
    }

    suspend fun saveExam(exam: Exam) {  // Fungsi untuk menyimpan exam
        val newExam = exam.toHashMap(converter)  // Mengonversi objek Exam ke HashMap
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login
        val document = db  // Mendapatkan referensi dokumen untuk menyimpan exam
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(EXAM_COLLECTION_ID)
            .document(exam.id.toString())  // Menyimpan exam berdasarkan ID
        document
            .set(newExam)  // Menyimpan data exam di Firestore
            .await()  // Menunggu hingga operasi selesai
    }
}