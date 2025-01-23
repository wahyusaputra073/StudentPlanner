package com.wahyusembiring.data.repository

import kotlinx.coroutines.flow.Flow
import com.wahyusembiring.data.Result

// Mendeklarasikan interface MainRepository yang berfungsi untuk sinkronisasi data antara lokal dan cloud
interface MainRepository {

    // Fungsi suspend untuk melakukan sinkronisasi data ke lokal dan mengembalikan hasil dalam bentuk Flow berisi Result<Unit>
    suspend fun syncToLocal(): Flow<Result<Unit>>

    // Fungsi suspend untuk melakukan sinkronisasi data ke cloud dan mengembalikan hasil dalam bentuk Flow berisi Result<Unit>
    suspend fun syncToCloud(): Flow<Result<Unit>>

}