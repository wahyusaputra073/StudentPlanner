package com.wahyusembiring.subject.screen.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.subject.R
import com.wahyusembiring.ui.component.button.ChooseColorButton
import com.wahyusembiring.ui.component.dropdown.Dropdown
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.loading.LoadingAlertDialog
import com.wahyusembiring.ui.component.popup.picker.colorpicker.ColorPicker
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.UIText


@Composable
fun CreateSubjectScreen(
    viewModel: CreateSubjectViewModel,  // ViewModel untuk mengelola state dan event
    navController: NavHostController     // Navigator untuk berpindah antar screen
) {
    // Mengambil state terbaru dari ViewModel menggunakan collectAsStateWithLifecycle
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Tampilan UI untuk CreateSubjectScreen
    CreateSubjectScreen(
        modifier = Modifier.fillMaxSize(),  // Mengatur ukuran tampilan agar mengisi penuh
        state = state,  // Menyediakan state saat ini untuk tampilan
        onUIEvent = viewModel::onUIEvent,  // Menangani event UI dengan ViewModel
        onNavigateUp = {  // Aksi untuk kembali ke layar sebelumnya
            navController.navigateUp()
        },
        onNavigateToCreateLecturer = {  // Aksi untuk navigasi ke layar pembuatan dosen
            navController.navigate(Screen.AddLecturer())
        }
    )
}


@Composable
private fun CreateSubjectScreen(
    modifier: Modifier = Modifier,  // Modifier untuk menyesuaikan gaya tampilan
    state: CreateSubjectScreenUIState,  // State yang berisi data UI terkini
    onUIEvent: (CreateSubjectScreenUIEvent) -> Unit,  // Fungsi untuk menangani event UI
    onNavigateUp: () -> Unit,  // Fungsi untuk kembali ke layar sebelumnya
    onNavigateToCreateLecturer: () -> Unit,  // Fungsi untuk navigasi ke layar pembuatan dosen
) {
    // Scaffold untuk menyediakan layout dasar dengan padding
    Scaffold { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)  // Menambahkan padding untuk kolom
        ) {
            // Header dengan tombol kembali dan simpan
            BackAndSaveHeader(
                onBackButtonClicked = onNavigateUp,
                onSaveButtonClicked = {
                    onUIEvent(CreateSubjectScreenUIEvent.OnSaveButtonClicked)
                }
            )

            // Kolom utama yang berisi form input
            Column(
                modifier = Modifier
                    .fillMaxSize()  // Mengisi ukuran penuh
                    .padding(MaterialTheme.spacing.Medium)  // Padding dengan jarak menengah
            ) {
                // Input untuk nama subject
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),  // Memenuhi lebar penuh
                    label = { Text(text = stringResource(R.string.subject_name)) },  // Label nama subject
                    leadingIcon = {  // Ikon di depan input
                        Icon(
                            painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_title),
                            contentDescription = stringResource(R.string.subject_name),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    value = state.name,  // Nilai input subject
                    onValueChange = { onUIEvent(CreateSubjectScreenUIEvent.OnSubjectNameChanged(it)) },  // Mengubah nilai input
                )

                // Input untuk nama ruang
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),  // Memenuhi lebar penuh
                    leadingIcon = {  // Ikon di depan input
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = stringResource(R.string.room),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    placeholder = { Text(text = stringResource(R.string.room)) },  // Placeholder untuk ruang
                    singleLine = true,
                    value = state.room,  // Nilai input ruang
                    onValueChange = { onUIEvent(CreateSubjectScreenUIEvent.OnRoomChanged(it)) },  // Mengubah nilai input
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,  // Border transparan saat fokus
                        unfocusedBorderColor = Color.Transparent  // Border transparan saat tidak fokus
                    )
                )

                // Tombol untuk memilih warna
                ChooseColorButton(
                    color = state.color,  // Warna yang dipilih
                    onClick = { onUIEvent(CreateSubjectScreenUIEvent.OnPickColorButtonClicked) }  // Aksi untuk memilih warna
                )

                // Dropdown untuk memilih dosen
                Dropdown(
                    items = state.lecturers,  // Daftar dosen
                    title = {
                        if (it?.name != null) {
                            UIText.DynamicString(it.name)  // Menampilkan nama dosen
                        } else {
                            UIText.StringResource(R.string.there_are_no_lecturer_selected)  // Pesan jika belum memilih dosen
                        }
                    },
                    selected = state.lecturer,  // Dosen yang dipilih
                    onItemClick = { onUIEvent(CreateSubjectScreenUIEvent.OnLecturerSelected(it)) },  // Aksi saat memilih dosen
                    emptyContent = {  // Konten saat tidak ada dosen yang tersedia
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.spacing.Medium),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(R.string.there_are_no_lecturer_avaliable))  // Pesan jika tidak ada dosen
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Small))  // Spasi kecil
                            Button(
                                onClick = onNavigateToCreateLecturer  // Navigasi untuk menambah dosen baru
                            ) {
                                Text(text = stringResource(R.string.add_new_lecturer))
                            }
                        }
                    }
                )
            }
        }
    }

    // Dialog pemilihan warna jika diaktifkan
    if (state.showColorPicker) {
        ColorPicker(
            initialColor = state.color,  // Warna awal
            onDismissRequest = { onUIEvent(CreateSubjectScreenUIEvent.OnColorPickerDismiss) },  // Aksi saat dialog ditutup
            onColorConfirmed = { onUIEvent(CreateSubjectScreenUIEvent.OnColorPicked(it)) }  // Aksi saat warna dipilih
        )
    }

    // Dialog loading saat penyimpanan
    if (state.showSavingLoading) {
        LoadingAlertDialog(message = stringResource(R.string.saving))
    }

    // Dialog konfirmasi penyimpanan subject
    if (state.showSaveConfirmationDialog) {
        ConfirmationAlertDialog(
            title = stringResource(R.string.save_subject),
            message = stringResource(R.string.are_you_sure_you_want_to_save_this_subject),
            positiveButtonText = stringResource(R.string.save),
            onPositiveButtonClick = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSaveConfirmationDialogConfirm)
            },
            negativeButtonText = stringResource(R.string.cancel),
            onNegativeButtonClick = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
            onDismissRequest = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
        )
    }

    // Dialog informasi saat subject berhasil disimpan
    if (state.showSubjectSavedDialog) {
        InformationAlertDialog(
            title = stringResource(R.string.success),
            message = stringResource(R.string.subject_saved),
            buttonText = stringResource(R.string.ok),
            onButtonClicked = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSubjectSavedDialogDismiss)
                onNavigateUp()
            },
            onDismissRequest = {
                onUIEvent(CreateSubjectScreenUIEvent.OnSubjectSavedDialogDismiss)
                onNavigateUp()
            },
        )
    }
}


@Composable
private fun BackAndSaveHeader(
    onBackButtonClicked: () -> Unit,  // Parameter untuk aksi ketika tombol kembali ditekan
    onSaveButtonClicked: () -> Unit   // Parameter untuk aksi ketika tombol simpan ditekan
) {
    Row(
        modifier = Modifier.fillMaxWidth(),  // Mengatur agar Row memenuhi lebar layar
        horizontalArrangement = Arrangement.SpaceBetween,  // Memberikan jarak antara elemen di dalam Row
        verticalAlignment = Alignment.CenterVertically  // Menyelaraskan elemen secara vertikal di tengah
    ) {
        IconButton(
            onClick = onBackButtonClicked  // Menangani aksi klik pada tombol kembali
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),  // Menggunakan ikon panah kembali
                contentDescription = stringResource(R.string.back)  // Deskripsi untuk aksesibilitas tombol kembali
            )
        }
        Button(
            modifier = Modifier.padding(end = MaterialTheme.spacing.Medium),  // Memberikan padding di sisi kanan tombol
            onClick = onSaveButtonClicked  // Menangani aksi klik pada tombol simpan
        ) {
            Text(text = stringResource(id = R.string.save))  // Menampilkan teks "Simpan" pada tombol
        }
    }
}
