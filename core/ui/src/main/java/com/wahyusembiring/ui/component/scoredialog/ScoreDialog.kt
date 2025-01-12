package com.wahyusembiring.ui.component.scoredialog

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.ui.R

data class ScoreDialog( // Data class untuk menyimpan informasi dialog skor
    val exam: ExamWithSubject, // Data ujian yang terkait dengan subjek
    val initialScore: Int = 0 // Skor awal dengan default 0
)

@Composable
fun ScoreDialog( // Fungsi composable untuk menampilkan dialog skor
    initialScore: Int = 0, // Skor awal dengan default 0
    onDismissRequest: () -> Unit, // Callback untuk permintaan penutupan dialog
    onMarkNotDoneYet: () -> Unit, // Callback untuk menandai ujian belum selesai
    onScoreConfirmed: (score: Int) -> Unit // Callback untuk mengonfirmasi skor
) {
    var score by remember { mutableStateOf(initialScore.toString()) } // State untuk menyimpan skor sementara dalam bentuk teks

    AlertDialog( // Komponen dialog dengan opsi dismiss dan confirm
        onDismissRequest = onDismissRequest, // Aksi saat dialog diminta untuk ditutup
        dismissButton = { // Tombol untuk membatalkan dan menandai "belum selesai"
            TextButton(
                onClick = {
                    onMarkNotDoneYet() // Menjalankan callback untuk "belum selesai"
                    onDismissRequest() // Menutup dialog
                }
            ) {
                Text(text = stringResource(R.string.undone)) // Label tombol "Belum Selesai"
            }
        },
        confirmButton = { // Tombol untuk mengonfirmasi skor
            TextButton(
                onClick = {
                    onScoreConfirmed(score.toIntOrNull() ?: 0) // Menjalankan callback dengan skor yang diinput, default ke 0 jika kosong atau tidak valid
                    onDismissRequest() // Menutup dialog
                }
            ) {
                Text(text = stringResource(R.string.done)) // Label tombol "Selesai"
            }
        },
        title = {
            Text(text = stringResource(R.string.enter_your_exam_score)) // Judul dialog
        },
        text = { // Konten input untuk skor
            OutlinedTextField(
                value = score, // Nilai input saat ini
                onValueChange = { score = it }, // Aksi saat nilai input berubah
                maxLines = 1, // Maksimal satu baris input
                singleLine = true, // Input hanya satu baris
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, // Keyboard angka
                    imeAction = ImeAction.Done // Aksi "Selesai" pada keyboard
                )
            )
        }
    )
}