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

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import kotlinx.coroutines.launch


@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel,
    navController: NavHostController,
    drawerState: DrawerState
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 5 // Scroll ke Today saat pertama kali dibuka
    )

    OverviewScreen(
        state = state,
        listState = listState,
        onUIEvent = viewModel::onUIEvent,
        onHamburgerMenuClick = {
            coroutineScope.launch { drawerState.open() }
        },
        onNavigateTo = {
            navController.navigate(it)
        },
        onRefresh = {
            coroutineScope.launch {
                viewModel.onUIEvent(OverviewScreenUIEvent.OnRefresh)
                listState.animateScrollToItem(5)
            }
        }
    )
}



@Composable
private fun OverviewScreen(
    modifier: Modifier = Modifier,
    state: OverviewScreenUIState,
    listState: LazyListState,
    onUIEvent: (OverviewScreenUIEvent) -> Unit,
    onHamburgerMenuClick: () -> Unit,
    onNavigateTo: (Screen) -> Unit,
    onRefresh: () -> Unit
) {
    var fabExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.overview),
                onMenuClick = onHamburgerMenuClick,
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            HomeworkExamAndReminderFAB(
                isExpanded = fabExpanded,
                onClick = { fabExpanded = !fabExpanded },
                onDismiss = { fabExpanded = false },
                onReminderFabClick = { onNavigateTo(Screen.CreateReminder()) },
                onExamFabClick = { onNavigateTo(Screen.CreateExam()) },
                onHomeworkFabClick = { onNavigateTo(Screen.CreateHomework()) },
            )
        }
    ) {
        OverviewScreenMainContent(
            modifier = modifier.padding(it),
            state = state,
            listState = listState,
            onUIEvent = onUIEvent,
            onNavigateTo = onNavigateTo
        )
    }
}



@Suppress("t")
@Composable
private fun OverviewScreenMainContent(
    modifier: Modifier = Modifier,
    state: OverviewScreenUIState,
    listState: LazyListState,
    onUIEvent: (OverviewScreenUIEvent) -> Unit,
    onNavigateTo: (Screen) -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
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
