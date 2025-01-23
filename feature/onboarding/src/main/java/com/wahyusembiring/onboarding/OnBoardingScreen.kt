package com.wahyusembiring.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.CollectAsOneTimeEvent
import com.wahyusembiring.onboarding.component.PageIndicator
import com.wahyusembiring.onboarding.model.OnBoardingModel
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun OnBoardingScreen(
    viewModel: OnBoardingScreenViewModel, // ViewModel untuk mengelola state dan event pada layar onboarding
    navController: NavHostController, // Controller untuk navigasi antar layar
) {

    // Mengambil state UI dari ViewModel menggunakan collectAsStateWithLifecycle
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Mengambil event navigasi dari ViewModel
    val navigationEvent = viewModel.navigationEvent

    // Mengumpulkan event navigasi yang hanya dipicu sekali
    CollectAsOneTimeEvent(navigationEvent) {
        when (it) {
            // Ketika event navigasi ke layar Home diterima
            is OnBoardingScreenNavigationEvent.NavigateToHomeScreen -> {
                navController.navigate(Screen.Overview) { // Navigasi ke layar Overview
                    popUpTo(navController.graph.id) // Menghapus seluruh layar sebelumnya dari stack navigasi
                }
            }
        }
    }

    // Menampilkan UI onboarding dengan state dan event handler dari ViewModel
    OnBoardingScreen(
        state = state, // State UI yang diteruskan ke Composable
        onUIEvent = viewModel::onUIEvent // Event handler untuk UI event yang diteruskan ke ViewModel
    )
}


@Suppress("t") // Menonaktifkan peringatan tentang penggunaan tipe yang tidak digunakan
@Composable
private fun OnBoardingScreen( // Fungsi utama untuk layar onboarding
    state: OnBoardingScreenUIState, // State yang berisi data untuk onboarding
    onUIEvent: (OnBoardingScreenUIEvent) -> Unit // Fungsi untuk mengirim event UI
) {
    val pagerState = rememberPagerState(initialPage = 0) { // Mengatur state untuk pager dengan halaman pertama diindeks 0
        state.models.size // Jumlah halaman onboarding berdasarkan ukuran models
    }
    val coroutineScope = rememberCoroutineScope() // Scope coroutine untuk meluncurkan tugas asinkron

    Scaffold { paddingValues -> // Scaffold untuk layout dasar yang mengatur padding
        Column( // Menyusun elemen secara vertikal
            modifier = Modifier
                .padding(paddingValues) // Menambahkan padding dari Scaffold
                .fillMaxSize() // Mengisi seluruh ukuran layar
        ) {
            HorizontalPager( // Pager untuk navigasi antar halaman onboarding
                modifier = Modifier.weight(1f), // Membuat pager mengambil ruang sisa yang tersedia
                state = pagerState // Menghubungkan pager dengan state
            ) {
                OnBoardingScreen(model = state.models[it]) // Menampilkan halaman onboarding sesuai dengan model data
            }
            Box( // Box untuk meletakkan tombol dan indikator halaman
                modifier = Modifier
                    .fillMaxWidth() // Membuat Box memenuhi lebar layar
                    .padding(
                        horizontal = MaterialTheme.spacing.Medium, // Padding horizontal
                        vertical = MaterialTheme.spacing.Small // Padding vertikal
                    ),
            ) {
                if (pagerState.currentPage > 0) { // Menampilkan tombol "Back" hanya jika bukan halaman pertama
                    TextButton( // Tombol untuk kembali ke halaman sebelumnya
                        modifier = Modifier.align(Alignment.CenterStart), // Menempatkan tombol di kiri
                        onClick = {
                            coroutineScope.launch { // Menjalankan animasi perpindahan halaman
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    ) {
                        Text(text = "Back") // Teks pada tombol "Back"
                    }
                }
                PageIndicator( // Menampilkan indikator halaman
                    modifier = Modifier.align(Alignment.Center), // Menempatkan indikator di tengah
                    pageSize = state.models.size, // Jumlah halaman onboarding
                    currentPage = pagerState.currentPage // Halaman yang sedang aktif
                )
                Button( // Tombol untuk navigasi ke halaman berikutnya atau menyelesaikan onboarding
                    modifier = Modifier.align(Alignment.CenterEnd), // Menempatkan tombol di kanan
                    onClick = {
                        coroutineScope.launch { // Menjalankan animasi perpindahan halaman
                            if (pagerState.currentPage == state.models.size - 1) { // Jika halaman terakhir
                                onUIEvent(OnBoardingScreenUIEvent.OnLoginSkipButtonClick) // Kirim event untuk login/skip
                                onUIEvent(OnBoardingScreenUIEvent.OnCompleted) // Event untuk menyelesaikan onboarding
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1) // Pindah ke halaman berikutnya
                            }
                        }
                    }
                ) {
                    Text(text = if (pagerState.currentPage == state.models.size - 1) "Get Started!" else "Next") // Teks tombol berubah di halaman terakhir
                }
            }
        }
    }
}


@Composable
private fun OnBoardingScreen(
    model: OnBoardingModel, // Menerima model untuk data onboarding
) {
    Column(
        modifier = Modifier.fillMaxSize(), // Menyusun elemen secara vertikal untuk mengisi seluruh layar
        horizontalAlignment = Alignment.CenterHorizontally, // Menyusun elemen secara horisontal di tengah
        verticalArrangement = Arrangement.Center // Menyusun elemen secara vertikal di tengah
    ) {
        AsyncImage(
            model = model.image, // Menampilkan gambar dari model
            contentDescription = model.title, // Deskripsi gambar (untuk aksesibilitas)
            modifier = Modifier
                .fillMaxWidth() // Gambar mengisi lebar layar
                .padding(horizontal = 64.dp), // Memberikan padding horizontal
            alignment = Alignment.Center // Menyusun gambar di tengah
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Large)) // Memberikan jarak besar di bawah gambar

        Text(
            text = model.title, // Menampilkan judul onboarding
            modifier = Modifier.fillMaxWidth(), // Teks mengisi lebar layar
            textAlign = TextAlign.Center, // Menyusun teks di tengah
            style = MaterialTheme.typography.titleMedium // Menggunakan style judul
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan jarak medium antara judul dan deskripsi

        Text(
            text = model.description, // Menampilkan deskripsi onboarding
            modifier = Modifier.fillMaxWidth(), // Teks mengisi lebar layar
            textAlign = TextAlign.Center, // Menyusun teks di tengah
            style = MaterialTheme.typography.bodyMedium // Menggunakan style teks biasa
        )
    }
}