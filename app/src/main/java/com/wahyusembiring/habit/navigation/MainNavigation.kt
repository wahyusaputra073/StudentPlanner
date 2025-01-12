package com.wahyusembiring.habit.navigation

import android.view.animation.PathInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.wahyusembiring.habit.MainViewModel

// Fungsi MainNavigation digunakan untuk menampilkan dan mengatur navigasi antar layar di aplikasi
@Composable
fun MainNavigation(
    mainViewModel: MainViewModel, // ViewModel untuk mengelola data aplikasi
    navController: NavHostController, // Controller untuk navigasi antar layar
    scaffoldPadding: PaddingValues, // Padding untuk konten agar tidak tertutup oleh drawer atau elemen lain
    builder: NavGraphBuilder.() -> Unit // Fungsi untuk membangun graf navigasi yang berisi layar-layar aplikasi
) {
    // Easing untuk animasi transisi antara layar, memberi kesan halus pada pergerakan
    val emphasizedDeceleratedEasing = remember {
        Easing { PathInterpolator(0.05f, 0.7f, 0.1f, 1f).getInterpolation(it) }
    }

    // Mendapatkan startDestination (layar awal) dari ViewModel
    val startDestination by mainViewModel.startDestination.collectAsStateWithLifecycle()

    // Membuat NavHost yang mengatur navigasi antar layar
    NavHost(
        navController = navController, // Controller untuk mengelola navigasi
        startDestination = startDestination, // Layar yang akan ditampilkan pertama kali
        enterTransition = { // Animasi saat layar masuk
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 400, // Durasi animasi
                    easing = emphasizedDeceleratedEasing // Jenis easing untuk animasi
                ),
                initialOffsetX = { it } // Offset awal layar saat masuk
            ) + fadeIn() // Efek fade-in
        },
        exitTransition = { // Animasi saat layar keluar
            slideOutHorizontally(
                tween(
                    durationMillis = 400, // Durasi animasi
                    easing = emphasizedDeceleratedEasing // Jenis easing untuk animasi
                ),
                targetOffsetX = { it } // Offset akhir layar saat keluar
            ) + fadeOut() // Efek fade-out
        },
        modifier = Modifier.padding(scaffoldPadding), // Menambahkan padding pada konten
        builder = builder // Membangun graf navigasi berdasarkan parameter builder
    )
}
