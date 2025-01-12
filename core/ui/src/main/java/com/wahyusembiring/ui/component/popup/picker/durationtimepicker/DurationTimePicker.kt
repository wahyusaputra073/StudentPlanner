package com.wahyusembiring.ui.component.popup.picker.durationtimepicker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wahyusembiring.common.util.withZeroPadding
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.component.popup.picker.timepicker.TimePicker
import com.wahyusembiring.ui.theme.spacing

@Composable
fun DurationTimePicker(
    modifier: Modifier = Modifier, // Modifier untuk mengatur tampilan
    onDismissRequest: () -> Unit, // Callback saat dialog ditutup
    initialDurationTime: SpanTime = SpanTime( // Durasi waktu awal yang digunakan (jam mulai dan selesai)
        startTime = Time(7, 0),
        endTime = Time(17, 0)
    ),
    onDurationSelected: (SpanTime) -> Unit, // Callback saat durasi waktu dipilih
) {
    // State untuk menyimpan waktu durasi yang dipilih (nullable)
    var durationTime by remember { mutableStateOf<SpanTime?>(initialDurationTime) }
    // State untuk menunjukkan apakah dialog pemilih waktu harus ditampilkan
    var showTimePickerDialog by remember { mutableStateOf<DuraTimeType?>(null) }

    // Dialog untuk memilih waktu durasi
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier, // Modifikasi tampilan
            color = AlertDialogDefaults.containerColor,
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Teks judul untuk dialog
                Text(
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.Large)
                        .padding(bottom = MaterialTheme.spacing.Medium),
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(R.string.enter_office_hour) // Menampilkan teks judul
                )

                // Baris untuk memilih waktu mulai dan selesai
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = MaterialTheme.spacing.Large)
                ) {
                    // Kolom untuk waktu mulai
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.from)) // Label untuk waktu mulai
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))
                        Row(
                            modifier = Modifier
                                .clickable { showTimePickerDialog = DuraTimeType.START } // Menampilkan picker untuk waktu mulai
                                .border(
                                    color = MaterialTheme.colorScheme.primary,
                                    width = 1.dp,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(MaterialTheme.spacing.Small),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ikon jam dan teks untuk waktu mulai
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small))
                            Text(
                                text = stringResource(
                                    R.string.time_colon,
                                    durationTime?.startTime?.hour?.withZeroPadding() ?: "00",
                                    durationTime?.startTime?.minute?.withZeroPadding() ?: "00"
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.Large))
                    // Kolom untuk waktu selesai
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = stringResource(R.string.until)) // Label untuk waktu selesai
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))
                        Row(
                            modifier = Modifier
                                .clickable { showTimePickerDialog = DuraTimeType.END } // Menampilkan picker untuk waktu selesai
                                .border(
                                    color = MaterialTheme.colorScheme.primary,
                                    width = 1.dp,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(MaterialTheme.spacing.Small),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ikon jam dan teks untuk waktu selesai
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small))
                            Text(
                                text = stringResource(
                                    R.string.time_colon,
                                    durationTime?.endTime?.hour?.withZeroPadding() ?: "00",
                                    durationTime?.endTime?.minute?.withZeroPadding() ?: "00"
                                )
                            )
                        }
                    }
                }

                // Baris untuk tombol konfirmasi dan batal
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.Large),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Tombol batal
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    // Tombol konfirmasi
                    TextButton(
                        onClick = {
                            durationTime?.let { // Mengirimkan waktu durasi yang dipilih
                                onDurationSelected(it)
                            }
                            onDismissRequest().also { durationTime = initialDurationTime } // Menutup dialog dan reset state
                        }
                    ) {
                        Text(text = stringResource(id = R.string.confirm))
                    }
                }
            }
        }
    }

    // Menampilkan TimePicker jika diperlukan (untuk memilih waktu mulai atau selesai)
    if (showTimePickerDialog != null) {
        TimePicker(
            title = when (showTimePickerDialog!!) { // Menentukan judul berdasarkan tipe waktu
                DuraTimeType.START -> stringResource(R.string.available_from)
                DuraTimeType.END -> stringResource(R.string.until)
            },
            onDismissRequest = { showTimePickerDialog = null }, // Menutup TimePicker
            onTimeSelected = { time -> // Mengubah waktu yang dipilih
                durationTime = durationTime?.let {
                    when (showTimePickerDialog!!) {
                        DuraTimeType.START -> it.copy(startTime = time) // Mengubah waktu mulai
                        DuraTimeType.END -> it.copy(endTime = time) // Mengubah waktu selesai
                    }
                }
            }
        )
    }
}


enum class DuraTimeType { // Enum untuk mendefinisikan dua jenis waktu durasi
    START, // Waktu mulai
    END    // Waktu selesai
}

