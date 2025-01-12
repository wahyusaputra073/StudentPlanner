package com.wahyusembiring.ui.component.popup.alertdialog.confirmation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wahyusembiring.ui.theme.spacing

@Composable
fun ConfirmationAlertDialog(
   onPositiveButtonClick: () -> Unit, // Callback untuk aksi tombol positif
   onNegativeButtonClick: () -> Unit, // Callback untuk aksi tombol negatif
   title: String, // Judul dialog
   message: String, // Pesan yang ditampilkan dalam dialog
   positiveButtonText: String, // Teks untuk tombol positif
   negativeButtonText: String, // Teks untuk tombol negatif
   onDismissRequest: () -> Unit, // Callback ketika dialog ditutup
) {
   AlertDialog(
      onDismissRequest = onDismissRequest, // Menangani penutupan dialog
      confirmButton = { // Tombol positif
         TextButton(onClick = onPositiveButtonClick) { // Menjalankan aksi positif
            Text(text = positiveButtonText) // Menampilkan teks tombol positif
         }
      },
      dismissButton = { // Tombol negatif
         TextButton(onClick = onNegativeButtonClick) { // Menjalankan aksi negatif
            Text(text = negativeButtonText) // Menampilkan teks tombol negatif
         }
      },
      title = { // Judul dialog
         Text(
            modifier = Modifier
               .padding(bottom = MaterialTheme.spacing.Medium), // Padding bawah
            style = MaterialTheme.typography.headlineSmall, // Gaya teks judul
            color = AlertDialogDefaults.titleContentColor, // Warna teks judul
            text = title // Menampilkan judul
         )
      },
      text = { // Pesan dalam dialog
         Text(
            modifier = Modifier
               .fillMaxWidth(), // Lebar penuh
            style = MaterialTheme.typography.bodyMedium, // Gaya teks pesan
            color = AlertDialogDefaults.textContentColor, // Warna teks pesan
            text = message // Menampilkan pesan
         )
      }
   )
}