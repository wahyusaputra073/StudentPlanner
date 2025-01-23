package com.wahyusembiring.data

// Mendeklarasikan sealed class Result yang digunakan untuk membungkus hasil operasi (berisi status loading, sukses, atau error)
sealed class Result<T> {

    // Representasi status loading, digunakan saat operasi sedang berlangsung
    class Loading<T> : Result<T>()

    // Representasi hasil sukses, membawa data bertipe T
    data class Success<T>(val data: T) : Result<T>()

    // Representasi hasil error, membawa throwable untuk error yang terjadi
    class Error<T>(val throwable: Throwable) : Result<T>()
}

