package com.wahyusembiring.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wahyusembiring.datetime.Moment
import com.wahyusembiring.datetime.formatter.FormattingStyle
import com.wahyusembiring.ui.R
import java.util.Date


// Fungsi Composable untuk menampilkan tombol tambah tanggal dengan ikon dan teks
@Composable
fun AddDateButton(
    modifier: Modifier = Modifier, // Modifier opsional untuk menyesuaikan tampilan
    date: Date?,                  // Tanggal yang akan ditampilkan, bisa null
    onClicked: (() -> Unit)? = null, // Lambda fungsi opsional untuk menangani klik
) {
    ListItem(
        modifier = modifier
            .then(
                if (onClicked != null) {
                    // Menambahkan Modifier.clickable jika onClicked tidak null
                    Modifier.clickable { onClicked() }
                } else {
                    Modifier // Jika onClicked null, tidak ada interaksi klik
                }
            ),
        leadingContent = {
            // Menampilkan ikon di sisi kiri
            Icon(
                painter = painterResource(id = R.drawable.ic_date_picker), // Ikon pemilih tanggal
                contentDescription = stringResource(R.string.pick_date),    // Deskripsi konten untuk aksesibilitas
                tint = MaterialTheme.colorScheme.primary // Pewarnaan sesuai tema
            )
        },
        headlineContent = {
            if (date != null) {
                // Menampilkan tanggal jika tidak null
                Column {
                    Text(
                        text = Moment.fromEpochMilliseconds(date.time) // Mengubah timestamp menjadi format tanggal
                            .toString(FormattingStyle.INDO_FULL)        // Format sesuai dengan gaya tertentu
                    )
                }
            } else {
                // Menampilkan teks jika tanggal null
                Text(
                    color = TextFieldDefaults.colors().disabledTextColor, // Warna teks disabled
                    text = stringResource(R.string.add_date),            // Teks "Tambah Tanggal"
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
private fun AddDatePreview() {
    MaterialTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val date = Date(System.currentTimeMillis())
                AddDateButton(date = date)
            }
        }
    }
}