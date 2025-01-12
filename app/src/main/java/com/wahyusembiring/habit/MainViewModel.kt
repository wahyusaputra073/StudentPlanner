package com.wahyusembiring.habit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.data.repository.AuthRepository
import com.wahyusembiring.data.repository.DataStoreRepository
import com.wahyusembiring.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

// Menandakan bahwa ViewModel ini akan menggunakan Hilt untuk dependency injection
@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,         // Repositori untuk otentikasi pengguna
    private val dataStoreRepository: DataStoreRepository, // Repositori untuk membaca/menulis preferensi lokal
    private val mainRepository: MainRepository          // Repositori utama untuk sinkronisasi data
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel" // Tag untuk logging
        private const val MINIMAL_SPLASH_SCREEN_DURATION = 3000L // Durasi minimum splash screen
    }

    // Menyimpan status apakah aplikasi siap digunakan
    private val _isAppReady = MutableStateFlow(false)
    val isAppReady: StateFlow<Boolean> = _isAppReady

    // Menyimpan tujuan awal aplikasi setelah splash screen
    private val _startDestination: MutableStateFlow<Screen> = MutableStateFlow(Screen.Blank)
    val startDestination: StateFlow<Screen> = _startDestination

    // Mengamati pengguna saat ini dari authRepository
    val currentUser = authRepository.currentUser.onEach {
        if (it != null) cloudToLocalSync() // Jika pengguna ada, sinkronkan data dari cloud ke lokal
    }

    // Bagian awal untuk inisialisasi aplikasi
    init {
        viewModelScope.launch { initializeApp() }
    }

    // Fungsi untuk inisialisasi aplikasi
    private suspend fun initializeApp() {
        coroutineScope {
            // Menunda durasi splash screen
            launch { delay(MINIMAL_SPLASH_SCREEN_DURATION) }
            // Sinkronisasi data dari cloud ke lokal
            launch { cloudToLocalSync() }
            // Menentukan tujuan awal aplikasi
            launch { setupStartDestination() }
            _isAppReady.value = true // Tandai bahwa aplikasi siap digunakan
        }
    }

    // Dipanggil saat aktivitas dijeda
    fun onActivityPause() {
        viewModelScope.launch { localToCloudSync() } // Sinkronisasi data dari lokal ke cloud
    }

    // Sinkronisasi data dari lokal ke cloud
    private suspend fun localToCloudSync() {
        mainRepository.syncToCloud().collect {
            Log.d(TAG, "localToCloudSync: ${it.javaClass.simpleName}")
            if (it is com.wahyusembiring.data.Result.Error) {
                Log.e(TAG, "localToCloudSync: ${it.throwable.message}", it.throwable)
            }
        }
    }

    // Sinkronisasi data dari cloud ke lokal
    private suspend fun cloudToLocalSync() {
        mainRepository.syncToLocal().collect {
            Log.d(TAG, "cloudToLocalSync: ${it.javaClass.simpleName}")
            if (it is com.wahyusembiring.data.Result.Error) {
                Log.e(TAG, "cloudToLocalSync: ${it.throwable.message}", it.throwable)
            }
        }
    }

    // Menentukan tujuan awal aplikasi berdasarkan status onboarding dan login
    private suspend fun setupStartDestination(): Unit = coroutineScope {
        val isOnBoardingCompleted = async {
            dataStoreRepository.readOnBoardingState().first() // Apakah onboarding selesai
        }.await()
        val isUserLoggedIn = async {
            authRepository.currentUser.first() != null // Apakah pengguna sudah login
        }.await()
        val startDestination = when {
            isOnBoardingCompleted && isUserLoggedIn -> Screen.Overview // Halaman utama
            isOnBoardingCompleted -> Screen.Login // Halaman login
            else -> Screen.OnBoarding // Halaman onboarding
        }
        _startDestination.value = startDestination // Set tujuan awal
    }
}
