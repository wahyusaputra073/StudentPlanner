package com.wahyusembiring.thesisplanner.screen.thesisselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.imageLoader
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.thesisplanner.R
import com.wahyusembiring.thesisplanner.component.Section
import com.wahyusembiring.thesisplanner.component.ThesisList
import com.wahyusembiring.ui.component.topappbar.TopAppBar
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun ThesisSelectionScreen(
    viewModel: ThesisSelectionScreenViewModel, // Mengambil ViewModel untuk mengelola status UI
    drawerState: DrawerState, // Mengontrol status drawer (menu samping)
    navController: NavHostController // Menavigasi antar layar
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle() // Mengambil state dari ViewModel
    val coroutineScope = rememberCoroutineScope() // Menyimpan coroutine scope untuk meluncurkan operasi asynchronous

    ThesisSelectionScreen( // Menampilkan UI untuk memilih tesis
        state = state, // Menyediakan state UI dari ViewModel
        onUIEvent = viewModel::onUIEvent, // Menangani event UI
        onNavigateToThesisPlanner = { // Menavigasi ke layar perencanaan tesis dengan ID tesis
            navController.navigate(Screen.ThesisPlanner(it))
        },
        onHamburgerMenuClick = { // Membuka menu drawer
            coroutineScope.launch { drawerState.open() }
        }
    )
}

@Composable
private fun ThesisSelectionScreen(
    state: ThesisSelectionScreenUIState, // State untuk menampilkan daftar tesis
    onUIEvent: (ThesisSelectionScreenUIEvent) -> Unit, // Fungsi untuk menangani UI events
    onNavigateToThesisPlanner: (thesisId: Int) -> Unit, // Fungsi untuk menavigasi ke ThesisPlanner
    onHamburgerMenuClick: () -> Unit, // Fungsi untuk membuka menu drawer
) {
    val context = LocalContext.current // Mengambil context aplikasi

    Scaffold( // Membuat struktur halaman dengan top bar dan floating action button
        topBar = { // Menampilkan bar atas dengan menu
            TopAppBar(
                title = stringResource(R.string.thesis_planner), // Menampilkan judul
                onMenuClick = onHamburgerMenuClick // Fungsi untuk membuka menu drawer
            )
        },
        floatingActionButton = { // Floating action button untuk membuat tesis baru
            FloatingActionButton(
                onClick = {
                    onUIEvent( // Menangani klik tombol untuk membuat tesis baru
                        ThesisSelectionScreenUIEvent.OnCreateNewThesisClick(
                            onNavigateToThesisPlanner = onNavigateToThesisPlanner
                        )
                    )
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), // Menampilkan ikon tambah
                    contentDescription = stringResource(R.string.create_new_thesis) // Deskripsi konten
                )
            }
        }
    ) { scaffoldPadding -> // Padding dari scaffold untuk konten utama
        if (state.listOfThesis.isNotEmpty()) { // Jika ada daftar tesis
            Column(
                modifier = Modifier.padding(scaffoldPadding) // Menambahkan padding pada kolom
            ) {
                Section( // Menampilkan bagian daftar tesis
                    title = stringResource(R.string.thesis_list) // Menampilkan judul bagian
                ) {
                    ThesisList( // Menampilkan daftar tesis
                        listOfThesis = state.listOfThesis, // Daftar tesis
                        onThesisClick = { // Menangani klik pada tesis untuk membuka Planner
                            onNavigateToThesisPlanner(it.thesis.id)
                        },
                        onDeleteThesis = { // Menangani klik untuk menghapus tesis
                            onUIEvent(ThesisSelectionScreenUIEvent.OnDeleteThesisClick(it))
                        }
                    )
                }
            }
        } else { // Jika tidak ada tesis
            Box( // Menampilkan pesan jika tidak ada tesis
                modifier = Modifier
                    .padding(scaffoldPadding) // Padding untuk box
                    .fillMaxSize(), // Mengisi seluruh ukuran
                contentAlignment = Alignment.Center // Menyusun konten di tengah
            ) {
                Column( // Kolom untuk menampilkan gambar dan teks
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally, // Penyusunan horizontal di tengah
                    verticalArrangement = Arrangement.Center // Penyusunan vertikal di tengah
                ) {
                    AsyncImage( // Menampilkan gambar saat tidak ada data
                        modifier = Modifier
                            .width(100.dp), // Ukuran gambar
                        model = R.drawable.no_data_picture, // Gambar yang digunakan
                        contentDescription = stringResource(R.string.you_dont_have_any_thesis), // Deskripsi konten
                        imageLoader = context.imageLoader // Memuat gambar menggunakan image loader
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Jarak antar elemen
                    Text( // Teks untuk memberi tahu pengguna bahwa tidak ada tesis
                        text = stringResource(R.string.you_dont_have_any_thesis),
                        style = MaterialTheme.typography.bodyMedium // Gaya teks
                    )
                }
            }
        }
    }
}