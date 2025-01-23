package com.wahyusembiring.ui.component.officehourinput

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.wahyusembiring.common.util.withZeroPadding
import com.wahyusembiring.data.model.OfficeHour
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.component.dropdown.Dropdown
import com.wahyusembiring.ui.component.popup.picker.timepicker.TimePicker
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.UIText

@Composable
fun OfficeHourInput(
    modifier: Modifier = Modifier, // Modifier untuk penyesuaian tata letak
    officeHours: List<OfficeHour>, // Daftar jam kantor yang sudah ada
    onNewOfficeHour: (OfficeHour) -> Unit, // Fungsi untuk menambahkan jam kantor baru
    onDeleteOfficeHour: (OfficeHour) -> Unit // Fungsi untuk menghapus jam kantor
) {
    var showOfficeHourDialog by remember { mutableStateOf(false) } // State untuk menampilkan dialog input jam kantor

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // Mengisi lebar baris
            horizontalArrangement = Arrangement.SpaceBetween, // Menyusun konten secara horizontal
            verticalAlignment = Alignment.CenterVertically // Menyusun konten secara vertikal di tengah
        ) {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.Medium), // Padding kiri untuk teks
                text = stringResource(R.string.office_hour), // Teks untuk judul "Office Hour"
                color = MaterialTheme.colorScheme.primary, // Menggunakan warna primer dari tema
                style = MaterialTheme.typography.titleMedium // Gaya teks untuk judul
            )
            IconButton(
                onClick = { showOfficeHourDialog = true } // Menampilkan dialog saat ikon ditekan
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), // Ikon tambah jam kantor
                    contentDescription = null, // Tidak ada deskripsi untuk ikon
                    tint = MaterialTheme.colorScheme.primary // Warna ikon sesuai tema
                )
            }
        }
        HorizontalDivider() // Pembatas horizontal antara judul dan daftar jam kantor
        Column(
            modifier = Modifier.padding(start = MaterialTheme.spacing.Large) // Padding kiri untuk konten daftar
        ) {
            if (officeHours.isEmpty()) { // Jika tidak ada jam kantor, tampilkan konten kosong
                EmptyOfficeHour()
            }
            for (officeHour in officeHours) { // Menampilkan daftar jam kantor
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small)) // Memberikan jarak antar item
                OfficeHourListItem(officeHour = officeHour,
                    onDeleteClick = { onDeleteOfficeHour(officeHour) }) // Tampilkan item dengan fungsi hapus
            }
        }
    }

    if (showOfficeHourDialog) { // Jika dialog tampil, tampilkan dialog input jam kantor
        OfficeHourInputDialog(
            onDismissRequest = { showOfficeHourDialog = false }, // Menutup dialog
            onOfficeHourAddClick = onNewOfficeHour // Menambahkan jam kantor baru
        )
    }
}



@Composable
fun OfficeHourListItem(
    officeHour: OfficeHour, // Jam kantor yang akan ditampilkan
    onDeleteClick: () -> Unit // Fungsi yang dijalankan saat tombol hapus diklik
) {
    var expanded by remember { mutableStateOf(false) } // State untuk menampilkan dropdown menu
    val days = stringArrayResource(id = R.array.days) // Daftar nama hari

    Row(
        modifier = Modifier.requiredHeight(IntrinsicSize.Min), // Menyesuaikan tinggi row dengan ukuran minimum kontennya
        horizontalArrangement = Arrangement.Start, // Menyusun elemen secara horizontal dari kiri
        verticalAlignment = Alignment.CenterVertically // Menyusun elemen secara vertikal di tengah
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_briefcase), // Ikon briefcase untuk jam kantor
            tint = MaterialTheme.colorScheme.secondary, // Warna ikon sesuai tema
            contentDescription = null // Tidak ada deskripsi untuk ikon
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Memberikan jarak antar ikon dan divider
        VerticalDivider(
            modifier = Modifier.fillMaxHeight() // Divider vertikal yang mengisi tinggi row
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Memberikan jarak antar divider dan konten teks
        Column {
            Text(
                text = days[officeHour.day] // Menampilkan nama hari sesuai indeks
            )
            Text(
                text = stringResource(
                    R.string.office_hour_from_to, // Format waktu mulai dan selesai
                    officeHour.startTime.hour, // Jam mulai
                    officeHour.startTime.minute, // Menit mulai
                    officeHour.endTime.hour, // Jam selesai
                    officeHour.endTime.minute // Menit selesai
                )
            )
        }

        Box {
            IconButton(
                onClick = { expanded = true }, // Menampilkan dropdown menu saat tombol diklik
                modifier = Modifier.size(24.dp) // Ukuran ikon titik tiga
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon titik tiga untuk menu opsi
                    contentDescription = "Options", // Deskripsi untuk ikon
                    tint = MaterialTheme.colorScheme.secondary // Warna ikon sesuai tema
                )
            }
            // Dropdown menu
            DropdownMenu(
                expanded = expanded, // Menentukan apakah menu terbuka atau tidak
                onDismissRequest = { expanded = false } // Menutup menu jika area di luar diklik
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") }, // Teks untuk opsi hapus
                    onClick = {
                        onDeleteClick() // Menjalankan fungsi hapus
                        expanded = false // Menutup dropdown setelah klik
                    }
                )
            }
        }
    }
}


@Composable
fun EmptyOfficeHour() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Membuat Box yang mengisi lebar penuh
        contentAlignment = Alignment.Center // Menyusun konten di tengah Box
    ) {
        Text(
            modifier = Modifier.padding( // Padding untuk teks
                horizontal = MaterialTheme.spacing.Medium, // Padding horizontal
                vertical = MaterialTheme.spacing.Small // Padding vertikal
            ),
            text = stringResource(R.string.no_office_hour), // Teks untuk menampilkan pesan jika tidak ada jam kantor
            style = MaterialTheme.typography.bodyMedium.copy( // Gaya teks dengan font medium
                fontStyle = FontStyle.Italic // Mengatur gaya font menjadi miring
            )
        )
    }
}

enum class TimeType { // Enum untuk tipe waktu
    START, // Waktu mulai
    END // Waktu selesai
}


@Composable
fun OfficeHourInputDialog(
    onDismissRequest: () -> Unit, // Fungsi yang dipanggil ketika dialog ditutup
    initialOfficeHour: OfficeHour = OfficeHour( // Jam kantor awal yang akan ditampilkan di dialog
        day = 1,
        startTime = Time(7, 0),
        endTime = Time(17, 0)
    ),
    onOfficeHourAddClick: (OfficeHour) -> Unit // Fungsi untuk menambahkan jam kantor baru
) {
    var officeHour by remember {
        mutableStateOf(initialOfficeHour) // Menyimpan jam kantor yang sedang diedit
    }
    var showTimePickerDialog by remember {
        mutableStateOf<TimeType?>(null) // State untuk menampilkan dialog pemilih waktu
    }

    val days = stringArrayResource(id = R.array.days) // Daftar nama hari

    AlertDialog(
        onDismissRequest = onDismissRequest, // Fungsi untuk menutup dialog
        title = {
            Text(text = stringResource(R.string.enter_office_hour)) // Judul dialog
        },
        text = {
            Column {
                Text(text = stringResource(R.string.day)) // Teks untuk memilih hari
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small)) // Spacer antara elemen
                Dropdown(
                    items = days.toList(), // Menampilkan daftar hari dalam dropdown
                    selected = days[officeHour.day], // Menampilkan hari yang dipilih
                    title = {
                        if (it != null) {
                            UIText.DynamicString(it) // Menampilkan nama hari
                        } else {
                            UIText.StringResource(R.string.no_day_selected) // Pesan jika hari tidak dipilih
                        }
                    },
                    onItemClick = {
                        officeHour = officeHour.copy(
                            day = days.indexOf(it) // Menyimpan indeks hari yang dipilih
                        )
                    },
                    emptyContent = {}
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
                Row(
                    modifier = Modifier.fillMaxWidth() // Row untuk memilih waktu mulai dan selesai
                ) {
                    Column(
                        modifier = Modifier.weight(1f) // Kolom untuk waktu mulai
                    ) {
                        Text(text = stringResource(R.string.from)) // Teks untuk waktu mulai
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))
                        Row(
                            modifier = Modifier
                                .clickable {
                                    showTimePickerDialog = TimeType.START // Menampilkan dialog waktu mulai
                                }
                                .border(
                                    color = MaterialTheme.colorScheme.primary,
                                    width = 1.dp,
                                    shape = MaterialTheme.shapes.small
                                ),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.spacing.Small)
                                    .padding(start = MaterialTheme.spacing.Small),
                                painter = painterResource(id = R.drawable.ic_clock), // Ikon jam
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.spacing.Small,
                                        horizontal = MaterialTheme.spacing.Medium
                                    ),
                                text = stringResource(
                                    R.string.time_colon, // Menampilkan waktu mulai dengan format jam dan menit
                                    officeHour.startTime.hour.withZeroPadding(),
                                    officeHour.startTime.minute.withZeroPadding()
                                )
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f) // Kolom untuk waktu selesai
                    ) {
                        Text(text = stringResource(R.string.until)) // Teks untuk waktu selesai
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))
                        Row(
                            modifier = Modifier
                                .clickable {
                                    showTimePickerDialog = TimeType.END // Menampilkan dialog waktu selesai
                                }
                                .border(
                                    color = MaterialTheme.colorScheme.primary,
                                    width = 1.dp,
                                    shape = MaterialTheme.shapes.small
                                ),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.spacing.Small)
                                    .padding(start = MaterialTheme.spacing.Small),
                                painter = painterResource(id = R.drawable.ic_clock), // Ikon jam
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.spacing.Small,
                                        horizontal = MaterialTheme.spacing.Medium
                                    ),
                                text = stringResource(
                                    R.string.time_colon, // Menampilkan waktu selesai dengan format jam dan menit
                                    officeHour.endTime.hour.withZeroPadding(),
                                    officeHour.endTime.minute.withZeroPadding()
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onOfficeHourAddClick(officeHour) // Menambahkan jam kantor baru saat tombol konfirmasi diklik
                    onDismissRequest().also { officeHour = initialOfficeHour } // Menutup dialog dan reset data
                }
            ) {
                Text(text = stringResource(R.string.add)) // Teks untuk tombol konfirmasi
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() } // Menutup dialog tanpa perubahan
            ) {
                Text(text = stringResource(R.string.cancel)) // Teks untuk tombol batal
            }
        }
    )
    if (showTimePickerDialog != null) { // Menampilkan dialog waktu saat pengguna memilih waktu mulai atau selesai
        TimePicker(
            title = when (showTimePickerDialog!!) {
                TimeType.START -> stringResource(R.string.available_from) // Judul untuk waktu mulai
                TimeType.END -> stringResource(R.string.until) // Judul untuk waktu selesai
            },
            onDismissRequest = { showTimePickerDialog = null }, // Menutup dialog waktu
            onTimeSelected = {
                officeHour = when (showTimePickerDialog!!) {
                    TimeType.START -> officeHour.copy(startTime = it) // Mengupdate waktu mulai
                    TimeType.END -> officeHour.copy(endTime = it) // Mengupdate waktu selesai
                }
            }
        )
    }
}