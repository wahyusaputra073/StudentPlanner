package com.wahyusembiring.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerState
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wahyusembiring.calendar.util.getEventsByDate
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.CollectAsOneTimeEvent
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Reminder
import com.wahyusembiring.ui.component.eventcard.EventCard
import com.wahyusembiring.ui.component.topappbar.TopAppBar
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.adjustHSL
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.day.DefaultDay
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle.FULL
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: CalendarScreenViewModel, // ViewModel untuk layar kalender
    navController: NavHostController, // Kontroler navigasi untuk berpindah antar layar
    drawerState: DrawerState // Status drawer untuk membuka atau menutup menu samping
) {
    val state = viewModel.state.collectAsStateWithLifecycle() // Mengamati state layar dengan lifecycle-aware
    val coroutineScope = rememberCoroutineScope() // Coroutine scope untuk operasi asinkron

    CollectAsOneTimeEvent(viewModel.navigationEvent) { // Mengumpulkan event navigasi sekali waktu
        when (it) {
            is CalendarScreenNavigationEvent.NavigateToHomeworkDetail -> {
                navController.navigate(Screen.CreateHomework(it.homeworkId)) // Navigasi ke detail tugas
            }
            is CalendarScreenNavigationEvent.NavigateToExamDetail -> {
                navController.navigate(Screen.CreateExam(it.examkId)) // Navigasi ke detail ujian
            }
            is CalendarScreenNavigationEvent.NavigateToReminderDetail -> {
                navController.navigate(Screen.CreateReminder(it.reminderId)) // Navigasi ke detail pengingat
            }
            else -> {} // Tidak ada tindakan untuk event lain
        }
    }

    CalendarScreen(
        state = state.value, // Menyediakan state layar ke komposisi
        onUIEvent = viewModel::onUIEvent, // Callback untuk menangani event UI
        onHamburgerMenuClick = {
            coroutineScope.launch { drawerState.open() } // Membuka drawer dengan coroutine
        }
    )
}


@Composable
private fun CalendarScreen(
    state: CalendarScreenUIState, // State layar yang berisi data dan status untuk kalender
    onUIEvent: (CalendarScreenUIEvent) -> Unit, // Callback untuk menangani event dari UI
    onHamburgerMenuClick: () -> Unit, // Callback untuk klik menu hamburger
) {
    val context = LocalContext.current // Mendapatkan konteks lokal
    var selectedDate: LocalDate? by remember { mutableStateOf(LocalDate.now()) } // Menyimpan tanggal yang dipilih
    val calendarState = rememberSelectableCalendarState( // State untuk kalender yang dapat dipilih
        initialSelection = listOf(LocalDate.now()), // Tanggal awal yang dipilih
        confirmSelectionChange = {
            selectedDate = it.firstOrNull() // Mengatur tanggal yang dipilih
            true // Mengonfirmasi perubahan pilihan
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.calendar), // Judul toolbar
                onMenuClick = onHamburgerMenuClick // Aksi saat menu hamburger diklik
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // Mengatur padding sesuai scaffold
                .fillMaxSize() // Mengisi ukuran penuh
        ) {
            SelectableCalendar(
                modifier = Modifier.padding(
                    horizontal = MaterialTheme.spacing.Medium,
                    vertical = MaterialTheme.spacing.Small
                ), // Margin untuk kalender
                calendarState = calendarState, // State kalender yang digunakan
                monthHeader = {
                    MonthHeader(monthState = it) // Header untuk bulan
                },
                dayContent = {
                    DefaultDay(state = it) // Tampilan default untuk hari
                    EventIndicator(dayState = it, events = state.events) // Indikator event di hari tertentu
                }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Large)) // Spasi vertikal
            selectedDate?.let { date -> // Hanya dijalankan jika tanggal terpilih ada
                EventCard(
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.spacing.Large,
                        vertical = MaterialTheme.spacing.Small
                    ), // Margin untuk kartu event
                    events = state.events.getEventsByDate(date), // Mendapatkan event berdasarkan tanggal
                    onEventClick = {
                        onUIEvent(CalendarScreenUIEvent.OnEventClick(it)) // Event saat event diklik
                    },
                    onDeletedEventClick = { event ->
                        onUIEvent(CalendarScreenUIEvent.OnDeleteEvent(event)) // Event saat event dihapus
                    },
                    onEventCheckedChange = { event, isChecked ->
                        onUIEvent(CalendarScreenUIEvent.OnEventCompletedStateChange(event, isChecked)) // Event saat status selesai berubah
                    },
                )
            }
        }
    }
}

@Composable
private fun MonthHeader(
    monthState: MonthState, // State yang menyimpan bulan yang sedang aktif
    modifier: Modifier = Modifier, // Modifier opsional untuk penyesuaian UI
) {
    Row(
        modifier = modifier.fillMaxWidth(), // Membuat baris selebar mungkin
        horizontalArrangement = Arrangement.Center, // Menyusun elemen secara horizontal di tengah
        verticalAlignment = Alignment.CenterVertically, // Menyusun elemen secara vertikal di tengah
    ) {
        DecrementButton(monthState = monthState) // Tombol untuk mengurangi bulan
        Text(
            text = monthState.currentMonth.month
                .getDisplayName(FULL, Locale.getDefault()) // Mendapatkan nama bulan dalam format lengkap
                .lowercase() // Mengubah semua huruf menjadi kecil
                .replaceFirstChar { it.titlecase() }, // Huruf pertama menjadi kapital
            style = MaterialTheme.typography.titleMedium, // Gaya teks sesuai tema
        )
        Spacer(modifier = Modifier.width(8.dp)) // Memberikan spasi horizontal antar elemen
        Text(
            text = monthState.currentMonth.year.toString(), // Menampilkan tahun aktif
            style = MaterialTheme.typography.titleMedium // Gaya teks sesuai tema
        )
        IncrementButton(monthState = monthState) // Tombol untuk menambah bulan
    }
}


@Composable
private fun DecrementButton(
    monthState: MonthState, // State yang menyimpan bulan aktif
) {
    IconButton(
        enabled = monthState.currentMonth > monthState.minMonth, // Tombol aktif jika bulan sekarang lebih besar dari bulan minimum
        onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) } // Mengubah bulan ke bulan sebelumnya
    ) {
        Image(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, // Ikon panah ke kiri
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface), // Warna ikon sesuai tema
            contentDescription = "Previous", // Deskripsi konten untuk aksesibilitas
        )
    }
}

@Composable
private fun IncrementButton(
    monthState: MonthState, // State yang menyimpan bulan aktif
) {
    IconButton(
        enabled = monthState.currentMonth < monthState.maxMonth, // Mengaktifkan tombol jika bulan saat ini lebih kecil dari batas maksimum
        onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) } // Mengubah bulan ke bulan berikutnya
    ) {
        Image(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // Ikon dengan panah ke kanan
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface), // Memberikan warna sesuai tema pada ikon
            contentDescription = "Next", // Deskripsi ikon untuk tujuan aksesibilitas
        )
    }
}



@Composable
private fun BoxScope.EventIndicator(
    dayState: DayState<DynamicSelectionState>, // Menyimpan status hari yang dipilih
    events: List<Any> // Daftar semua event yang ada
) {
    val eventInThisDay = events.getEventsByDate(dayState.date) // Mendapatkan event pada tanggal tertentu
    val isThereAnyExam = eventInThisDay.any { it is ExamWithSubject } // Cek apakah ada ujian pada hari ini
    val isThereAnyHomework = eventInThisDay.any { it is HomeworkWithSubject } // Cek apakah ada tugas pada hari ini
    val isThereAnyReminder = eventInThisDay.any { it is Reminder } // Cek apakah ada pengingat pada hari ini

    Row(
        modifier = Modifier
            .padding(bottom = 6.dp) // Padding di bagian bawah
            .fillMaxWidth() // Mengisi lebar penuh
            .align(Alignment.BottomCenter), // Menempatkan indikator di bagian bawah tengah
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isThereAnyHomework) { // Jika ada tugas
            Box(
                modifier = Modifier
                    .padding(2.dp) // Padding antar indikator
                    .size(6.dp) // Ukuran indikator
                    .background(
                        color = MaterialTheme.colorScheme.primary, // Warna indikator tugas
                        shape = RoundedCornerShape(50) // Bentuk indikator bulat
                    )
            )
        }
        if (isThereAnyExam) { // Jika ada ujian
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.adjustHSL(hue = 120f), // Warna indikator ujian
                        shape = RoundedCornerShape(50)
                    )
            )
        }
        if (isThereAnyReminder) { // Jika ada pengingat
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.adjustHSL(hue = 180f), // Warna indikator pengingat
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}