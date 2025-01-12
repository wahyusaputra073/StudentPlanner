package com.wahyusembiring.ui.component.eventcard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import com.wahyusembiring.common.util.withZeroPadding
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.entity.Reminder
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.UIText


data class EventCard(
    val title: UIText,
    val date: UIText,
    val events: List<Any>
)

@Composable
fun EventCard(
    modifier: Modifier = Modifier, // Modifier untuk mengatur tampilan kartu
    events: List<Any> = emptyList(), // Daftar event yang akan ditampilkan
    onEventCheckedChange: (event: Any, isChecked: Boolean) -> Unit = { _, _ -> }, // Fungsi untuk menangani perubahan status centang pada event
    onEventClick: (event: Any) -> Unit = {}, // Fungsi untuk menangani klik pada event
    onDeletedEventClick: (event: Any) -> Unit = {} // Fungsi untuk menangani klik pada tombol hapus event
) {
    Card(
        modifier = modifier.fillMaxWidth() // Menyusun Card agar memenuhi lebar layar
    ) {
        Header() // Menampilkan header di dalam kartu
        if (events.isEmpty()) { // Jika tidak ada event
            NoEventBody() // Tampilkan tampilan kosong
        } else { // Jika ada event
            Body(
                events = events, // Kirim daftar event ke Body
                onEventCheckedChange = onEventCheckedChange, // Kirim fungsi perubahan centang
                onClick = onEventClick, // Kirim fungsi klik event
                onDeleteEventClick = onDeletedEventClick // Kirim fungsi klik hapus event
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Large)) // Spacer untuk memberikan jarak bawah yang besar
    }
}


@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Membuat Row memenuhi lebar layar
            .padding(MaterialTheme.spacing.Medium), // Memberikan padding di sekitar Row
        verticalAlignment = Alignment.CenterVertically, // Menyusun elemen secara vertikal di tengah
        horizontalArrangement = Arrangement.Start // Menyusun elemen secara horizontal di kiri
    ) {
        Box(
            modifier = Modifier
                .background( // Memberikan latar belakang dengan warna utama dan bentuk bulat
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center // Menyusun konten di tengah Box
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp) // Ukuran ikon
                    .padding(8.dp), // Padding di sekitar ikon
                painter = painterResource(id = R.drawable.ic_event), // Ikon event
                contentDescription = stringResource(R.string.event_icon), // Deskripsi ikon untuk aksesibilitas
                tint = MaterialTheme.colorScheme.onPrimary // Menyesuaikan warna ikon dengan warna latar belakang
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small)) // Memberikan jarak antara ikon dan teks
        Text(
            text = stringResource(R.string.event_overview), // Menampilkan teks "Event Overview"
            style = MaterialTheme.typography.titleMedium, // Gaya teks dengan ukuran sedang
            color = MaterialTheme.colorScheme.primary, // Warna teks sesuai dengan warna utama
        )
    }
}


@Composable
private fun Body(
    events: List<Any>, // Daftar event yang akan ditampilkan
    onClick: (event: Any) -> Unit = {}, // Fungsi untuk menangani klik event
    onDeleteEventClick: (event: Any) -> Unit = {}, // Fungsi untuk menangani klik hapus event
    onEventCheckedChange: (event: Any, isChecked: Boolean) -> Unit // Fungsi untuk menangani perubahan status centang
) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Membuat Column memenuhi lebar layar
            .padding(horizontal = MaterialTheme.spacing.Medium), // Memberikan padding horizontal
    ) {
        for (event in events) { // Iterasi untuk setiap event dalam daftar events
            when (event) { // Menentukan tipe event dan menampilkan elemen yang sesuai
                is ExamWithSubject -> { // Jika event adalah ExamWithSubject
                    BodyEventList(
                        onClick = { onClick(event) }, // Menangani klik pada event
                        onDeletedClick = { onDeleteEventClick(event) }, // Menangani klik hapus event
                        isChecked = event.exam.score != null, // Menandakan apakah ujian sudah dinilai
                        onCheckedChange = { onEventCheckedChange(event, it) }, // Menangani perubahan status centang
                        title = event.exam.title, // Judul ujian
                        subjectColor = event.subject.color, // Warna mata pelajaran
                        subjectName = event.subject.name, // Nama mata pelajaran
                        eventType = stringResource(R.string.exam), // Tipe event (ujian)
                        times = event.exam.deadline, // Tenggat waktu ujian
                        duration = null, // Durasi tidak digunakan untuk ujian
                    )
                }

                is HomeworkWithSubject -> { // Jika event adalah HomeworkWithSubject
                    BodyEventList(
                        onClick = { onClick(event) },
                        onDeletedClick = { onDeleteEventClick(event) },
                        isChecked = event.homework.completed, // Menandakan apakah pekerjaan rumah sudah selesai
                        onCheckedChange = { onEventCheckedChange(event, it) },
                        title = event.homework.title, // Judul pekerjaan rumah
                        subjectColor = event.subject.color, // Warna mata pelajaran
                        subjectName = event.subject.name, // Nama mata pelajaran
                        eventType = stringResource(R.string.task), // Tipe event (tugas)
                        times = event.homework.deadline, // Tenggat waktu pekerjaan rumah
                        duration = null, // Durasi tidak digunakan untuk pekerjaan rumah
                    )
                }

                is Reminder -> { // Jika event adalah Reminder
                    BodyEventList(
                        onClick = { onClick(event) },
                        onDeletedClick = { onDeleteEventClick(event) },
                        isChecked = event.completed, // Menandakan apakah pengingat sudah diselesaikan
                        onCheckedChange = { onEventCheckedChange(event, it) },
                        title = event.title, // Judul pengingat
                        subjectColor = null, // Tidak ada warna mata pelajaran untuk pengingat
                        subjectName = null, // Tidak ada nama mata pelajaran untuk pengingat
                        eventType = stringResource(R.string.agenda), // Tipe event (agenda)
                        times = null, // Tidak ada waktu untuk pengingat
                        duration = event.duration // Durasi pengingat
                    )
                }
            }
        }
    }
}

@Composable
private fun BodyEventList(
    isChecked: Boolean, // Menyimpan status centang
    onCheckedChange: (Boolean) -> Unit, // Fungsi untuk menangani perubahan centang
    title: String, // Judul event
    times: DeadlineTime?, // Waktu tenggat (optional)
    duration: SpanTime?, // Durasi event (optional)
    onClick: () -> Unit = {}, // Fungsi untuk menangani klik pada event
    onDeletedClick: () -> Unit = {}, // Fungsi untuk menangani klik hapus event
    subjectColor: Color?, // Warna mata pelajaran (optional)
    subjectName: String?, // Nama mata pelajaran (optional)
    eventType: String // Jenis event (contoh: "tugas", "ujian")
) {
    var checkBoxWidth by remember { mutableIntStateOf(0) } // Menyimpan lebar checkbox untuk pengaturan layout

    Column(
        modifier = Modifier
            .clickable { onClick() }, // Membuat seluruh Column dapat diklik
        verticalArrangement = Arrangement.spacedBy(15.dp) // Memberikan jarak antar elemen vertikal
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(), // Membuat Row memenuhi lebar layar
            verticalAlignment = Alignment.CenterVertically // Menyusun elemen secara vertikal di tengah
        ) {
            Checkbox(
                modifier = Modifier.onSizeChanged { checkBoxWidth = it.width }, // Menyimpan lebar checkbox
                checked = isChecked, // Status centang checkbox
                onCheckedChange = onCheckedChange // Fungsi untuk menangani perubahan centang
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small)) // Memberikan jarak antara checkbox dan teks

            Column(modifier = Modifier) {
                Text(
                    text = title, // Menampilkan judul event
                    style = MaterialTheme.typography.titleSmall.let {
                        if (!isChecked) it else it.copy( // Menambahkan efek coret jika sudah dicentang
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                )

                Row(modifier = Modifier) {
                    Text(
                        text = "($eventType)", // Menampilkan tipe event
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small)) // Memberikan jarak

                    // Menampilkan durasi event jika ada
                    if (duration != null) {
                        Text(
                            text = stringResource(
                                R.string.events_from_to,
                                duration.startTime.hour.withZeroPadding(),
                                duration.startTime.minute.withZeroPadding(),
                                duration.endTime.hour.withZeroPadding(),
                                duration.endTime.minute.withZeroPadding()
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Menampilkan waktu tenggat jika ada
                    if (times != null) {
                        Text(
                            text = "${times.hour.withZeroPadding()}:${times.minute.withZeroPadding()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically // Menyusun elemen secara vertikal di tengah
                ) {
                    // Menampilkan warna dan nama mata pelajaran jika ada
                    if (subjectName != null && subjectColor != null) {
                        Box(
                            modifier = Modifier
                                .size(8.dp) // Ukuran lingkaran warna
                                .background(
                                    color = subjectColor, // Warna mata pelajaran
                                    shape = RoundedCornerShape(50) // Membuat bentuk bulat
                                )
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small)) // Memberikan jarak

                        Text(
                            text = subjectName, // Nama mata pelajaran
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Memberikan ruang agar ikon dan dropdown menu berada di ujung kanan

            Column {
                var expanded by remember { mutableStateOf(false) } // Status apakah dropdown menu terbuka

                // Tombol untuk membuka menu dropdown
                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon menu vertikal
                        contentDescription = null
                    )
                }
                // Menu dropdown yang muncul ketika tombol ditekan
                DropdownMenu(
                    expanded = expanded, // Menampilkan dropdown jika expanded = true
                    onDismissRequest = {
                        expanded = false // Menutup dropdown jika diluar area diklik
                    }
                ) {
                    // Menu item untuk menghapus event
                    DropdownMenuItem(
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete), // Ikon hapus
                                contentDescription = stringResource(R.string.delete),
                                tint = MaterialTheme.colorScheme.error // Warna merah untuk delete
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.delete), // Teks "Delete"
                                color = MaterialTheme.colorScheme.error // Warna merah untuk teks
                            )
                        },
                        onClick = { onDeletedClick() } // Menangani klik untuk menghapus event
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small)) // Memberikan jarak bawah
    }
}


@Composable
private fun NoEventBody() {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Membuat Column memenuhi lebar layar
            .padding(horizontal = MaterialTheme.spacing.Medium), // Memberikan padding horizontal
    ) {
        AsyncImage(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.Medium), // Memberikan padding pada gambar
            model = R.drawable.relaxing, // Gambar yang ditampilkan ketika tidak ada event
            contentDescription = stringResource(R.string.no_event_picture), // Deskripsi gambar untuk aksesibilitas
            imageLoader = LocalContext.current.imageLoader // Menyediakan image loader lokal
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan jarak antara gambar dan teks
        Text(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.Small), // Padding vertikal untuk teks
            text = "There are no events", // Teks yang menampilkan tidak ada event
            style = MaterialTheme.typography.titleMedium // Gaya teks medium
        )
        Text(
            text = "You can add new events with the button in the bottom right corner", // Teks petunjuk untuk menambah event
            style = MaterialTheme.typography.bodyMedium // Gaya teks medium
        )
    }
}

@Preview
@Composable
private fun EventCardPreview() {
    EventCard()
}