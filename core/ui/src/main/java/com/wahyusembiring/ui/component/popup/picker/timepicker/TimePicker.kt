package com.wahyusembiring.ui.component.popup.picker.timepicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.datetime.Moment
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class) // Menandai penggunaan API eksperimental Material 3
@Composable
fun TimePicker(
    modifier: Modifier = Modifier, // Modifier untuk mendekorasi composable ini
    title: String = stringResource(R.string.when_do_you_want_to_be_reminded), // Judul default dialog
    onTimeSelected: (time: Time) -> Unit, // Callback untuk mengirim waktu yang dipilih
    onDismissRequest: () -> Unit, // Callback untuk menutup dialog
) {
    val moment = remember { Moment.now() } // Menyimpan waktu saat ini menggunakan remember
    val timePickerState = rememberTimePickerState(
        initialHour = moment.hour, // Inisialisasi jam awal dari waktu saat ini
        initialMinute = moment.minute // Inisialisasi menit awal dari waktu saat ini
    )

    Dialog(onDismissRequest = onDismissRequest) { // Membuat dialog yang dapat ditutup
        Surface(
            modifier = modifier, // Menerapkan modifier
            color = AlertDialogDefaults.containerColor, // Warna background dialog
            shape = AlertDialogDefaults.shape, // Bentuk dialog
            tonalElevation = AlertDialogDefaults.TonalElevation // Elevasi tonal dialog
        ) {
            Column(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.Large), // Padding vertikal
                horizontalAlignment = Alignment.CenterHorizontally, // Konten sejajar horizontal di tengah
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.Large) // Padding horizontal
                        .padding(bottom = MaterialTheme.spacing.Medium), // Padding bawah
                    color = MaterialTheme.colorScheme.primary, // Warna teks
                    text = title // Menampilkan teks judul
                )
                androidx.compose.material3.TimePicker(state = timePickerState) // Komponen time picker
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // Baris memenuhi lebar layar
                        .padding(horizontal = MaterialTheme.spacing.Large), // Padding horizontal
                    horizontalArrangement = Arrangement.End // Elemen di akhir baris
                ) {
                    TextButton(onClick = onDismissRequest) { // Tombol untuk membatalkan dialog
                        Text(text = stringResource(id = R.string.cancel)) // Teks tombol "Cancel"
                    }
                    TextButton(
                        onClick = {
                            val time = Time(timePickerState.hour, timePickerState.minute) // Membuat objek Time dari pilihan pengguna
                            onTimeSelected(time) // Memanggil callback dengan waktu yang dipilih
                            onDismissRequest() // Menutup dialog
                        }
                    ) {
                        Text(text = stringResource(id = R.string.confirm)) // Teks tombol "Confirm"
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TimePickerPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TimePicker(
                onTimeSelected = {},
                onDismissRequest = {},
            )
        }
    }
}