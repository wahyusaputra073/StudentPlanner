package com.wahyusembiring.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.data.Result
import com.wahyusembiring.data.repository.AuthRepository
import com.wahyusembiring.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Menandakan bahwa kelas ini menggunakan Hilt untuk dependency injection
class OnBoardingScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository, // Dependency untuk membaca dan menyimpan state onboarding
    private val authRepository: AuthRepository, // Dependency untuk autentikasi pengguna
) : ViewModel() {

    companion object {
        private const val TAG = "OnBoardingScreenViewModel" // TAG untuk log
    }

    private val _uiState = MutableStateFlow(OnBoardingScreenUIState()) // Menyimpan state UI onboarding
    val uiState = _uiState.asStateFlow() // Mengexpose state UI sebagai StateFlow

    private val _navigationEvent = Channel<OnBoardingScreenNavigationEvent>() // Channel untuk event navigasi
    val navigationEvent = _navigationEvent.receiveAsFlow() // Mengexpose navigation event sebagai Flow

    init { // Inisialisasi yang dijalankan ketika ViewModel pertama kali dibuat
        viewModelScope.launch { // Melakukan operasi secara asynchronous di dalam scope ViewModel
            dataStoreRepository.readOnBoardingState().collect { completed -> // Mengambil status onboarding
                if (completed) {
                    _navigationEvent.send(OnBoardingScreenNavigationEvent.NavigateToHomeScreen) // Navigasi ke HomeScreen jika onboarding selesai
                }
            }
        }
    }

    fun onUIEvent(event: OnBoardingScreenUIEvent) { // Menangani event dari UI
        when (event) {
            is OnBoardingScreenUIEvent.OnCompleted -> { // Ketika onboarding selesai
                saveOnBoardingState(completed = true) // Simpan status selesai onboarding
            }
            is OnBoardingScreenUIEvent.OnLoginSkipButtonClick -> onLoginSkipButtonClick() // Ketika tombol skip login ditekan
        }
    }

    private fun onLoginSkipButtonClick() { // Fungsi untuk menangani klik tombol skip login
        viewModelScope.launch { // Melakukan operasi secara asynchronous
            val request = authRepository.signInAnonymously() // Melakukan sign-in anonim
            request.collect { result -> // Mengambil hasil dari request sign-in
                when (result) {
                    is Result.Loading -> { // Ketika masih dalam proses
                        _uiState.update {
                            it.copy(popUpStack = it.popUpStack + OnBoardingScreenPopUp.SignInLoading) // Menampilkan popup loading
                        }
                    }

                    is Result.Error -> { // Ketika terjadi error
                        val throwable = result.throwable
                        Log.e(TAG, "Login failed: ${throwable.message}", throwable) // Mencatat error pada log
                        _uiState.update {
                            it.copy(
                                popUpStack = it.popUpStack
                                    .minus(OnBoardingScreenPopUp.SignInLoading) // Menghapus popup loading
                                    .plus(OnBoardingScreenPopUp.SignInFailed) // Menambahkan popup gagal login
                            )
                        }
                    }

                    is Result.Success -> { // Ketika berhasil
                        _uiState.update {
                            it.copy(popUpStack = it.popUpStack - OnBoardingScreenPopUp.SignInLoading) // Menghapus popup loading
                        }
                    }
                }
            }
        }
    }

    private fun saveOnBoardingState(completed: Boolean) { // Fungsi untuk menyimpan status onboarding
        viewModelScope.launch(Dispatchers.IO) { // Melakukan operasi di thread IO
            dataStoreRepository.saveOnBoardingState(completed = completed) // Menyimpan status ke DataStore
        }
    }
}
