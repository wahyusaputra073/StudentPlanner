package com.wahyusembiring.ui.component.emailinput


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
fun EmailInput(
    modifier: Modifier = Modifier,
    emails: List<String>,                        // Daftar email yang akan ditampilkan
    onNewEmail: (String) -> Unit,                // Fungsi untuk menambahkan email baru
    onDeleteEmail: (String) -> Unit              // Fungsi untuk menghapus email
) {
    var showEmailDialog by remember { mutableStateOf(false) }  // Status untuk menampilkan dialog email

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,    // Menata elemen secara horizontal
            verticalAlignment = Alignment.CenterVertically        // Menyusun elemen secara vertikal di tengah
        ) {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.Medium),
                text = stringResource(R.string.email),  // Menampilkan teks "Email"
                color = MaterialTheme.colorScheme.primary,  // Menggunakan warna utama tema
                style = MaterialTheme.typography.titleMedium // Menggunakan gaya teks untuk judul
            )
            // Tombol untuk menambahkan email baru
            IconButton(
                onClick = { showEmailDialog = true } // Menampilkan dialog untuk menambahkan email
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), // Ikon untuk menambah email
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary // Menggunakan warna utama tema
                )
            }
        }
        HorizontalDivider()  // Membuat pembatas horizontal antara judul dan daftar email
        Column(
            modifier = Modifier.padding(start = MaterialTheme.spacing.Large) // Padding untuk daftar email
        ) {
            if (emails.isEmpty()) {
                EmptyEmail()  // Menampilkan pesan jika daftar email kosong
            }
            // Menampilkan setiap email dalam daftar
            for (email in emails) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small)) // Menambahkan ruang di antara item
                EmailListItem(
                    email = email,                        // Menampilkan email
                    onDeleteClick = { onDeleteEmail(email) } // Menangani aksi hapus email
                )
            }
        }
    }

    // Menampilkan dialog untuk menambahkan email baru
    if (showEmailDialog) {
        EmailInputDialog(
            onDismissRequest = { showEmailDialog = false },  // Menutup dialog
            onEmailAddClick = onNewEmail // Menambahkan email baru
        )
    }
}


@Composable
private fun EmailListItem(
    email: String,                  // Email yang akan ditampilkan
    onDeleteClick: () -> Unit       // Fungsi yang dipanggil ketika tombol hapus diklik
) {
    var expanded by remember { mutableStateOf(false) }  // Status untuk menampilkan dropdown menu

    Row(
        modifier = Modifier.requiredHeight(IntrinsicSize.Min), // Menetapkan tinggi berdasarkan konten
        horizontalArrangement = Arrangement.Start,             // Menata elemen secara horizontal di kiri
        verticalAlignment = Alignment.CenterVertically        // Menyusun elemen secara vertikal di tengah
    ) {
        // Menampilkan ikon email
        Icon(
            painter = painterResource(id = R.drawable.ic_email),
            tint = MaterialTheme.colorScheme.secondary,  // Menggunakan warna sekunder tema
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium))  // Memberikan ruang antar elemen

        // Membuat pembatas vertikal
        VerticalDivider(
            modifier = Modifier.fillMaxHeight() // Membuat pembatas vertikal mengisi seluruh tinggi
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium))  // Memberikan ruang antar elemen

        // Menampilkan alamat email
        Text(
            text = email
        )

        Box {
            // Menampilkan ikon titik tiga untuk membuka dropdown menu
            IconButton(
                onClick = { expanded = true }, // Menampilkan dropdown menu
                modifier = Modifier.size(24.dp) // Ukuran ikon titik tiga
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical),  // Ikon titik tiga vertikal
                    contentDescription = "Options",  // Deskripsi untuk aksesibilitas
                    tint = MaterialTheme.colorScheme.secondary // Menggunakan warna sekunder tema
                )
            }

            // Menampilkan dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }  // Menutup menu jika area luar diklik
            ) {
                // Menu pilihan untuk menghapus email
                DropdownMenuItem(
                    text = { Text("Delete") },  // Teks menu item
                    onClick = {
                        onDeleteClick() // Menjalankan fungsi hapus email
                        expanded = false // Menutup dropdown setelah klik
                    }
                )
            }
        }
    }
}


@Composable
private fun EmptyEmail() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Membuat Box mengisi seluruh lebar layar
        contentAlignment = Alignment.Center // Menyusun konten di tengah Box
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.Medium, // Menambahkan padding horizontal
                vertical = MaterialTheme.spacing.Small // Menambahkan padding vertikal
            ),
            text = stringResource(R.string.no_email), // Menampilkan teks yang memberi tahu bahwa tidak ada email
            style = MaterialTheme.typography.bodyMedium.copy(
                fontStyle = FontStyle.Italic // Menggunakan gaya font miring (italic)
            )
        )
    }
}


@Composable
private fun EmailInputDialog(
    onDismissRequest: () -> Unit,  // Fungsi yang dipanggil ketika dialog dibatalkan
    onEmailAddClick: (String) -> Unit  // Fungsi yang dipanggil ketika tombol "Add" diklik
) {
    var email by remember { mutableStateOf("") }  // State untuk menyimpan email yang dimasukkan
    var errorDialogVisible by remember { mutableStateOf(false) } // Menyimpan status dialog error

    // Menangani tombol konfirmasi
    val onConfirmClick = {
        if (!email.contains("@")) {
            // Jika email tidak mengandung "@" maka tampilkan dialog error
            errorDialogVisible = true
        } else {
            // Jika email valid, lanjutkan untuk menambah email
            onEmailAddClick(email)
            onDismissRequest() // Menutup dialog
            email = "" // Reset nilai email
        }
    }

    // Dialog utama untuk input email
    AlertDialog(
        onDismissRequest = onDismissRequest,  // Fungsi yang dipanggil saat dialog dibatalkan
        title = {
            Text(text = stringResource(R.string.enter_email))  // Judul dialog
        },
        text = {
            // TextField untuk memasukkan email
            OutlinedTextField(
                value = email,  // Nilai input email
                onValueChange = { email = it },  // Mengubah nilai email saat user mengetik
                label = { Text(text = stringResource(R.string.email)) },  // Label untuk input email
                maxLines = 1,  // Membatasi input hanya pada satu baris
                singleLine = true,  // Memastikan input hanya satu baris
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email  // Menentukan jenis keyboard (email)
                )
            )
        },
        confirmButton = {
            // Tombol konfirmasi untuk menambah email
            TextButton(
                onClick = onConfirmClick  // Menangani tombol konfirmasi
            ) {
                Text(text = stringResource(R.string.add))  // Teks tombol konfirmasi "Add"
            }
        },
        dismissButton = {
            // Tombol pembatalan untuk menutup dialog
            TextButton(
                onClick = { onDismissRequest() }  // Menutup dialog tanpa menambahkan email
            ) {
                Text(text = stringResource(R.string.cancel))  // Teks tombol "Cancel"
            }
        }
    )

    // Dialog error jika email tidak valid (tidak mengandung "@")
    if (errorDialogVisible) {
        AlertDialog(
            onDismissRequest = { errorDialogVisible = false },  // Menutup dialog error
            title = { Text(text = "Failed") },  // Judul dialog error
            text = { Text(text = "Email must contain '@'.") },  // Pesan error
            confirmButton = {
                TextButton(
                    onClick = { errorDialogVisible = false }  // Menutup dialog error
                ) {
                    Text(text = "OK")  // Tombol konfirmasi
                }
            }
        )
    }
}