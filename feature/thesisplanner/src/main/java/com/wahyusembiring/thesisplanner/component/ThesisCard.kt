package com.wahyusembiring.thesisplanner.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wahyusembiring.thesisplanner.R
import com.wahyusembiring.thesisplanner.screen.thesisselection.Thesis
import com.wahyusembiring.ui.theme.spacing

@Composable
internal fun ThesisCard(
    modifier: Modifier = Modifier, // Modifier opsional untuk mengubah tampilan card
    thesis: Thesis, // Data tesis yang akan ditampilkan
    onClick: () -> Unit, // Fungsi callback saat card diklik
    onDeleteClick: () -> Unit // Fungsi callback saat tombol hapus diklik
) {
    Card(
        modifier = modifier, // Menetapkan modifier pada card
        onClick = onClick // Menangani klik pada card
    ) {
        Header(onDeleteClick) // Menampilkan header dengan tombol hapus
        Body(
            title = thesis.thesis.title, // Judul tesis
            finishedTasks = Pair( // Menampilkan jumlah task selesai dan total task
                thesis.taskTheses.filter { it.isCompleted }.size, // Jumlah task yang selesai
                thesis.taskTheses.size // Total jumlah task
            ),
            articles = thesis.thesis.articles.size // Jumlah artikel terkait tesis
        )
    }
}


@Composable
private fun Header(
    onDeleteClick: () -> Unit // Fungsi callback untuk menghapus tesis
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Mengisi lebar layar
            .padding(MaterialTheme.spacing.Medium), // Padding medium
        verticalAlignment = Alignment.CenterVertically, // Menyelaraskan secara vertikal di tengah
        horizontalArrangement = Arrangement.SpaceBetween // Memberi jarak antar elemen secara horizontal
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Menyelaraskan konten secara vertikal di tengah
            horizontalArrangement = Arrangement.Start // Menyusun elemen di kiri
        ) {
            Box(
                modifier = Modifier
                    .background( // Memberi background berbentuk bulat pada ikon
                        color = MaterialTheme.colorScheme.primary, // Warna background utama
                        shape = RoundedCornerShape(50) // Membuat sudut membulat
                    ),
                contentAlignment = Alignment.Center // Menyelaraskan ikon di tengah box
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp) // Ukuran ikon
                        .padding(8.dp), // Padding ikon
                    painter = painterResource(id = R.drawable.ic_pen), // Ikon pensil
                    contentDescription = stringResource(R.string.thesis_icon), // Deskripsi ikon untuk aksesibilitas
                    tint = MaterialTheme.colorScheme.onPrimary // Warna ikon sesuai dengan tema
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.Small)) // Spacer untuk memberi jarak antara elemen
            Text(
                text = stringResource(R.string.thesis), // Menampilkan teks "Thesis"
                style = MaterialTheme.typography.titleMedium, // Gaya teks untuk judul
                color = MaterialTheme.colorScheme.primary, // Warna teks sesuai tema
            )
        }
        Column(
            verticalArrangement = Arrangement.Center // Menyusun elemen di tengah secara vertikal
        ) {
            var optionExpanded by remember { mutableStateOf(false) } // State untuk mengatur dropdown menu

            IconButton(
                onClick = { optionExpanded = true } // Menampilkan dropdown menu saat ikon diklik
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more), // Ikon menu lebih
                    contentDescription = stringResource(R.string.more_options) // Deskripsi aksesibilitas
                )
            }
            DropdownMenu(
                expanded = optionExpanded, // Status dropdown menu
                onDismissRequest = { optionExpanded = false } // Menutup dropdown saat area luar diklik
            ) {
                DropdownMenuItem(
                    leadingIcon = { // Ikon hapus tesis di menu dropdown
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(R.string.delete_thesis), // Deskripsi untuk hapus tesis
                            tint = MaterialTheme.colorScheme.error // Warna merah untuk ikon hapus
                        )
                    },
                    text = { // Teks "Hapus" di menu dropdown
                        Text(text = stringResource(id = R.string.delete))
                    },
                    onClick = {
                        optionExpanded = false // Menutup dropdown menu
                        onDeleteClick() // Menjalankan fungsi hapus tesis
                    }
                )
            }
        }
    }
}


@Composable
private fun Body(
    title: String, // Judul tesis yang akan ditampilkan
    finishedTasks: Pair<Int, Int>, // Pasangan nilai (selesai, total) untuk task
    articles: Int // Jumlah artikel terkait tesis
) {
    Column(
        modifier = Modifier.padding(
            horizontal = MaterialTheme.spacing.Medium, // Padding horizontal medium
        )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(), // Mengisi lebar penuh
            text = title, // Menampilkan judul tesis
            color = MaterialTheme.colorScheme.secondary, // Warna teks sesuai tema
            style = MaterialTheme.typography.titleMedium, // Gaya teks untuk judul
            textAlign = TextAlign.Center // Menyelaraskan teks di tengah
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Spacer untuk memberi jarak vertikal
        Text(
            text = stringResource(
                R.string.tasks_finished, // Menampilkan jumlah task yang selesai dan total task
                finishedTasks.first, // Jumlah task yang selesai
                finishedTasks.second // Jumlah total task
            ),
            style = MaterialTheme.typography.bodySmall // Gaya teks untuk informasi task
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small)) // Spacer untuk memberi jarak vertikal
        Text(
            text = stringResource(R.string.articles_count, articles), // Menampilkan jumlah artikel
            style = MaterialTheme.typography.bodySmall // Gaya teks untuk informasi artikel
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Spacer untuk memberi jarak vertikal
    }
}