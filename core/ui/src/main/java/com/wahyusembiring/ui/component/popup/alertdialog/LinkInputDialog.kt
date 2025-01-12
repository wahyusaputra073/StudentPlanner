package com.wahyusembiring.ui.component.popup.alertdialog

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.HabitTheme
import com.wahyusembiring.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class) // Menggunakan API eksperimental dari Material3
@Composable
fun LinkInputDialog(
   onDismissRequest: () -> Unit, // Callback untuk menutup dialog
   onLinkConfirmed: (uri: Uri) -> Unit // Callback saat link dikonfirmasi
) {
   var link by remember { mutableStateOf("") } // Menyimpan nilai link yang dimasukkan

   BasicAlertDialog(
      onDismissRequest = onDismissRequest // Menangani penutupan dialog
   ) {
      Surface(
         shape = AlertDialogDefaults.shape, // Bentuk kontainer dialog
         tonalElevation = AlertDialogDefaults.TonalElevation, // Elevasi untuk bayangan
         color = AlertDialogDefaults.containerColor, // Warna latar belakang kontainer
      ) {
         Column(
            modifier = Modifier
               .padding(MaterialTheme.spacing.Large) // Memberikan padding di sekitar kolom
         ) {
            OutlinedTextField(
               modifier = Modifier
                  .fillMaxWidth(), // Membuat field mengisi lebar penuh
               value = link, // Nilai dari link yang dimasukkan
               onValueChange = { link = it }, // Memperbarui nilai saat ada perubahan
               label = { Text(text = "URL Link:") }, // Label untuk input field
               singleLine = true, // Membatasi input hanya satu baris
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Memberikan ruang di antara elemen
            Row(
               modifier = Modifier
                  .fillMaxWidth(), // Membuat baris mengisi lebar penuh
               horizontalArrangement = Arrangement.End // Menempatkan tombol di sebelah kanan
            ) {
               TextButton(onClick = onDismissRequest) { // Tombol untuk menutup dialog
                  Text(text = stringResource(R.string.cancel)) // Teks "Cancel"
               }
               TextButton(
                  onClick = {
                     onLinkConfirmed(Uri.parse(link)) // Mengirimkan link sebagai Uri
                     onDismissRequest() // Menutup dialog setelah konfirmasi
                  }
               ) {
                  Text(text = stringResource(R.string.confirm)) // Teks "Confirm"
               }
            }
         }
      }
   }
}


@Preview(showBackground = true)
@Composable
private fun LinkInputDialogPreview() {
   HabitTheme {
      Box(
         modifier = Modifier.fillMaxSize(),
         contentAlignment = Alignment.Center
      ) {
         LinkInputDialog(
            onDismissRequest = {},
            onLinkConfirmed = {}
         )
      }
   }
}