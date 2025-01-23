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
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.ui.R

// Fungsi Composable untuk menampilkan tombol tambah mata pelajaran dengan ikon dan teks
@Composable
fun AddSubjectButton(
    modifier: Modifier = Modifier,  // Modifier opsional untuk menyesuaikan tampilan
    subject: Subject?,             // Mata pelajaran yang akan ditampilkan, bisa null
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
                painter = painterResource(id = R.drawable.ic_subjects), // Ikon mata pelajaran
                contentDescription = stringResource(R.string.add_subject), // Deskripsi konten untuk aksesibilitas
                tint = MaterialTheme.colorScheme.primary // Pewarnaan sesuai tema
            )
        },
        headlineContent = {
            if (subject != null) {
                // Menampilkan nama mata pelajaran jika ada
                Column {
                    Text(text = subject.name) // Nama mata pelajaran
                }
            } else {
                // Menampilkan teks jika mata pelajaran null
                Text(
                    color = TextFieldDefaults.colors().disabledTextColor, // Warna teks disabled
                    text = stringResource(R.string.add_subject),           // Teks "Tambah Mata Pelajaran"
                )
            }
        }
    )
}