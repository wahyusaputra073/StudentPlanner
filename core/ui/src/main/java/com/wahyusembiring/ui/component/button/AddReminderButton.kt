package com.wahyusembiring.ui.component.button

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
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.ui.R

// Fungsi Composable untuk menampilkan tombol tambah pengingat dengan ikon dan teks
@Composable
fun AddReminderButton(
    modifier: Modifier = Modifier,  // Modifier opsional untuk menyesuaikan tampilan
    time: Time?,                   // Waktu pengingat yang akan ditampilkan, bisa null
    onClicked: (() -> Unit)? = null, // Lambda fungsi opsional untuk menangani klik
    permissionCheck: (() -> Unit)? = null // Lambda opsional untuk memeriksa izin sebelum klik
) {
    ListItem(
        modifier = modifier
            .then(
                if (onClicked != null) {
                    // Menambahkan Modifier.clickable jika onClicked tidak null
                    Modifier.clickable {
                        // Jika ada fungsi permissionCheck, jalankan; jika tidak, jalankan onClicked
                        permissionCheck?.invoke() ?: onClicked()
                    }
                } else {
                    Modifier // Jika onClicked null, tidak ada interaksi klik
                }
            ),
        leadingContent = {
            // Menampilkan ikon di sisi kiri
            Icon(
                painter = painterResource(id = R.drawable.ic_reminder), // Ikon pengingat
                contentDescription = stringResource(R.string.add_reminder), // Deskripsi konten untuk aksesibilitas
                tint = MaterialTheme.colorScheme.primary // Pewarnaan sesuai tema
            )
        },
        headlineContent = {
            if (time != null) {
                // Menampilkan waktu pengingat jika ada
                Column {
                    Text(
                        text = "${stringResource(R.string.remind_me_at)} ${time.hour.withZeroPadding()}:${time.minute.withZeroPadding()}"
                        // Menampilkan waktu dengan format jam dan menit
                    )
                }
            } else {
                // Menampilkan teks jika waktu null
                Text(
                    color = TextFieldDefaults.colors().disabledTextColor, // Warna teks disabled
                    text = stringResource(id = R.string.add_reminder),    // Teks "Tambah Pengingat"
                )
            }
        }
    )
}
