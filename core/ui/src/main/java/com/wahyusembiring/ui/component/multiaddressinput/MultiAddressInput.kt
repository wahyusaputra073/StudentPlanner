package com.wahyusembiring.ui.component.multiaddressinput

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing


@Composable
fun MultiAddressInput(
    modifier: Modifier = Modifier,
    addresses: List<String>, // Daftar alamat yang akan ditampilkan
    onNewAddress: (String) -> Unit, // Fungsi untuk menambah alamat baru
    onDeleteAddress: (String) -> Unit // Fungsi untuk menghapus alamat yang ada
) {
    var showAddressDialog by remember { mutableStateOf(false) } // Menyimpan status untuk menampilkan dialog input alamat

    // Layout utama untuk komponen alamat
    Column(
        modifier = modifier
    ) {
        // Baris untuk judul "Address" dan tombol untuk menambahkan alamat baru
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.Medium),
                text = stringResource(R.string.address), // Teks untuk judul "Address"
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            // Tombol untuk membuka dialog input alamat baru
            IconButton(
                onClick = { showAddressDialog = true } // Menampilkan dialog input alamat baru
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), // Ikon untuk menambah alamat
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        HorizontalDivider() // Pembatas horizontal antara bagian judul dan daftar alamat
        // Kolom untuk menampilkan alamat yang ada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MaterialTheme.spacing.Large)
        ) {
            // Jika tidak ada alamat, tampilkan pesan "Empty"
            if (addresses.isEmpty()) {
                EmptyAddress() // Menampilkan komponen EmptyAddress ketika alamat kosong
            }
            // Iterasi untuk menampilkan alamat yang ada dalam daftar
            for (address in addresses) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small)) // Spacer untuk jarak antar item
                AddressListItem(
                    address = address, // Menampilkan alamat
                    onDeleteClick = { onDeleteAddress(address) } // Menangani aksi hapus alamat
                )
            }
        }
    }

    // Menampilkan dialog input alamat ketika showAddressDialog bernilai true
    if (showAddressDialog) {
        AddressInputDialog(
            onDismissRequest = { showAddressDialog = false }, // Menutup dialog saat dismissed
            onAddressAddClick = onNewAddress // Menambahkan alamat baru saat tombol add diklik
        )
    }
}


@Composable
private fun AddressListItem(
    address: String, // Alamat yang akan ditampilkan dalam item
    onDeleteClick: () -> Unit // Callback untuk menghapus alamat saat dipilih
) {
    var expanded by remember { mutableStateOf(false) } // Menyimpan status apakah dropdown menu terbuka

    // Baris utama untuk item alamat
    Row(
        modifier = Modifier
            .fillMaxWidth() // Lebar penuh
            .height(IntrinsicSize.Min), // Tinggi sesuai isi konten
        horizontalArrangement = Arrangement.Start, // Mengatur konten ke kiri
        verticalAlignment = Alignment.CenterVertically // Menyelaraskan konten secara vertikal
    ) {
        // Ikon untuk alamat
        Icon(
            painter = painterResource(id = R.drawable.ic_address), // Ikon alamat
            tint = MaterialTheme.colorScheme.secondary, // Menggunakan warna sekunder dari tema
            contentDescription = null // Tidak diperlukan deskripsi karena sudah jelas
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Memberikan jarak antar ikon dan teks

        // Pembatas vertikal antara ikon dan teks
        VerticalDivider(
            modifier = Modifier.fillMaxHeight() // Pembatas vertikal yang mengisi tinggi
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Memberikan jarak antar pembatas dan teks

        // Menampilkan alamat sebagai teks
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Start, // Teks sejajar ke kiri
            text = address, // Alamat yang diteruskan sebagai teks
            maxLines = 1, // Maksimal satu baris
            overflow = TextOverflow.Ellipsis // Teks yang terlalu panjang akan dipotong dengan elipsis
        )

        Box { // Untuk menempatkan tombol dropdown di akhir item
            // Tombol untuk membuka dropdown menu
            IconButton(
                onClick = { expanded = true }, // Menampilkan dropdown menu saat tombol diklik
                modifier = Modifier.size(24.dp) // Ukuran ikon titik tiga
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon menu titik tiga
                    contentDescription = "Options", // Deskripsi konten
                    tint = MaterialTheme.colorScheme.secondary // Menggunakan warna yang sama dengan ikon alamat
                )
            }

            // Dropdown menu untuk opsi lebih lanjut (misalnya, delete)
            DropdownMenu(
                expanded = expanded, // Menampilkan atau menyembunyikan dropdown menu berdasarkan state
                onDismissRequest = { expanded = false } // Menutup menu jika area lain di luar menu diklik
            ) {
                // Item menu untuk menghapus alamat
                DropdownMenuItem(
                    text = { Text("Delete") }, // Teks yang tampil di menu
                    onClick = {
                        onDeleteClick() // Menjalankan aksi hapus alamat
                        expanded = false // Menutup menu setelah klik
                    }
                )
            }
        }
    }
}


@Composable
private fun EmptyAddress() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Menyesuaikan lebar box dengan lebar layar
        contentAlignment = Alignment.Center // Menempatkan teks di tengah-tengah box
    ) {
        // Teks yang memberi informasi bahwa tidak ada alamat
        Text(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.Medium, // Padding horizontal
                vertical = MaterialTheme.spacing.Small // Padding vertikal
            ),
            text = stringResource(R.string.no_address), // Mengambil teks dari string resource
            style = MaterialTheme.typography.bodyMedium.copy(
                fontStyle = FontStyle.Italic // Menambahkan gaya huruf miring pada teks
            )
        )
    }
}


@Composable
private fun AddressInputDialog(
    onDismissRequest: () -> Unit, // Fungsi yang dipanggil saat dialog ditutup
    onAddressAddClick: (String) -> Unit // Fungsi yang dipanggil saat alamat ditambahkan
) {
    var address by remember { mutableStateOf("") } // State untuk menyimpan alamat yang dimasukkan

    // Membuat dialog dengan AlertDialog
    AlertDialog(
        onDismissRequest = onDismissRequest, // Menangani dismiss dialog
        title = {
            Text(text = stringResource(R.string.enter_address)) // Judul dialog
        },
        text = {
            // TextField untuk memasukkan alamat
            OutlinedTextField(
                value = address, // Nilai input teks
                onValueChange = { address = it }, // Mengubah nilai alamat saat user mengetik
                label = { Text(text = stringResource(R.string.address)) }, // Label untuk input
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text // Menentukan jenis keyboard (teks)
                )
            )
        },
        confirmButton = {
            // Tombol konfirmasi untuk menambah alamat
            TextButton(
                onClick = {
                    onAddressAddClick(address) // Menambahkan alamat
                    onDismissRequest().also { address = "" } // Menutup dialog dan reset alamat
                }
            ) {
                Text(text = stringResource(R.string.add)) // Teks pada tombol konfirmasi
            }
        },
        dismissButton = {
            // Tombol pembatalan untuk menutup dialog
            TextButton(
                onClick = { onDismissRequest() } // Menutup dialog tanpa menambahkan alamat
            ) {
                Text(text = stringResource(R.string.cancel)) // Teks pada tombol pembatalan
            }
        }
    )
}