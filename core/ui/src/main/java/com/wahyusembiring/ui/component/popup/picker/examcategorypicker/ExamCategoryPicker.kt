package com.wahyusembiring.ui.component.popup.picker.examcategorypicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamCategoryPicker( // Fungsi untuk menampilkan picker kategori ujian dalam bottom sheet
    initialCategory: ExamCategory, // Kategori ujian yang dipilih sebelumnya
    onDismissRequest: () -> Unit, // Fungsi untuk menangani penutupan picker
    onCategoryPicked: (ExamCategory) -> Unit // Fungsi yang dipanggil saat kategori ujian dipilih
) {
    val sheetState = rememberModalBottomSheetState() // State untuk kontrol bottom sheet
    val coroutineScope = rememberCoroutineScope() // Coroutine scope untuk mengelola operasi async

    ModalBottomSheet( // Menampilkan modal bottom sheet
        sheetState = sheetState, // Menyambungkan state ke bottom sheet
        onDismissRequest = onDismissRequest, // Fungsi untuk menangani dismiss
        content = { // Konten yang ditampilkan dalam bottom sheet
            ExamCategoryPickerContent( // Konten picker kategori ujian
                initialCategory = initialCategory, // Kategori ujian yang akan dipilih pertama kali
                onCancelButtonClicked = { // Fungsi untuk menangani aksi cancel
                    coroutineScope.launch { sheetState.hide() } // Menyembunyikan bottom sheet
                        .invokeOnCompletion { onDismissRequest() } // Menutup picker setelah selesai
                },
                onCategoryPicked = { // Fungsi untuk menangani kategori yang dipilih
                    onCategoryPicked(it) // Mengirim kategori yang dipilih
                    coroutineScope.launch { sheetState.hide() } // Menyembunyikan bottom sheet
                        .invokeOnCompletion { onDismissRequest() } // Menutup picker setelah selesai
                }
            )
        }
    )
}

@Composable
private fun ColumnScope.ExamCategoryPickerContent( // Konten untuk memilih kategori ujian
    initialCategory: ExamCategory, // Kategori ujian yang sudah dipilih sebelumnya
    onCancelButtonClicked: () -> Unit, // Fungsi untuk membatalkan pemilihan
    onCategoryPicked: (ExamCategory) -> Unit // Fungsi untuk mengonfirmasi kategori yang dipilih
) {
    val categories = ExamCategory.entries.map { // Membuat daftar kategori ujian yang ada
        when (it) {
            ExamCategory.WRITTEN -> stringResource(R.string.written_test) // Kategori ujian tertulis
            ExamCategory.ORAL -> stringResource(R.string.oral_test) // Kategori ujian lisan
            ExamCategory.PRACTICAL -> stringResource(R.string.practical_test) // Kategori ujian praktek
        }
    }
    var selectedCategoryIndex by remember { // Mengingat indeks kategori yang dipilih
        mutableIntStateOf(ExamCategory.entries.indexOf(initialCategory))
    }

    Text( // Menampilkan judul kategori ujian
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(bottom = MaterialTheme.spacing.Medium),
        style = MaterialTheme.typography.titleLarge,
        text = stringResource(R.string.exam_category)
    )
    Column( // Menampilkan pilihan kategori ujian
        modifier = Modifier.selectableGroup()
    ) {
        categories.forEachIndexed { index, category -> // Menampilkan setiap kategori ujian
            Row( // Menampilkan pilihan kategori sebagai baris
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable( // Membuat elemen dapat dipilih
                        selected = selectedCategoryIndex == index, // Menandai kategori yang dipilih
                        onClick = { selectedCategoryIndex = index }, // Memilih kategori saat diklik
                        role = Role.RadioButton // Menandakan elemen sebagai pilihan radio button
                    )
                    .padding(
                        horizontal = MaterialTheme.spacing.Medium,
                        vertical = MaterialTheme.spacing.Small
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton( // Menampilkan radio button untuk setiap kategori
                    modifier = Modifier.semantics { contentDescription = category },
                    selected = selectedCategoryIndex == index,
                    onClick = null // Menonaktifkan klik langsung pada radio button
                )
                Text( // Menampilkan teks kategori ujian
                    modifier = Modifier.padding(start = MaterialTheme.spacing.Medium),
                    text = category
                )
            }
        }
    }
    Row( // Menampilkan tombol konfirmasi dan batal
        modifier = Modifier
            .align(Alignment.End)
    ) {
        TextButton( // Tombol untuk membatalkan pemilihan kategori
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.Medium),
            onClick = onCancelButtonClicked
        ) {
            Text(text = stringResource(id = R.string.cancel))
        }
        Button( // Tombol untuk mengonfirmasi kategori yang dipilih
            modifier = Modifier
                .padding(MaterialTheme.spacing.Medium),
            onClick = {
                onCategoryPicked(ExamCategory.entries[selectedCategoryIndex]) // Mengonfirmasi pilihan kategori
            }
        ) {
            Text(text = stringResource(id = R.string.confirm))
        }
    }
}


@Preview(showBackground = false)
@Composable
private fun ExamCategoryPickerPreview() {
    Column {
        ExamCategoryPickerContent(
            initialCategory = ExamCategory.WRITTEN,
            onCancelButtonClicked = { },
            onCategoryPicked = { }
        )
    }
}