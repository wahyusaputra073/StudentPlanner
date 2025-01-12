package com.wahyusembiring.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Reminder
import com.wahyusembiring.ui.component.eventcard.EventCard
import com.wahyusembiring.ui.component.scoredialog.ScoreDialog
import com.wahyusembiring.ui.component.floatingactionbutton.HomeworkExamAndReminderFAB
import com.wahyusembiring.ui.component.topappbar.TopAppBar
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch


@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel, // Menerima viewModel untuk mengelola state dan event
    navController: NavHostController, // Menerima controller navigasi untuk berpindah antar layar
    drawerState: DrawerState // Menerima state drawer untuk mengontrol tampilan menu samping
) {
    val state by viewModel.state.collectAsStateWithLifecycle() // Mengambil state terkini dari viewModel dengan lifecycle-aware
    val coroutineScope = rememberCoroutineScope() // Mengambil coroutine scope untuk menjalankan operasi asinkron

    OverviewScreen(
        state = state, // Menyampaikan state ke komponen OverviewScreen
        onUIEvent = viewModel::onUIEvent, // Menyampaikan fungsi untuk menangani event UI
        onHamburgerMenuClick = { // Menangani klik pada tombol hamburger menu untuk membuka drawer
            coroutineScope.launch { drawerState.open() }
        },
        onNavigateTo = { // Menangani navigasi ke layar lain
            navController.navigate(it)
        }
    )
}



@Composable
private fun OverviewScreen(
    modifier: Modifier = Modifier, // Modifier untuk penyesuaian tampilan
    state: OverviewScreenUIState, // State untuk menyimpan data yang akan ditampilkan di layar
    onUIEvent: (OverviewScreenUIEvent) -> Unit, // Fungsi untuk menangani event UI
    onHamburgerMenuClick: () -> Unit, // Fungsi untuk menangani klik pada tombol hamburger menu
    onNavigateTo: (Screen) -> Unit, // Fungsi untuk menangani navigasi ke layar lain
) {
    var fabExpanded by remember { mutableStateOf(false) } // Menyimpan status apakah FAB terbuka atau tertutup

    Scaffold(
        topBar = { // Menampilkan TopAppBar dengan judul dan menu
            TopAppBar(
                title = stringResource(R.string.overview), // Judul layar dari string resource
                onMenuClick = onHamburgerMenuClick // Menangani klik pada tombol menu
            )
        },
        floatingActionButton = { // Menampilkan tombol FAB dengan fungsionalitas pengaturan status expand
            HomeworkExamAndReminderFAB(
                isExpanded = fabExpanded, // Status apakah FAB diperluas
                onClick = { fabExpanded = !fabExpanded }, // Toggle status FAB ketika diklik
                onDismiss = { fabExpanded = false }, // Menutup FAB jika di-dismis
                onReminderFabClick = { onNavigateTo(Screen.CreateReminder()) }, // Menavigasi ke layar pembuatan reminder
                onExamFabClick = { onNavigateTo(Screen.CreateExam()) }, // Menavigasi ke layar pembuatan ujian
                onHomeworkFabClick = { onNavigateTo(Screen.CreateHomework()) }, // Menavigasi ke layar pembuatan tugas
            )
        }
    ) {
        OverviewScreenMainContent( // Menampilkan konten utama layar Overview
            modifier = modifier.padding(it), // Menambahkan padding untuk mengakomodasi ukuran layar
            state = state, // Menyampaikan state untuk data yang ditampilkan
            onUIEvent = onUIEvent, // Menangani event UI dari konten utama
            onNavigateTo = onNavigateTo // Menangani navigasi dari konten utama
        )
    }
}



@Suppress("t") // Menonaktifkan peringatan untuk penggunaan tipe yang tidak digunakan
@Composable
private fun OverviewScreenMainContent(
    modifier: Modifier = Modifier, // Modifier untuk penyesuaian tampilan
    state: OverviewScreenUIState, // State untuk data yang akan ditampilkan di layar
    onUIEvent: (OverviewScreenUIEvent) -> Unit, // Fungsi untuk menangani event UI
    onNavigateTo: (Screen) -> Unit, // Fungsi untuk menangani navigasi ke layar lain
) {
    val context = LocalContext.current // Mendapatkan context aplikasi saat ini

    LazyColumn( // Menampilkan daftar item dalam bentuk kolom yang dapat digulir
        modifier = modifier.fillMaxSize() // Mengisi seluruh ukuran layar
    ) {
        items(
            items = state.eventCards, // Mengambil daftar eventCards dari state
            key = { it.title.asString(context) } // Menentukan key unik berdasarkan judul event
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan jarak antara item
            DaySectionHeader(
                title = it.title.asString(), // Menampilkan judul dari event
                date = it.date.asString() // Menampilkan tanggal event
            )
            EventCard( // Menampilkan kartu event
                modifier = Modifier.padding( // Menambahkan padding pada kartu event
                    horizontal = MaterialTheme.spacing.Large,
                    vertical = MaterialTheme.spacing.Small
                ),
                onEventClick = { event -> // Menangani klik pada event untuk navigasi ke detail
                    when (event) {
                        is HomeworkWithSubject -> {
                            onNavigateTo(Screen.CreateHomework(event.homework.id)) // Navigasi ke layar pembuatan tugas
                        }

                        is ExamWithSubject -> {
                            onNavigateTo(Screen.CreateExam(event.exam.id)) // Navigasi ke layar pembuatan ujian
                        }

                        is Reminder -> {
                            onNavigateTo(Screen.CreateReminder(event.id)) // Navigasi ke layar pembuatan reminder
                        }

                        else -> Unit // Tidak melakukan apapun jika tipe event tidak dikenali
                    }
                },
                onDeletedEventClick = { event -> // Menangani klik pada event untuk menghapusnya
                    onUIEvent(OverviewScreenUIEvent.OnDeleteEvent(event))
                },
                events = it.events, // Menampilkan daftar event dalam DaySection
                onEventCheckedChange = { event, isChecked -> // Menangani perubahan status centang event
                    onUIEvent(OverviewScreenUIEvent.OnEventCompletedStateChange(event, isChecked))
                },
            )
        }
    }

    if (state.scoreDialog != null) { // Menampilkan dialog skor jika ada
        ScoreDialog(
            initialScore = state.scoreDialog.initialScore, // Menampilkan skor awal
            onMarkNotDoneYet = { // Menandai ujian sebagai belum selesai
                onUIEvent(
                    OverviewScreenUIEvent.OnMarkExamAsUndone(state.scoreDialog.exam)
                )
            },
            onDismissRequest = { // Menutup dialog
                onUIEvent(
                    OverviewScreenUIEvent
                        .OnExamScoreDialogStateChange(null)
                )
            },
            onScoreConfirmed = { score -> // Menyimpan skor yang dikonfirmasi
                onUIEvent(
                    OverviewScreenUIEvent.OnExamScorePicked(state.scoreDialog.exam, score)
                )
            }
        )
    }
}


@Composable
private fun DaySectionHeader(
    title: String, // Judul untuk bagian hari
    date: String // Tanggal untuk bagian hari
) {
    Row( // Menyusun elemen secara horisontal dalam baris
        modifier = Modifier
            .fillMaxWidth() // Mengisi seluruh lebar layar
            .padding(horizontal = MaterialTheme.spacing.Medium), // Menambahkan padding horizontal
        horizontalArrangement = Arrangement.SpaceBetween, // Menyusun elemen dengan ruang di antara
        verticalAlignment = Alignment.Bottom // Menyusun elemen secara vertikal di bagian bawah
    ) {
        Text(
            text = title, // Menampilkan judul
            style = MaterialTheme.typography.titleLarge, // Menggunakan style untuk judul besar
            fontWeight = FontWeight.Bold, // Menebalkan font
            color = MaterialTheme.colorScheme.tertiary // Menggunakan warna sekunder untuk teks
        )
        Text(
            text = date, // Menampilkan tanggal
            style = MaterialTheme.typography.bodyMedium, // Menggunakan style untuk teks biasa
        )
    }
}
