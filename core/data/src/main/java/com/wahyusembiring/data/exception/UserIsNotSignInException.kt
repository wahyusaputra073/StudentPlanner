package com.wahyusembiring.data.exception

class UserIsNotSignInException : Exception() {  // Kelas exception khusus untuk kasus pengguna yang tidak terautentikasi

    override val message: String  // Pesan yang akan ditampilkan saat exception dilempar
        get() = "Expecting user to be sign in, but null is returned by AuthRepository.currentUser"  // Pesan error jika pengguna tidak terautentikasi
}