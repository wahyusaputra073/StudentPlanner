package com.wahyusembiring.ui.component.popup.picker.subjectpicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.ui.component.modalbottomsheet.component.AddNewSubject
import com.wahyusembiring.ui.component.modalbottomsheet.component.SubjectListItem
import com.wahyusembiring.ui.R
import com.wahyusembiring.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class) // Menandakan penggunaan fitur eksperimen dari Material3 API
@Composable
fun SubjectPicker(
    modifier: Modifier = Modifier, // Modifier untuk penyesuaian tampilan
    sheetState: SheetState = rememberModalBottomSheetState(), // Menyimpan status bottom sheet
    onDismissRequest: () -> Unit, // Fungsi yang dipanggil ketika bottom sheet ditutup
    subjects: List<Subject>, // Daftar mata pelajaran yang akan ditampilkan
    onSubjectSelected: (subject: Subject) -> Unit, // Fungsi yang dipanggil saat mata pelajaran dipilih
    navigateToCreateSubjectScreen: () -> Unit, // Fungsi untuk navigasi ke layar pembuatan mata pelajaran
) {
    ModalBottomSheet( // Komponen ModalBottomSheet yang menampilkan daftar mata pelajaran
        modifier = modifier,
        onDismissRequest = onDismissRequest, // Menangani aksi dismiss
        sheetState = sheetState, // Menangani status sheet
    ) {
        SubjectPickerContent( // Konten untuk memilih mata pelajaran
            onSubjectSelected = onSubjectSelected, // Menangani pemilihan mata pelajaran
            onCancelButtonClicked = onDismissRequest, // Menangani aksi batal
            navigateToCreateSubjectScreen = navigateToCreateSubjectScreen, // Navigasi ke layar pembuatan mata pelajaran
            subjects = subjects // Menampilkan daftar mata pelajaran
        )
    }
}


@Composable
private fun ColumnScope.SubjectPickerContent(
    onSubjectSelected: (subject: Subject) -> Unit, // Fungsi untuk menangani pemilihan mata pelajaran
    onCancelButtonClicked: () -> Unit, // Fungsi untuk menangani aksi batal
    navigateToCreateSubjectScreen: () -> Unit, // Fungsi untuk navigasi ke layar pembuatan mata pelajaran
    subjects: List<Subject> = emptyList() // Daftar mata pelajaran yang ditampilkan, default kosong
) {
    Text( // Judul untuk picker mata pelajaran
        modifier = Modifier
            .align(Alignment.CenterHorizontally) // Menempatkan teks di tengah
            .padding(bottom = MaterialTheme.spacing.Medium), // Memberikan padding bawah
        style = MaterialTheme.typography.titleLarge, // Gaya teks besar
        text = stringResource(R.string.pick_a_subject) // Menampilkan teks judul dari resource string
    )
    val listItemColors = ListItemDefaults.colors(containerColor = Color.Transparent) // Menentukan warna item tanpa latar belakang
    AddNewSubject( // Tombol untuk menambahkan mata pelajaran baru
        colors = listItemColors,
        onClicked = navigateToCreateSubjectScreen // Menavigasi ke layar pembuatan mata pelajaran
    )
    subjects.forEach { subject -> // Looping untuk menampilkan daftar mata pelajaran
        SubjectListItem(
            colors = listItemColors, // Menggunakan warna yang sama untuk item
            subject = subject, // Menyediakan data mata pelajaran
            onClicked = onSubjectSelected, // Menangani aksi pemilihan mata pelajaran
            onDeleteSubClick = {} // Menangani aksi hapus mata pelajaran (kosongkan aksi)
        )
    }
    Button( // Tombol untuk membatalkan pemilihan
        modifier = Modifier
            .align(Alignment.End) // Menempatkan tombol di kanan
            .padding(MaterialTheme.spacing.Medium), // Memberikan padding
        onClick = onCancelButtonClicked // Menangani aksi batal
    ) {
        Text(text = stringResource(id = R.string.cancel)) // Teks pada tombol dibaca dari resource string
    }
}


@Preview(showBackground = true)
@Composable
private fun SubjectPickerPreview() {
    Column {
        SubjectPickerContent(
            onSubjectSelected = {},
            onCancelButtonClicked = {},
            navigateToCreateSubjectScreen = {},
            subjects = listOf(
                Subject(
                    name = "Math",
                    color = Color.Red,
                    room = "Room 1",
                    description = "Math description",
                    lecturerId = 1
                )
            )
        )
    }
}