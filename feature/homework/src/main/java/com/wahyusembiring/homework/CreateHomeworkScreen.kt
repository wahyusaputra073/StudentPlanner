package com.wahyusembiring.homework

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.getNotificationReminderPermission
import com.wahyusembiring.ui.component.button.AddAttachmentButton
import com.wahyusembiring.ui.component.button.AddDateButton
import com.wahyusembiring.ui.component.button.AddDeadlineButton
import com.wahyusembiring.ui.component.button.AddReminderButton
import com.wahyusembiring.ui.component.button.AddSubjectButton
import com.wahyusembiring.ui.component.modalbottomsheet.component.NavigationAndActionButtonHeader
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.error.ErrorAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.popup.picker.attachmentpicker.AttachmentPicker
import com.wahyusembiring.ui.component.popup.picker.datepicker.DatePicker
import com.wahyusembiring.ui.component.popup.picker.subjectpicker.SubjectPicker
import com.wahyusembiring.ui.component.popup.picker.timepicker.DeadlineTimePicker
import com.wahyusembiring.ui.component.popup.picker.timepicker.TimePicker
import com.wahyusembiring.ui.theme.HabitTheme
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.checkForPermissionOrLaunchPermissionLauncher

@Composable
fun CreateHomeworkScreen(
    viewModel: CreateHomeworkScreenViewModel, // ViewModel untuk mengelola state dan event
    navController: NavHostController, // Controller untuk navigasi antar layar
) {
    val state by viewModel.state.collectAsStateWithLifecycle() // Mengambil state dari ViewModel secara reactive

    CreateHomeworkScreen( // Komponen UI untuk halaman CreateHomeworkScreen
        modifier = Modifier, // Modifier untuk penataan tampilan
        state = state, // Menyediakan state untuk UI
        onUIEvent = viewModel::onUIEvent, // Mengirim event UI ke ViewModel
        onNavigateBack = { navController.navigateUp() }, // Navigasi kembali
        onNavigateToCreateSubjectScreen = { // Navigasi ke layar CreateSubject
            navController.navigate(Screen.CreateSubject())
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class) // Menandakan penggunaan API eksperimental dari Material3
@Composable
private fun CreateHomeworkScreen(
    modifier: Modifier = Modifier, // Modifier untuk pengaturan UI
    state: CreateHomeworkScreenUIState, // State untuk mengontrol UI
    onUIEvent: (CreateHomeworkUIEvent) -> Unit, // Fungsi untuk mengirim event ke ViewModel
    onNavigateBack: () -> Unit, // Fungsi untuk navigasi kembali
    onNavigateToCreateSubjectScreen: () -> Unit, // Fungsi untuk navigasi ke layar pembuatan mata pelajaran
) {
    val context = LocalContext.current // Mengambil konteks lokal aplikasi

    val notificationPermissionRequestLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions() // Launcher untuk meminta izin
        ) { permissions -> // Menangani hasil permintaan izin
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d("PermissionCheck", "All permissions granted, launching event")
                onUIEvent(CreateHomeworkUIEvent.OnPickTimeButtonClicked) // Meluncurkan event setelah izin diberikan
            } else {
                Log.d("PermissionCheck", "Permission denied") // Log jika izin ditolak
            }
        }

    Scaffold { paddingValues -> // Scaffold untuk membuat struktur dasar layar
        Column(
            modifier = modifier
                .padding(paddingValues) // Mengatur padding pada column
                .fillMaxSize() // Mengisi seluruh ukuran layar
        ) {
            // Header dengan tombol navigasi dan aksi (simpan/edit)
            NavigationAndActionButtonHeader(
                onNavigationButtonClicked = onNavigateBack, // Fungsi untuk tombol navigasi kembali
                onActionButtonClicked = {
                    onUIEvent(CreateHomeworkUIEvent.OnSaveHomeworkButtonClicked) // Menyimpan tugas
                },
                actionButtonText = if (state.isEditMode) {
                    stringResource(R.string.edit) // Teks tombol untuk edit mode
                } else {
                    stringResource(R.string.save) // Teks tombol untuk save mode
                },
                navigationButtonDescription = stringResource(R.string.close_add_homework_sheet) // Deskripsi untuk tombol navigasi
            )
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.Medium) // Padding untuk kolom form
            ) {
                // Field untuk judul tugas
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.task_title)) }, // Label untuk input judul
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_title), // Ikon untuk judul
                            contentDescription = stringResource(R.string.homework_title),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true, // Membatasi input hanya satu baris
                    value = state.homeworkTitle, // Nilai untuk judul tugas
                    onValueChange = { onUIEvent(CreateHomeworkUIEvent.OnHomeworkTitleChanged(it)) }, // Mengubah judul tugas
                )
                // Tombol untuk memilih tanggal tugas
                AddDateButton(
                    date = state.date,
                    onClicked = {
                        onUIEvent(CreateHomeworkUIEvent.OnPickDateButtonClicked) // Memilih tanggal tugas
                    }
                )

                // Tombol untuk memilih waktu deadline tugas
                AddDeadlineButton(
                    times = state.times,
                    onClicked = {
                        Log.d("ButtonClick", "AddDeadlineTimeButton clicked")
                        onUIEvent(CreateHomeworkUIEvent.OnPickDeadlineTimeButtonClicked) // Memilih waktu deadline
                    }
                )

                // Tombol untuk memilih waktu pengingat
                AddReminderButton(
                    time = state.time,
                    onClicked = {
                        Log.d("ButtonClick", "AddReminderButton clicked")
                        onUIEvent(CreateHomeworkUIEvent.OnPickTimeButtonClicked) // Memilih waktu pengingat
                    }
                )
                // Tombol untuk memilih mata pelajaran
                AddSubjectButton(
                    subject = state.subject,
                    onClicked = { onUIEvent(CreateHomeworkUIEvent.OnPickSubjectButtonClicked) } // Memilih mata pelajaran
                )

                // Input untuk deskripsi tugas
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.event_description)) }, // Label untuk deskripsi
                    value = state.description, // Nilai deskripsi
                    onValueChange = {
                        onUIEvent(CreateHomeworkUIEvent.OnExamDescriptionChanged(it)) // Mengubah deskripsi tugas
                    }
                )
            }
        }
    }

    // Dialog konfirmasi untuk menyimpan tugas
    if (state.showSaveConfirmationDialog) {
        ConfirmationAlertDialog(
            title = stringResource(id = R.string.save_homework),
            message = stringResource(id = R.string.are_you_sure_you_want_to_save_this_task),
            positiveButtonText = stringResource(id = R.string.save),
            onPositiveButtonClick = {
                onUIEvent(CreateHomeworkUIEvent.OnConfirmSaveHomeworkClick) // Menyimpan tugas
                onUIEvent(CreateHomeworkUIEvent.OnDismissSaveConfirmationDialog) // Menutup dialog
            },
            negativeButtonText = stringResource(id = R.string.cancel),
            onNegativeButtonClick = {
                onUIEvent(CreateHomeworkUIEvent.OnDismissSaveConfirmationDialog) // Menutup dialog tanpa menyimpan
            },
            onDismissRequest = {
                onUIEvent(CreateHomeworkUIEvent.OnDismissSaveConfirmationDialog) // Menutup dialog jika dibatalkan
            },
        )
    }

    // Dialog pemberitahuan jika tugas berhasil disimpan
    if (state.showHomeworkSavedDialog) {
        InformationAlertDialog(
            title = stringResource(id = R.string.success),
            message = stringResource(id = R.string.homework_saved),
            buttonText = stringResource(id = R.string.ok),
            onButtonClicked = {
                onUIEvent(CreateHomeworkUIEvent.OnDismissHomeworkSavedDialog) // Menutup dialog
                onNavigateBack() // Navigasi kembali
            },
            onDismissRequest = {
                onUIEvent(CreateHomeworkUIEvent.OnDismissHomeworkSavedDialog) // Menutup dialog jika dibatalkan
                onNavigateBack() // Navigasi kembali
            },
        )
    }

    // Dialog untuk memilih tanggal
    if (state.showDatePicker) {
        DatePicker(
            onDismissRequest = { onUIEvent(CreateHomeworkUIEvent.OnDismissDatePicker) }, // Menutup picker tanggal
            onDateSelected = { onUIEvent(CreateHomeworkUIEvent.OnDatePicked(it)) } // Memilih tanggal
        )
    }

    // Dialog untuk memilih waktu
    if (state.showTimePicker) {
        TimePicker(
            onDismissRequest = { onUIEvent(CreateHomeworkUIEvent.OnDismissTimePicker) }, // Menutup picker waktu
            onTimeSelected = { onUIEvent(CreateHomeworkUIEvent.OnTimePicked(it)) } // Memilih waktu
        )
    }

    // Dialog untuk memilih waktu deadline
    if (state.showDeadlineTimePicker) {
        DeadlineTimePicker(
            onDismissRequest = { onUIEvent(CreateHomeworkUIEvent.OnDismissDeadlineTimePicker) }, // Menutup picker deadline
            onTimeSelected = { onUIEvent(CreateHomeworkUIEvent.OnDeadlineTimePicked(it)) } // Memilih waktu deadline
        )
    }

    // Dialog untuk memilih mata pelajaran
    if (state.showSubjectPicker) {
        SubjectPicker(
            subjects = state.subjects,
            onDismissRequest = { onUIEvent(CreateHomeworkUIEvent.OnDismissSubjectPicker) }, // Menutup picker mata pelajaran
            onSubjectSelected = { onUIEvent(CreateHomeworkUIEvent.OnSubjectPicked(it)) }, // Memilih mata pelajaran
            navigateToCreateSubjectScreen = onNavigateToCreateSubjectScreen // Navigasi ke pembuatan mata pelajaran
        )
    }

    // Dialog untuk memilih lampiran
    if (state.showAttachmentPicker) {
        AttachmentPicker(
            onDismissRequest = { onUIEvent(CreateHomeworkUIEvent.OnDismissAttachmentPicker) }, // Menutup picker lampiran
            onAttachmentsConfirmed = { onUIEvent(CreateHomeworkUIEvent.OnAttachmentPicked(it)) } // Memilih lampiran
        )
    }

    // Dialog untuk menampilkan pesan error
    if (state.errorMessage != null) {
        ErrorAlertDialog(
            message = state.errorMessage.asString(), // Menampilkan pesan error
            buttonText = stringResource(R.string.ok),
            onButtonClicked = {
                onUIEvent(CreateHomeworkUIEvent.OnDismissErrorDialog) // Menutup dialog error
            },
            onDismissRequest = {
                onUIEvent(CreateHomeworkUIEvent.OnDismissErrorDialog) // Menutup dialog error jika dibatalkan
            }
        )
    }
}