package com.wahyusembiring.ui.component.popup.alertdialog.information

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
fun InformationAlertDialog(
   onButtonClicked: () -> Unit, // Callback saat tombol diklik
   buttonText: String, // Teks pada tombol konfirmasi
   title: String, // Judul dialog
   message: String, // Pesan yang ditampilkan dalam dialog
   onDismissRequest: () -> Unit, // Callback saat dialog ditutup
) {
   AlertDialog(
      onDismissRequest = onDismissRequest, // Menangani penutupan dialog
      confirmButton = { // Tombol konfirmasi
         TextButton(onClick = onButtonClicked) { // Menjalankan aksi saat tombol diklik
            Text(text = buttonText) // Menampilkan teks tombol
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