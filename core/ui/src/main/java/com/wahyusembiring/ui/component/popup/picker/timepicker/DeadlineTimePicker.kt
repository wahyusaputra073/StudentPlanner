package com.wahyusembiring.ui.component.popup.picker.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.wahyusembiring.data.model.DeadlineTime
import com.wahyusembiring.datetime.Moment
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class) // Menandakan penggunaan API eksperimental
@Composable
fun DeadlineTimePicker(
    modifier: Modifier = Modifier, // Modifier untuk penyesuaian tampilan
    title: String = stringResource(R.string.when_deadline), // Judul picker, default mengambil dari resource string
    onTimeSelected: (time: DeadlineTime) -> Unit, // Callback ketika waktu dipilih
    onDismissRequest: () -> Unit, // Callback ketika dialog dibatalkan
) {
    val moment = remember { Moment.now() } // Mengambil waktu saat ini
    val timePickerState = rememberTimePickerState( // Menginisialisasi state picker waktu dengan jam dan menit saat ini
        initialHour = moment.hour,
        initialMinute = moment.minute
    )

    Dialog(onDismissRequest = onDismissRequest) { // Menampilkan dialog dengan aksi dismiss
        Surface(
            modifier = modifier, // Menggunakan modifier yang diberikan
            color = AlertDialogDefaults.containerColor, // Menentukan warna latar belakang
            shape = AlertDialogDefaults.shape, // Menentukan bentuk dialog
            tonalElevation = AlertDialogDefaults.TonalElevation // Menentukan elevasi tonal
        ) {
            Column(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.Large), // Padding vertikal di kolom
                horizontalAlignment = Alignment.CenterHorizontally, // Menyelaraskan konten ke tengah secara horizontal
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.Large) // Padding horizontal
                        .padding(bottom = MaterialTheme.spacing.Medium), // Padding bawah
                    color = MaterialTheme.colorScheme.primary, // Warna teks menggunakan warna utama
                    text = title // Menampilkan judul
                )
                androidx.compose.material3.TimePicker(state = timePickerState) // Menampilkan widget time picker
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Memastikan tombol memenuhi lebar penuh
                        .padding(horizontal = MaterialTheme.spacing.Large), // Padding horizontal
                    horizontalArrangement = Arrangement.End // Menyelaraskan tombol ke kanan
                ) {
                    TextButton(onClick = onDismissRequest) { // Tombol untuk membatalkan
                        Text(text = stringResource(id = R.string.cancel)) // Teks tombol dibaca dari resource string
                    }
                    TextButton(
                        onClick = {
                            val time = DeadlineTime(timePickerState.hour, timePickerState.minute) // Membuat objek DeadlineTime berdasarkan waktu yang dipilih
                            onTimeSelected(time) // Mengirim waktu yang dipilih ke callback
                            onDismissRequest() // Menutup dialog setelah pemilihan
                        }
                    ) {
                        Text(text = stringResource(id = R.string.confirm)) // Teks tombol konfirmasi
                    }
                }
            }
        }
    }
}