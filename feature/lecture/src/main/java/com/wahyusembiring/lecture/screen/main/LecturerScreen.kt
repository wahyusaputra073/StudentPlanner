package com.wahyusembiring.lecture.screen.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.CollectAsOneTimeEvent
import com.wahyusembiring.lecture.R
import com.wahyusembiring.lecture.component.LecturerCard
import com.wahyusembiring.ui.component.topappbar.TopAppBar
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun LecturerScreen(
    viewModel: LecturerScreenViewModel, // ViewModel untuk mengelola data dan logika layar ini
    navController: NavController, // Navigasi untuk berpindah antar layar
    drawerState: DrawerState, // Status dari drawer menu
) {
    val state by viewModel.state.collectAsStateWithLifecycle() // Mengambil state dari ViewModel dan mengamati perubahan
    val coroutineScope = rememberCoroutineScope() // Membuat scope coroutine untuk menjalankan tugas asinkron

    // Mengumpulkan event navigasi satu kali dari ViewModel
    CollectAsOneTimeEvent(viewModel.navigationEvent) {
        when (it) {
            is LecturerScreenNavigationEvent.NavigateToLecturerDetail -> {
                navController.navigate(Screen.AddLecturer(it.lecturerId)) // Navigasi ke detail dosen
            }

            is LecturerScreenNavigationEvent.NavigateToAddLecturer -> {
                navController.navigate(Screen.AddLecturer()) // Navigasi ke layar tambah dosen
            }
        }
    }

    // Memanggil Composable LecturerScreen dengan parameter yang dibutuhkan
    LecturerScreen(
        navController = navController, // Mengirimkan controller untuk navigasi
        state = state, // Mengirimkan state dosen saat ini
        onUIEvent = viewModel::onUIEvent, // Menangani event UI
        onHamburgerMenuClick = {
            coroutineScope.launch { drawerState.open() } // Membuka menu drawer saat menu hamburger diklik
        }
    )
}


@Composable
private fun LecturerScreen(
    navController: NavController, // NavController untuk navigasi antar layar
    state: LecturerScreenUIState, // State UI yang berisi data dosen
    onUIEvent: (LecturerScreenUIEvent) -> Unit, // Fungsi untuk menangani event UI
    onHamburgerMenuClick: () -> Unit, // Fungsi yang dipanggil ketika menu hamburger diklik
) {
    Scaffold(
        topBar = {
            // Menampilkan TopAppBar dengan judul "Lectures" dan tombol menu hamburger
            TopAppBar(
                title = stringResource(R.string.lectures),
                onMenuClick = onHamburgerMenuClick
            )
        },
        floatingActionButton = {
            // Menampilkan FloatingActionButton yang membuka layar tambah dosen
            FloatingActionButton(
                onClick = {
                    onUIEvent(LecturerScreenUIEvent.OnAddLecturerClick(navController)) // Navigasi untuk menambah dosen
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add, // Ikon tambah
                    contentDescription = null
                )
            }
        }
    ) { scaffoldPadding ->
        // Menampilkan LazyColumn yang berisi daftar dosen dengan subjek
        LazyColumn(
            modifier = Modifier.padding(scaffoldPadding) // Mengatur padding untuk LazyColumn
        ) {
            items(
                items = state.listOfLecturerWithSubjects, // Daftar dosen beserta subjek
                key = { it.lecturer.id } // Key untuk item berdasarkan ID dosen
            ) { lectureWithSubjects ->
                // Menampilkan setiap LecturerCard untuk setiap dosen
                LecturerCard(
                    lecturerWithSubjects = lectureWithSubjects,
                    onClick = {
                        onUIEvent(LecturerScreenUIEvent.OnLecturerClick(lectureWithSubjects)) // Klik pada dosen untuk detail
                    },
                    onDeleteClick = {
                        onUIEvent(LecturerScreenUIEvent.OnDeleteLecturerClick(lectureWithSubjects)) // Klik hapus dosen
                    }
                )
                // Menampilkan garis horizontal pemisah, kecuali pada item terakhir
                if (lectureWithSubjects != state.listOfLecturerWithSubjects.last()) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.Large))
                }
            }
        }
    }
}