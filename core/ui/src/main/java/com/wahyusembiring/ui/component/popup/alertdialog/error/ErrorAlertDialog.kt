package com.wahyusembiring.ui.component.popup.alertdialog.error

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@Composable
fun ErrorAlertDialog(
   message: String, // Pesan yang ditampilkan dalam dialog
   buttonText: String, // Teks pada tombol konfirmasi
   onDismissRequest: () -> Unit, // Callback saat dialog ditutup
   onButtonClicked: () -> Unit = onDismissRequest, // Callback saat tombol diklik (default: tutup dialog)
) {
   AlertDialog(
      onDismissRequest = onDismissRequest, // Menangani penutupan dialog
      confirmButton = { // Tombol konfirmasi
         TextButton(onClick = onButtonClicked) { // Menjalankan aksi saat tombol diklik
            Text(text = buttonText) // Menampilkan teks tombol
         }
      },
      icon = { // Ikon error pada dialog
         Icon(
            painter = painterResource(R.drawable.ic_error), // Ikon error
            contentDescription = stringResource(R.string.error), // Deskripsi ikon
            tint = MaterialTheme.colorScheme.error // Warna ikon error
         )
      },
      title = { // Judul dialog
         Text(
            modifier = Modifier
               .padding(bottom = MaterialTheme.spacing.Medium), // Padding bawah
            style = MaterialTheme.typography.headlineSmall, // Gaya teks judul
            color = AlertDialogDefaults.titleContentColor, // Warna teks judul
            text = stringResource(R.string.error) // Menampilkan teks "error"
         )
      },
      text = { // Pesan dalam dialog
         Text(
            modifier = Modifier
               .fillMaxWidth(), // Lebar penuh
            style = MaterialTheme.typography.bodyMedium, // Gaya teks pesan
            color = AlertDialogDefaults.textContentColor, // Warna teks pesan
            text = message // Menampilkan pesan error
         )
      }
   )
}