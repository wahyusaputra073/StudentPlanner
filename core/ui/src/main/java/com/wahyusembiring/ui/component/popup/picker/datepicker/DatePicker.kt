package com.wahyusembiring.ui.component.popup.picker.datepicker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class) // Menggunakan API eksperimental dari Material3
@Composable
fun DatePicker(
   onDismissRequest: () -> Unit, // Callback saat dialog ditutup
   onDateSelected: (Date) -> Unit // Callback saat tanggal dipilih
) {
   val datePickerState = rememberDatePickerState( // Mengelola state dari date picker
      initialSelectedDateMillis = System.currentTimeMillis(), // Menentukan tanggal awal yang dipilih (waktu saat ini)
      initialDisplayedMonthMillis = System.currentTimeMillis() // Menentukan bulan yang ditampilkan saat ini
   )
   val enableConfirmButton = remember { // Menentukan apakah tombol konfirmasi dapat diaktifkan
      derivedStateOf { datePickerState.selectedDateMillis != null } // Mengaktifkan tombol jika ada tanggal yang dipilih
   }

   DatePickerDialog(
      modifier = Modifier.padding(MaterialTheme.spacing.Medium), // Menambahkan padding di sekitar dialog
      onDismissRequest = onDismissRequest, // Menangani penutupan dialog
      confirmButton = { // Menangani tombol konfirmasi
         TextButton(
            onClick = { // Menangani klik pada tombol konfirmasi
               onDateSelected(Date(datePickerState.selectedDateMillis!!)) // Mengirimkan tanggal yang dipilih
               onDismissRequest() // Menutup dialog setelah tanggal dipilih
            },
            enabled = enableConfirmButton.value // Mengaktifkan atau menonaktifkan tombol berdasarkan tanggal yang dipilih
         ) {
            Text(text = stringResource(R.string.confirm)) // Teks tombol konfirmasi
         }
      },
      dismissButton = { // Menangani tombol batal
         TextButton(
            onClick = onDismissRequest // Menutup dialog saat tombol batal diklik
         ) {
            Text(text = stringResource(R.string.cancel)) // Teks tombol batal
         }
      }
   ) {
      androidx.compose.material3.DatePicker(state = datePickerState) // Menampilkan komponen DatePicker dengan state yang dikelola
   }
}