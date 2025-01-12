package com.wahyusembiring.ui.component.websiteinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing


@Composable
fun WebsiteInput( // Fungsi composable untuk mengelola input dan daftar website
    modifier: Modifier = Modifier, // Modifier untuk menyesuaikan tampilan
    websites: List<String>, // Daftar website yang ditampilkan
    onNewWebsiteAddClick: (String) -> Unit, // Callback saat menambahkan website baru
    onDeleteWebsite: (String) -> Unit // Callback saat menghapus website
) {
    var showWebsiteDialog by remember { mutableStateOf(false) } // State untuk menampilkan dialog input website

    Column(
        modifier = modifier // Tampilan utama dalam bentuk kolom
    ) {
        Row( // Baris untuk header input website
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Mengatur jarak antar elemen secara horizontal
            verticalAlignment = Alignment.CenterVertically // Menyatukan elemen secara vertikal
        ) {
            Text( // Judul "Website"
                modifier = Modifier.padding(start = MaterialTheme.spacing.Medium),
                text = stringResource(R.string.website),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                onClick = { showWebsiteDialog = true } // Membuka dialog input website saat diklik
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), // Ikon tambah
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        HorizontalDivider() // Garis pembatas horizontal
        Column(
            modifier = Modifier.padding(start = MaterialTheme.spacing.Large) // Kolom untuk daftar website
        ) {
            if (websites.isEmpty()) { // Menampilkan pesan kosong jika daftar website kosong
                EmptyWebsite()
            }
            for (website in websites) { // Iterasi daftar website
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small)) // Spasi antar item
                WebsiteListItem(
                    website = website, // Menampilkan item website
                    onDeleteClick = { onDeleteWebsite(website) } // Callback saat item dihapus
                )
            }
        }
    }

    if (showWebsiteDialog) { // Menampilkan dialog input website jika state aktif
        WebsiteInputDialog(
            onDismissRequest = { showWebsiteDialog = false }, // Menutup dialog
            onNewWebsiteAddClick = onNewWebsiteAddClick // Callback untuk menambahkan website baru
        )
    }
}


@Composable
private fun WebsiteListItem( // Fungsi composable untuk menampilkan item website dalam daftar
    website: String, // Website yang akan ditampilkan
    onDeleteClick: () -> Unit // Callback saat website dihapus
) {
    var expanded by remember { mutableStateOf(false) } // State untuk mengatur tampilan menu dropdown

    Row(
        modifier = Modifier.requiredHeight(IntrinsicSize.Min), // Mengatur tinggi elemen berdasarkan konten
        horizontalArrangement = Arrangement.Start, // Mengatur elemen ke kiri
        verticalAlignment = Alignment.CenterVertically // Menyatukan elemen secara vertikal
    ) {
        Icon( // Ikon link internet
            painter = painterResource(id = R.drawable.ic_link_internet),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Spasi setelah ikon
        VerticalDivider(
            modifier = Modifier.fillMaxHeight() // Garis vertikal sepanjang elemen
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Spasi sebelum teks
        Text(
            text = website // Menampilkan teks nama website
        )

        Box { // Container untuk ikon opsi dan menu dropdown
            IconButton(
                onClick = { expanded = true }, // Membuka menu dropdown saat diklik
                modifier = Modifier.size(24.dp) // Ukuran ikon opsi
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon titik tiga
                    contentDescription = "Options",
                    tint = MaterialTheme.colorScheme.secondary // Warna ikon
                )
            }
            DropdownMenu( // Menu dropdown untuk opsi tambahan
                expanded = expanded, // Menentukan apakah menu terbuka
                onDismissRequest = { expanded = false } // Menutup menu jika klik di luar
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") }, // Opsi "Delete"
                    onClick = {
                        onDeleteClick() // Memanggil callback untuk menghapus item
                        expanded = false // Menutup menu setelah klik
                    }
                )
            }
        }
    }
}


@Composable
private fun EmptyWebsite() {
    Box( // Membuat kontainer untuk menampilkan teks di tengah layar
        modifier = Modifier.fillMaxWidth(), // Mengisi lebar penuh layar
        contentAlignment = Alignment.Center // Memposisikan konten di tengah secara vertikal dan horizontal
    ) {
        Text( // Menampilkan teks yang menunjukkan tidak ada website
            modifier = Modifier.padding( // Memberikan jarak di sekitar teks
                horizontal = MaterialTheme.spacing.Medium, // Padding horizontal
                vertical = MaterialTheme.spacing.Small // Padding vertikal
            ),
            text = stringResource(R.string.no_website), // Teks diambil dari resource string
            style = MaterialTheme.typography.bodyMedium.copy( // Mengatur gaya teks menjadi bodyMedium dengan huruf miring
                fontStyle = FontStyle.Italic
            )
        )
    }
}

@Composable
private fun WebsiteInputDialog(
    onDismissRequest: () -> Unit, // Callback untuk menutup dialog
    onNewWebsiteAddClick: (String) -> Unit // Callback untuk menangani penambahan website baru
) {
    var website by remember { mutableStateOf("") } // State untuk menyimpan input teks website

    AlertDialog( // Komponen dialog untuk meminta input dari pengguna
        onDismissRequest = onDismissRequest, // Menutup dialog saat pengguna mencoba membatalkan
        title = {
            Text(text = stringResource(R.string.enter_website_link)) // Menampilkan judul dialog
        },
        text = {
            OutlinedTextField( // Field input dengan outline untuk memasukkan URL
                value = website, // Mengikat nilai input ke state
                onValueChange = { website = it }, // Memperbarui state saat input berubah
                label = { Text(text = stringResource(R.string.website)) }, // Label untuk field input
                maxLines = 1, // Membatasi input menjadi satu baris
                singleLine = true, // Memastikan input hanya pada satu baris
                keyboardOptions = KeyboardOptions( // Mengatur keyboard untuk input tipe URL
                    keyboardType = KeyboardType.Uri
                )
            )
        },
        confirmButton = {
            TextButton( // Tombol untuk mengonfirmasi input
                onClick = {
                    onNewWebsiteAddClick(website) // Memanggil callback untuk menambah website
                    onDismissRequest().also { website = "" } // Menutup dialog dan mereset input
                }
            ) {
                Text(text = stringResource(R.string.add)) // Label tombol konfirmasi
            }
        },
        dismissButton = {
            TextButton( // Tombol untuk membatalkan dialog
                onClick = { onDismissRequest() } // Memanggil callback untuk menutup dialog
            ) {
                Text(text = stringResource(R.string.cancel)) // Label tombol batal
            }
        }
    )
}