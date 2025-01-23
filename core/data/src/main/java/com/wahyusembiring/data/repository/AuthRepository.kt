package com.wahyusembiring.data.repository

import android.content.Context
import androidx.activity.result.ActivityResultRegistryOwner
import com.wahyusembiring.data.model.User
import kotlinx.coroutines.flow.Flow
import com.wahyusembiring.data.Result

interface AuthRepository {  // Interface yang mendefinisikan operasi autentikasi pengguna

    val currentUser: Flow<User?>  // Flow untuk mendapatkan user saat ini, bisa null jika belum ada user yang login

    fun createUserWithEmailAndPassword(email: String, password: String): Flow<Result<User>>  // Fungsi untuk membuat akun baru dengan email dan password

    fun signInAnonymously(): Flow<Result<User>>  // Fungsi untuk login tanpa menggunakan akun, login anonim

    fun signInWithEmailAndPassword(email: String, password: String): Flow<Result<User>>  // Fungsi untuk login menggunakan email dan password

    fun signInWithGoogle(context: Context): Flow<Result<User>>  // Fungsi untuk login menggunakan akun Google

    fun signInWithFacebook(activityResultRegistryOwner: ActivityResultRegistryOwner): Flow<Result<User>>  // Fungsi untuk login menggunakan akun Facebook

    fun logout(): Flow<Result<Unit>>  // Fungsi untuk logout, mengembalikan Flow dengan hasil Unit
}