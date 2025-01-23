package com.wahyusembiring.ui.component.officehourinput

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.common.util.withZeroPadding
import com.wahyusembiring.data.model.SpanTime
import com.wahyusembiring.ui.R



@Composable
fun DurationInput(
    modifier: Modifier = Modifier, // Modifier untuk penyesuaian tata letak
    durationTime: SpanTime?, // Parameter untuk waktu durasi, bisa null
    onClicked: (() -> Unit)? = null, // Fungsi yang dijalankan saat item diklik, bisa null
) {
    ListItem(
        modifier = modifier
            .then(
                if (onClicked != null) { // Jika ada aksi onClicked, tambahkan modifier clickable
                    Modifier.clickable { onClicked() }
                } else { // Jika tidak, tetap menggunakan modifier default
                    Modifier
                }
            ),
        leadingContent = {
            Icon( // Menampilkan ikon di awal item
                painter = painterResource(id = R.drawable.ic_date_picker), // Ikon pemilih tanggal
                contentDescription = stringResource(R.string.pick_date), // Deskripsi ikon
                tint = MaterialTheme.colorScheme.primary // Mengatur warna ikon sesuai tema
            )
        },
        headlineContent = {
            if (durationTime != null) { // Jika durationTime ada
                Column {
                    Text(
                        text = stringResource(
                            R.string.event_from_to, // Format teks untuk waktu mulai dan selesai
                            durationTime.startTime.hour.withZeroPadding(), // Jam mulai dengan padding 0 jika perlu
                            durationTime.startTime.minute.withZeroPadding(), // Menit mulai dengan padding 0 jika perlu
                            durationTime.endTime.hour.withZeroPadding(), // Jam selesai dengan padding 0 jika perlu
                            durationTime.endTime.minute.withZeroPadding(), // Menit selesai dengan padding 0 jika perlu
                        )
                    )
                }
            } else { // Jika durationTime null, tampilkan teks untuk menambahkan waktu akhir
                Text(
                    color = TextFieldDefaults.colors().disabledTextColor, // Teks dalam warna disabled
                    text = stringResource(id = R.string.add_end_time), // Teks untuk menambahkan waktu akhir
                )
            }
        }
    )
}