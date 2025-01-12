package com.wahyusembiring.data.remote


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wahyusembiring.data.exception.UserIsNotSignInException
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.model.ThesisWithTask
import com.wahyusembiring.data.remote.util.USER_COLLECTION_ID
import com.wahyusembiring.data.remote.util.toHashMap
import com.wahyusembiring.data.remote.util.toThesisWithTask
import com.wahyusembiring.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ThesisService @Inject constructor(  // Kelas ThesisService dengan dependensi injection untuk Converter dan AuthRepository
    private val converter: Converter,  // Converter untuk mengonversi data
    private val authRepository: AuthRepository  // AuthRepository untuk autentikasi
) {

    companion object {
        private const val THESIS_COLLECTION_ID = "thesis"  // ID koleksi thesis di Firestore
    }

    private val db by lazy { Firebase.firestore }  // Lazy initialization untuk instance Firebase Firestore

    suspend fun getAllThesisWithTask(): List<ThesisWithTask> {  // Fungsi untuk mengambil semua data thesis dengan task
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login, atau throw exception jika tidak ada
        val query = db  // Mendapatkan snapshot koleksi 'thesis' untuk user tertentu
            .collection(USER_COLLECTION_ID)
            .document(user.id)
            .collection(THESIS_COLLECTION_ID)
            .get()
            .await()  // Mengambil data dari Firestore
        return query.documents.map { it.toThesisWithTask(converter) }  // Mengonversi setiap document menjadi objek ThesisWithTask
    }

    suspend fun saveThesisWithTask(thesisWithTask: ThesisWithTask) {  // Fungsi untuk menyimpan thesis dengan task
        val user = authRepository.currentUser.first() ?: throw UserIsNotSignInException()  // Mengambil user yang sedang login
        db.collection(USER_COLLECTION_ID)  // Mendapatkan referensi koleksi dan dokumen untuk menyimpan thesis
            .document(user.id)
            .collection(THESIS_COLLECTION_ID).document(thesisWithTask.thesis.id.toString())  // Menyimpan thesis berdasarkan ID
            .set(thesisWithTask.toHashMap(converter))  // Mengonversi objek ThesisWithTask ke HashMap dan menyimpannya
            .await()  // Menunggu hingga operasi selesai
    }
}