package com.wahyusembiring.data.remote


import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.wahyusembiring.data.exception.UserIsNotSignInException
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.entity.Homework
import com.wahyusembiring.data.remote.util.USER_COLLECTION_ID
import com.wahyusembiring.data.remote.util.toHashMap
import com.wahyusembiring.data.remote.util.toHomework
import com.wahyusembiring.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeworkService @Inject constructor(  // Kelas HomeworkService dengan dependensi injection untuk Converter dan AuthRepository
    private val converter: Converter,  // Converter untuk mengonversi data
    private val authRepository: AuthRepository  // AuthRepository untuk autentikasi
) {

    companion object {
        private const val HOMEWORK_COLLECTION_ID = "homework"  // ID koleksi homework di Firestore
    }

    private val db by lazy { Firebase.firestore }  // Lazy initialization untuk instance Firebase Firestore

    suspend fun getAllHomework(): List<Homework> {  // Fungsi untuk mengambil semua data homework
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login, atau throw exception jika tidak ada
        val querySnapshot = db  // Mendapatkan snapshot koleksi 'homework' untuk user tertentu
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(HOMEWORK_COLLECTION_ID).get().await()  // Mengambil data dari Firestore
        return querySnapshot.documents.map { it.toHomework(converter) }  // Mengonversi setiap document menjadi objek Homework
    }

    suspend fun saveHomework(homework: Homework) {  // Fungsi untuk menyimpan homework
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login
        val newHomework = homework.toHashMap(converter)  // Mengonversi objek Homework ke HashMap
        val document = db  // Mendapatkan referensi dokumen untuk menyimpan homework
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(HOMEWORK_COLLECTION_ID).document(homework.id.toString())  // Menyimpan homework berdasarkan ID
        document
            .set(newHomework)  // Menyimpan data homework di Firestore
            .await()  // Menunggu hingga operasi selesai
    }
}