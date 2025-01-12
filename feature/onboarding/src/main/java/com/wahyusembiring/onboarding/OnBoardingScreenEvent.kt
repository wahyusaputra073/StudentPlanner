package com.wahyusembiring.onboarding

sealed class OnBoardingScreenUIEvent { // Event yang dikirim dari UI ke ViewModel
    data object OnCompleted : OnBoardingScreenUIEvent() // Event ketika onboarding selesai
    data object OnLoginSkipButtonClick : OnBoardingScreenUIEvent() // Event ketika tombol skip login ditekan
}

sealed class OnBoardingScreenNavigationEvent { // Event untuk navigasi antar screen
    data object NavigateToHomeScreen : OnBoardingScreenNavigationEvent() // Event untuk navigasi ke HomeScreen
}

sealed class OnBoardingScreenPopUp { // Pop-up yang ditampilkan di UI
    data object SignInLoading : OnBoardingScreenPopUp() // Pop-up loading saat login anonim
    data object SignInFailed : OnBoardingScreenPopUp() // Pop-up gagal login
    data object CommonLoading : OnBoardingScreenPopUp() // Pop-up loading umum
}
