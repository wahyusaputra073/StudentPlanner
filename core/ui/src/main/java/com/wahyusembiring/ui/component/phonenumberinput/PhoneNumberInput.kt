package com.wahyusembiring.ui.component.phonenumberinput

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
fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    phoneNumbers: List<String>, // Daftar nomor telepon yang akan ditampilkan
    onNewPhoneNumber: (String) -> Unit, // Fungsi untuk menambahkan nomor telepon baru
    onDeletePhoneNumber: (String) -> Unit // Fungsi untuk menghapus nomor telepon
) {
    var showPhoneNumberDialog by remember { mutableStateOf(false) } // State untuk menampilkan dialog input nomor telepon

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.Medium),
                text = stringResource(R.string.phone_number), // Teks judul untuk input nomor telepon
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                onClick = { showPhoneNumberDialog = true } // Menampilkan dialog input nomor telepon
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), // Ikon untuk menambah nomor telepon
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        HorizontalDivider() // Pembatas horizontal
        Column(
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.Large)
                .fillMaxWidth() // Membuat tombol selebar layar
        ) {
            if (phoneNumbers.isEmpty()) {
                EmptyPhoneNumber() // Menampilkan pesan jika belum ada nomor telepon
            }
            for (phoneNumber in phoneNumbers) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))
                PhoneNumberListItem(phoneNumber = phoneNumber, // Menampilkan daftar nomor telepon
                    onDeleteClick = { onDeletePhoneNumber(phoneNumber) }) // Menambahkan aksi untuk menghapus nomor telepon

            }
        }
    }

    if (showPhoneNumberDialog) {
        PhoneNumberInputDialog(
            onDismissRequest = { showPhoneNumberDialog = false }, // Menutup dialog
            onPhoneNumberAddClick = onNewPhoneNumber // Menambahkan nomor telepon baru
        )
    }
}

@Composable
private fun PhoneNumberListItem(
    phoneNumber: String, // Nomor telepon yang ditampilkan
    onDeleteClick: () -> Unit // Fungsi untuk menghapus nomor telepon
) {
    var expanded by remember { mutableStateOf(false) } // State untuk mengatur apakah dropdown menu terbuka atau tidak

    Row(
        modifier = Modifier
            .requiredHeight(IntrinsicSize.Min)// Menetapkan tinggi baris sesuai dengan isi
            .fillMaxWidth() // Membuat tombol selebar layar
        ,horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_contact), // Ikon kontak
            tint = MaterialTheme.colorScheme.secondary, // Warna ikon
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.Medium)) // Memberi jarak antar elemen
        VerticalDivider(
            modifier = Modifier.fillMaxHeight() // Pembatas vertikal antar elemen
        )
        Spacer(modifier = Modifier
            .width(MaterialTheme.spacing.Medium)
            .fillMaxWidth()) // Memberi jarak antar elemen
        Text(
            text = phoneNumber // Menampilkan nomor telepon
        )
        Box {
            IconButton(
                onClick = { expanded = true }, // Menampilkan dropdown menu saat tombol diklik
                modifier = Modifier
                    .fillMaxWidth() // Membuat tombol selebar layar
                    .height(48.dp) // Tinggi tombol untuk proporsi yang sesuai
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vertical), // Ikon titik tiga untuk menu opsi
                    contentDescription = "Options",
                    tint = MaterialTheme.colorScheme.secondary, // Warna ikon
                    modifier = Modifier.align(Alignment.Center) // Menengahkan ikon di dalam tombol
                )
            }

            // Dropdown menu untuk opsi
            DropdownMenu(
                expanded = expanded, // Status apakah dropdown menu terbuka
                onDismissRequest = { expanded = false } // Menutup dropdown jika area diluar diklik
            ) {
                DropdownMenuItem(
                    text = { Text("Delete") }, // Opsi hapus
                    onClick = {
                        onDeleteClick() // Menjalankan fungsi penghapusan
                        expanded = false // Menutup dropdown setelah klik
                    }
                )
            }
        }
    }
}


@Composable
private fun EmptyPhoneNumber() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Membuat kotak yang memenuhi lebar layar
        contentAlignment = Alignment.Center // Menyusun konten di tengah
    ) {
        Text(
            modifier = Modifier.padding( // Menambahkan padding pada teks
                horizontal = MaterialTheme.spacing.Medium, // Padding horizontal
                vertical = MaterialTheme.spacing.Small // Padding vertikal
            ),
            text = stringResource(R.string.no_phone_number), // Menampilkan teks jika tidak ada nomor telepon
            style = MaterialTheme.typography.bodyMedium.copy( // Mengatur gaya teks
                fontStyle = FontStyle.Italic // Menjadikan teks miring
            )
        )
    }
}


@Composable
private fun PhoneNumberInputDialog(
    onDismissRequest: () -> Unit, // Fungsi untuk menutup dialog
    onPhoneNumberAddClick: (String) -> Unit // Fungsi untuk menambahkan nomor telepon
) {
    var phoneNumber by remember { mutableStateOf("") } // Menyimpan input nomor telepon
    var errorDialogVisible by remember { mutableStateOf(false) } // Untuk mengontrol apakah dialog error ditampilkan

    // Menangani saat tombol konfirmasi diklik
    val onConfirmClick = {
        if (phoneNumber.length > 15) {
            errorDialogVisible = true // Menampilkan dialog error jika panjang lebih dari 15 karakter
        } else {
            onPhoneNumberAddClick(phoneNumber) // Menambahkan nomor telepon jika valid
            onDismissRequest() // Menutup dialog utama
            phoneNumber = "" // Reset input
        }
    }

    // Dialog utama untuk input nomor telepon
    AlertDialog(
        onDismissRequest = onDismissRequest, // Menangani permintaan untuk menutup dialog
        title = {
            Text(text = stringResource(R.string.enter_phone_number)) // Judul dialog
        },
        text = {
            OutlinedTextField(
                value = phoneNumber, // Menampilkan nilai input nomor telepon
                onValueChange = { phoneNumber = it }, // Menangani perubahan input
                label = { Text(text = stringResource(R.string.phone_number)) }, // Label untuk input
                maxLines = 1, // Hanya satu baris
                singleLine = true, // Input hanya bisa dalam satu baris
                keyboardOptions = KeyboardOptions( // Menentukan tipe keyboard untuk input telepon
                    keyboardType = KeyboardType.Phone
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick // Menangani konfirmasi
            ) {
                Text(text = stringResource(R.string.add)) // Teks untuk tombol konfirmasi
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() } // Menangani pembatalan
            ) {
                Text(text = stringResource(R.string.cancel)) // Teks untuk tombol batal
            }
        }
    )

    // Dialog error jika input melebihi 15 karakter
    if (errorDialogVisible) {
        AlertDialog(
            onDismissRequest = { errorDialogVisible = false }, // Menutup dialog error
            title = { Text(text = "Failed") }, // Judul dialog error
            text = { Text(text = "Phone number cannot exceed 15 characters.") }, // Pesan error
            confirmButton = {
                TextButton(
                    onClick = { errorDialogVisible = false } // Menutup dialog error
                ) {
                    Text(text = "OK") // Tombol konfirmasi
                }
            }
        )
    }
}