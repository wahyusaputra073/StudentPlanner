package com.wahyusembiring.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.ui.R

// Fungsi Composable untuk menampilkan tombol pilih kategori ujian dengan ikon dan teks
@Composable
fun ExamCategoryPickerButton(
    modifier: Modifier = Modifier,  // Modifier opsional untuk menyesuaikan tampilan
    examCategory: ExamCategory,    // Kategori ujian yang akan ditampilkan
    onClicked: (() -> Unit)? = null, // Lambda fungsi opsional untuk menangani klik
) {
    ListItem(
        modifier = modifier
            .then(
                if (onClicked != null) {
                    // Menambahkan Modifier.clickable jika onClicked tidak null
                    Modifier.clickable { onClicked() }
                } else {
                    Modifier // Jika onClicked null, tidak ada interaksi klik
                }
            ),
        leadingContent = {
            // Menampilkan ikon di sisi kiri sesuai kategori ujian
            Icon(
                painter = when (examCategory) {
                    ExamCategory.WRITTEN -> painterResource(id = R.drawable.ic_written_test) // Ikon untuk ujian tertulis
                    ExamCategory.ORAL -> painterResource(id = R.drawable.ic_oral_test)     // Ikon untuk ujian lisan
                    ExamCategory.PRACTICAL -> painterResource(id = R.drawable.ic_practical_test) // Ikon untuk ujian praktikum
                },
                contentDescription = stringResource(R.string.pick_test_category), // Deskripsi konten untuk aksesibilitas
                tint = MaterialTheme.colorScheme.primary // Pewarnaan sesuai tema
            )
        },
        headlineContent = {
            // Menampilkan teks sesuai kategori ujian
            Text(
                text = when (examCategory) {
                    ExamCategory.WRITTEN -> stringResource(R.string.written_test)   // Teks untuk ujian tertulis
                    ExamCategory.ORAL -> stringResource(R.string.oral_test)         // Teks untuk ujian lisan
                    ExamCategory.PRACTICAL -> stringResource(R.string.practical_test) // Teks untuk ujian praktikum
                },
            )
        }
    )
}