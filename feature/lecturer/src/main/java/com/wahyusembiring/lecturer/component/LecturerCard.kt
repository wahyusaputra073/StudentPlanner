package com.wahyusembiring.lecturer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.wahyusembiring.data.model.LecturerWithSubject
import com.wahyusembiring.lecture.R
import com.wahyusembiring.ui.theme.spacing

@Composable
fun LecturerCard(
    lecturerWithSubjects: LecturerWithSubject, // Data untuk menampilkan informasi dosen dan mata kuliah
    onClick: () -> Unit, // Aksi ketika card diklik
    onDeleteClick: () -> Unit, // Aksi ketika delete diklik
    modifier: Modifier = Modifier // Modifier opsional untuk kustomisasi
) {
    var expanded by remember { mutableStateOf(false) } // State untuk menampilkan DropdownMenu

    // ListItem untuk menampilkan card dosen
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick) // Mengatur aksi klik pada card
            .then(modifier), // Menambahkan modifier eksternal
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(32.dp) // Ukuran gambar
                    .clip(RoundedCornerShape(50)) // Bentuk gambar bulat
                    .background(MaterialTheme.colorScheme.primary), // Background bulat
                contentAlignment = Alignment.Center
            ) {
                // Menampilkan foto dosen atau ikon default jika tidak ada foto
                if (lecturerWithSubjects.lecturer.photo == null) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize() // Ukuran ikon menyesuaikan
                            .padding(MaterialTheme.spacing.ExtraSmall), // Padding ekstra kecil
                        painter = painterResource(id = R.drawable.ic_person), // Ikon default
                        tint = MaterialTheme.colorScheme.onPrimary, // Warna ikon
                        contentDescription = null
                    )
                } else {
                    // Menampilkan foto dosen dengan gambar asinkron
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = lecturerWithSubjects.lecturer.photo, // Foto dosen
                        contentScale = ContentScale.Crop, // Menjaga proporsi gambar
                        contentDescription = null
                    )
                }
            }
        },
        headlineContent = {
            // Menampilkan nama dosen
            Text(text = lecturerWithSubjects.lecturer.name)
        },
        supportingContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = (lecturerWithSubjects.primarySubjects + lecturerWithSubjects.secondarySubjects)
                    .joinToString { it.name }
                    .ifBlank {
                        stringResource(R.string.no_subject_added)
                    }
            )
        },
        trailingContent = {
            // Tombol untuk membuka menu dropdown
            IconButton(
                onClick = { expanded = true }
            ) {
                Icon(
                    painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_more_vertical), // Ikon menu lebih
                    contentDescription = null
                )
            }

            // DropdownMenu untuk aksi tambahan
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false } // Menutup dropdown saat di luar menu diklik
            ) {
                // Item dalam menu dropdown untuk aksi delete
                DropdownMenuItem(
                    onClick = {
                        expanded = false // Menutup menu dropdown setelah klik
                        onDeleteClick() // Memanggil aksi delete
                    },
                    text = {
                        Text(text = stringResource(R.string.delete_lecture)) // Menampilkan teks "Delete"
                    }
                )
            }
        }
    )
}