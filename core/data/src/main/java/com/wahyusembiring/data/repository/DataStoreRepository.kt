package com.wahyusembiring.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {  // Interface untuk mengelola penyimpanan data menggunakan DataStore

    suspend fun saveOnBoardingState(completed: Boolean)  // Fungsi untuk menyimpan status penyelesaian onboarding (true/false)

    fun readOnBoardingState(): Flow<Boolean>  // Fungsi untuk membaca status penyelesaian onboarding yang disimpan, mengembalikan Flow dengan nilai Boolean
}
