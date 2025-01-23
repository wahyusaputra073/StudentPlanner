package com.wahyusembiring.thesisplanner.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.UIText

@Composable
internal fun Section(
    title: String, // Judul yang akan ditampilkan di bagian atas
    trailingContent: @Composable () -> Unit = {}, // Konten tambahan yang bisa disematkan di sebelah kanan judul, default kosong
    content: @Composable ColumnScope.() -> Unit // Konten utama yang ditampilkan dalam bentuk kolom
) {
    Column(
        modifier = Modifier // Menggunakan modifier untuk kolom utama
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth() // Membuat baris memenuhi lebar layar
                .padding(
                    horizontal = MaterialTheme.spacing.Medium, // Padding horizontal medium
                    vertical = MaterialTheme.spacing.Small // Padding vertikal kecil
                ),
            horizontalArrangement = Arrangement.SpaceBetween, // Memberi jarak antar elemen horizontal
            verticalAlignment = Alignment.CenterVertically // Menyelaraskan elemen secara vertikal di tengah
        ) {
            Text(
                text = title, // Menampilkan judul dengan teks
                style = MaterialTheme.typography.titleLarge // Gaya teks untuk judul
            )
            Box(
                contentAlignment = Alignment.Center // Menyelaraskan konten tambahan di tengah box
            ) {
                trailingContent() // Menampilkan konten tambahan (jika ada)
            }
        }
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.Medium), // Padding kolom konten utama
            content = content // Menampilkan konten utama
        )
    }
}