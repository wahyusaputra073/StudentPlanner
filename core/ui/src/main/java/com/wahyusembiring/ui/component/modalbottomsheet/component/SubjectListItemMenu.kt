package com.wahyusembiring.ui.component.modalbottomsheet.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.ui.R

@Composable
fun SubjectListItemMenu(
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(), // Memungkinkan kustomisasi warna untuk ListItem
    subject: Subject, // Data subjek yang akan ditampilkan
    onClicked: ((subject: Subject) -> Unit)? = null, // Aksi opsional saat item subjek diklik
    onDeleteSubClick: () -> Unit, // Callback untuk aksi hapus
) {
    var expanded by remember { mutableStateOf(false) } // Menyimpan status visibilitas menu dropdown

    ListItem(
        colors = colors, // Menetapkan warna untuk ListItem
        modifier = modifier
            .then(
                if (onClicked != null) { // Jika ada handler klik, membuat item menjadi dapat diklik
                    Modifier.clickable { onClicked(subject) }
                } else {
                    Modifier
                }
            ),
        leadingContent = { // Konten yang ditampilkan di awal item (ikon subjek)
            Icon(
                painter = painterResource(id = R.drawable.ic_subjects), // Ikon untuk subjek
                contentDescription = subject.name, // Nama subjek sebagai deskripsi konten untuk aksesibilitas
                tint = subject.color // Menggunakan warna subjek untuk ikon
            )
        },
        headlineContent = { // Konten utama dari item (nama subjek)
            Text(text = subject.name)
        },
        trailingContent = { // Konten yang ditampilkan di akhir (tombol menu)
            IconButton(
                onClick = { expanded = true } // Menampilkan menu dropdown saat tombol diklik
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon menu
                    contentDescription = null // Tidak ada deskripsi konten untuk ikon ini
                )
            }

            // Menu dropdown untuk aksi (hapus)
            DropdownMenu(
                expanded = expanded, // Mengontrol visibilitas menu dropdown
                onDismissRequest = { expanded = false } // Menutup menu saat dibatalkan
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false // Menutup menu setelah aksi dipilih
                        onDeleteSubClick() // Memanggil aksi hapus
                    },
                    text = {
                        Text(text = stringResource(R.string.delete)) // Menampilkan teks "Delete" di menu
                    }
                )
            }
        }
    )
}

