package com.wahyusembiring.lecturer.screen.addlecture

import com.wahyusembiring.ui.util.UIText

class ValidationException(
    val displayMessage: UIText, // Menyimpan pesan kesalahan yang akan ditampilkan kepada pengguna
) : Exception() // Menurunkan dari kelas Exception untuk menandai ini sebagai pengecualian