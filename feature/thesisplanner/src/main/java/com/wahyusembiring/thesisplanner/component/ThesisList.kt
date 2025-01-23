package com.wahyusembiring.thesisplanner.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.thesisplanner.R
import com.wahyusembiring.thesisplanner.screen.thesisselection.Thesis
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.theme.spacing

@Composable
fun ThesisList(
    listOfThesis: List<Thesis>, // Daftar tesis yang akan ditampilkan
    onThesisClick: (Thesis) -> Unit, // Fungsi callback saat tesis diklik
    onDeleteThesis: (Thesis) -> Unit // Fungsi callback saat tesis dihapus
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth() // Mengisi lebar layar
    ) {
        items(
            items = listOfThesis, // Menampilkan setiap tesis dalam daftar
            key = { it.thesis.id } // Menentukan key unik untuk tiap tesis berdasarkan ID
        ) { thesis -> // Untuk setiap tesis
            var showConfirmationDialog by remember { mutableStateOf(false) } // State untuk menampilkan dialog konfirmasi

            ThesisCard(
                modifier = Modifier.padding( // Padding sekitar card tesis
                    horizontal = MaterialTheme.spacing.Medium, // Padding horizontal medium
                    vertical = MaterialTheme.spacing.Small // Padding vertikal kecil
                ),
                thesis = thesis, // Mengirim data tesis ke card
                onClick = { onThesisClick(thesis) }, // Menangani klik pada tesis
                onDeleteClick = { // Menangani klik hapus pada tesis
                    showConfirmationDialog = true // Menampilkan dialog konfirmasi penghapusan
                }
            )

            // Menampilkan dialog konfirmasi jika diperlukan
            if (showConfirmationDialog) {
                ConfirmationAlertDialog(
                    onPositiveButtonClick = { // Jika tombol positif diklik (ya), hapus tesis
                        onDeleteThesis(thesis)
                        showConfirmationDialog = false // Menutup dialog
                    },
                    onNegativeButtonClick = { showConfirmationDialog = false }, // Menutup dialog jika tombol negatif diklik (tidak)
                    title = stringResource(id = R.string.delete_thesis), // Judul dialog konfirmasi
                    message = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_thesis), // Pesan konfirmasi
                    positiveButtonText = stringResource(id = R.string.yes), // Teks untuk tombol ya
                    negativeButtonText = stringResource(id = R.string.no), // Teks untuk tombol tidak
                    onDismissRequest = { showConfirmationDialog = false } // Menutup dialog jika di luar dialog diklik
                )
            }
        }
    }
}