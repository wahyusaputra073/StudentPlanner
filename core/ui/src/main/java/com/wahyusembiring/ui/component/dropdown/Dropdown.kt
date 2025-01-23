package com.wahyusembiring.ui.component.dropdown

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.UIText

// Fungsi Composable untuk menampilkan dropdown list dengan item yang dapat dipilih
@Composable
fun <T : Any> Dropdown(
    modifier: Modifier = Modifier,               // Modifier untuk menyesuaikan tampilan
    items: List<T>,                              // Daftar item yang akan ditampilkan di dropdown
    selected: T?,                                // Item yang dipilih
    icons: ((item: T) -> Painter)? = null,       // Fungsi opsional untuk mendapatkan ikon untuk setiap item
    title: (item: T?) -> UIText,                 // Fungsi untuk menghasilkan judul teks untuk item
    onItemClick: (item: T) -> Unit,              // Fungsi untuk menangani item yang dipilih
    emptyContent: @Composable ColumnScope.() -> Unit // Konten yang ditampilkan jika daftar item kosong
) {
    var expanded by remember { mutableStateOf(false) }  // Status untuk mengontrol apakah dropdown terbuka atau tertutup

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,                             // Border dengan ketebalan 1dp
                    color = MaterialTheme.colorScheme.primary, // Warna border sesuai tema
                    shape = MaterialTheme.shapes.small        // Bentuk border sesuai tema
                ),
            verticalAlignment = Alignment.CenterVertically,    // Vertikal center untuk isi row
            horizontalArrangement = Arrangement.SpaceBetween   // Space antara teks dan ikon
        ) {
            // Menampilkan teks judul item yang dipilih
            Text(
                modifier = Modifier
                    .weight(1f) // Membuat teks mengambil ruang yang tersisa
                    .padding(
                        horizontal = MaterialTheme.spacing.Medium,  // Padding horizontal
                        vertical = MaterialTheme.spacing.Small      // Padding vertikal
                    ),
                text = title(selected).asString()  // Menggunakan fungsi title untuk menampilkan nama item
            )
            // Menampilkan ikon dropdown untuk membuka/menutup dropdown menu
            IconButton(
                onClick = { expanded = !expanded }  // Toggle status expanded
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dropdown),  // Ikon dropdown
                    contentDescription = null
                )
            }
        }
        // Menampilkan DropdownMenu jika expanded = true
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth() // Mengisi lebar penuh
                .padding(horizontal = MaterialTheme.spacing.Medium), // Padding horizontal
            expanded = expanded,             // Menentukan apakah dropdown terbuka
            onDismissRequest = { expanded = false } // Menutup dropdown saat klik di luar
        ) {
            if (items.isEmpty()) emptyContent()  // Jika item kosong, tampilkan konten kosong
            for (item in items) {
                DropdownMenuItem(
                    leadingIcon = icons?.let {
                        { Icon(painter = it(item), contentDescription = null) }  // Menampilkan ikon untuk item jika ada
                    },
                    text = {
                        // Menampilkan teks item dengan warna yang menonjol jika dipilih
                        Text(
                            text = title(item).asString(),
                            color = if (item == selected) {
                                MaterialTheme.colorScheme.primary  // Menyorot warna teks jika item dipilih
                            } else {
                                MaterialTheme.colorScheme.onSurface // Warna default jika tidak dipilih
                            }
                        )
                    },
                    onClick = {
                        onItemClick(item)  // Memanggil fungsi saat item dipilih
                        expanded = false    // Menutup dropdown setelah item dipilih
                    }
                )
            }
        }
    }
}