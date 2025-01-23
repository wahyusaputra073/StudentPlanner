package com.wahyusembiring.subject.screen.main

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.CollectAsOneTimeEvent
import com.wahyusembiring.subject.R
import com.wahyusembiring.ui.component.modalbottomsheet.component.AddNewSubject
import com.wahyusembiring.ui.component.modalbottomsheet.component.SubjectListItem
import com.wahyusembiring.ui.component.modalbottomsheet.component.SubjectListItemMenu
import com.wahyusembiring.ui.component.topappbar.TopAppBar
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun SubjectScreen(
    viewModel: SubjectScreenViewModel,  // ViewModel untuk mengelola state dan event
    navController: NavController,  // Kontrol navigasi antar layar
    drawerState: DrawerState  // Status drawer untuk membuka/tutup menu samping
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()  // Mengambil state terkini dari ViewModel
    val coroutineScope = rememberCoroutineScope()  // Membuat scope untuk coroutine

    CollectAsOneTimeEvent(viewModel.navigationEvent) {  // Menangani event navigasi sekali
        when (it) {
            is SubjectScreenNavigationEvent.NavigateToSubjectDetail -> {  // Jika event untuk navigasi detail subject
                navController.navigate(Screen.CreateSubject(it.subject.id))  // Menavigasi ke layar detail subject
            }
        }
    }

    // Tampilan UI untuk layar Subject
    SubjectScreen(
        state = state,  // State yang digunakan untuk meng-update UI
        onUIEvent = { event ->  // Event handler untuk interaksi UI
            when (event) {
                is SubjectScreenUIEvent.OnHamburgerMenuClick -> {  // Jika menu hamburger diklik
                    coroutineScope.launch { drawerState.open() }  // Membuka drawer menu
                }
                is SubjectScreenUIEvent.OnExamClick -> {  // Jika exam diklik
                    navController.navigate(Screen.CreateExam(event.exam.id))  // Menavigasi ke layar exam
                }
                is SubjectScreenUIEvent.OnFloatingActionButtonClick -> {  // Jika tombol FAB diklik
                    navController.navigate(Screen.CreateSubject())  // Menavigasi ke layar pembuatan subject
                }
                else -> viewModel.onUIEvent(event)  // Menangani event lainnya menggunakan ViewModel
            }
        }
    )
}


@Composable
@Suppress("t")
private fun SubjectScreen(
    state: SubjectScreenUIState,  // State untuk mengontrol UI terkait subject
    onUIEvent: (event: SubjectScreenUIEvent) -> Unit  // Event handler untuk setiap aksi UI
) {
    val scrollState = rememberScrollState()  // State untuk mengatur scroll di layar
    val context = LocalContext.current  // Mendapatkan context aplikasi saat ini

    // Scaffold untuk layout utama, berisi top bar dan floating action button
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.subject),  // Judul TopAppBar
                onMenuClick = { onUIEvent(SubjectScreenUIEvent.OnHamburgerMenuClick) }  // Menu klik
            )
        },
        floatingActionButton = {  // Tombol aksi mengapung
            FloatingActionButton(onClick = { onUIEvent(SubjectScreenUIEvent.OnFloatingActionButtonClick) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),  // Ikon untuk tombol FAB
                    contentDescription = null
                )
            }
        }
    ) { scaffoldPadding ->  // Padding dari Scaffold diteruskan ke konten
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)  // Padding untuk konten
                .scrollable(scrollState, Orientation.Vertical)  // Scroll vertikal untuk konten
        ) {
            state.subjects.forEach { subject ->  // Menampilkan list subject
                SubjectListItemMenu(
                    subject = subject.subject,  // Subjek yang ditampilkan
                    onClicked = { selectedSubject ->  // Klik pada subject
                        onUIEvent(SubjectScreenUIEvent.OnSubjectClick(selectedSubject))
                    },
                    onDeleteSubClick = { onUIEvent(SubjectScreenUIEvent.OnDeleteSubjectClick(subject.subject)) }  // Klik hapus
                )
            }

            // Menampilkan pesan jika tidak ada subject
            if (state.subjects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),  // Membuat Box penuh
                    contentAlignment = Alignment.Center  // Menempatkan konten di tengah
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,  // Rata tengah horizontal
                        verticalArrangement = Arrangement.Center  // Rata tengah vertikal
                    ) {
                        AsyncImage(
                            modifier = Modifier.width(64.dp),  // Ukuran gambar
                            model = R.drawable.no_data_picture,  // Gambar jika tidak ada data
                            contentDescription = null,
                            imageLoader = context.imageLoader  // Loader gambar
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))  // Spacer antar elemen
                        Text(
                            text = stringResource(id = R.string.you_don_t_have_any_subject),  // Teks jika tidak ada subject
                            style = MaterialTheme.typography.bodyMedium  // Gaya teks
                        )
                    }
                }
            }

            // AddNewSubject bisa ditambahkan di sini jika diperlukan
//            AddNewSubject(
//                onClicked = {
//                    onUIEvent(SubjectScreenUIEvent.OnFloatingActionButtonClick)
//                }
//            )
        }
    }
}



