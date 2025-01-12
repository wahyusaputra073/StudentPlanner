package com.wahyusembiring.onboarding.model

import androidx.annotation.DrawableRes
import com.wahyusembiring.onboarding.R

sealed class OnBoardingModel(
    @DrawableRes val image: Int, // Gambar yang ditampilkan pada onboarding
    val title: String, // Judul untuk setiap layar onboarding
    val description: String // Deskripsi yang memberikan penjelasan lebih lanjut
) {

    // Objek data pertama, memberikan gambaran tentang mengelola jadwal
    data object First : OnBoardingModel(
        image = R.drawable.onboarding_schedule, // Gambar untuk layar pertama
        title = "Stay on top of your schedule", // Judul layar pertama
        description = "Always know your deadlines, plan your days with an integrated calendar" // Deskripsi layar pertama
    )

    // Objek data kedua, memberikan gambaran tentang meningkatkan produktivitas
    data object Second : OnBoardingModel(
        image = R.drawable.onboarding_productivity, // Gambar untuk layar kedua
        title = "Boost your productivity", // Judul layar kedua
        description = "Get task reminders, create important note, and track your study progress" // Deskripsi layar kedua
    )

    // Objek data ketiga, memberikan gambaran tentang manajemen waktu untuk ujian
    data object Third : OnBoardingModel(
        image = R.drawable.onboarding_time_management, // Gambar untuk layar ketiga
        title = "Ace your exams", // Judul layar ketiga
        description = "Create effective study schedule, track your learning progress, and plan your exams" // Deskripsi layar ketiga
    )
}