package com.wahyusembiring.onboarding

import com.wahyusembiring.onboarding.model.OnBoardingModel

data class OnBoardingScreenUIState(
    val email: String = "", // Menyimpan email yang diinput oleh pengguna
    val password: String = "", // Menyimpan password yang diinput oleh pengguna
    val popUpStack: List<OnBoardingScreenPopUp> = emptyList(), // Menyimpan daftar popup yang tampil dalam stack

    val models: List<OnBoardingModel> = listOf( // Menyimpan daftar model untuk layar onboarding
        OnBoardingModel.First, // Model pertama untuk onboarding
        OnBoardingModel.Second, // Model kedua untuk onboarding
        OnBoardingModel.Third // Model ketiga untuk onboarding
    )
)
