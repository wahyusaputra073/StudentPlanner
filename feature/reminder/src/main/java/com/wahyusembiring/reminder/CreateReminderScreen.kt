package com.wahyusembiring.reminder

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.wahyusembiring.ui.component.button.AddDateButton
import com.wahyusembiring.ui.component.button.AddReminderButton
import com.wahyusembiring.ui.component.button.ChooseColorButton
import com.wahyusembiring.ui.component.modalbottomsheet.component.NavigationAndActionButtonHeader
import com.wahyusembiring.ui.component.officehourinput.DurationInput
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.error.ErrorAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.loading.LoadingAlertDialog
import com.wahyusembiring.ui.component.popup.picker.attachmentpicker.AttachmentPicker
import com.wahyusembiring.ui.component.popup.picker.colorpicker.ColorPicker
import com.wahyusembiring.ui.component.popup.picker.datepicker.DatePicker
import com.wahyusembiring.ui.component.popup.picker.durationtimepicker.DurationTimePicker
import com.wahyusembiring.ui.component.popup.picker.timepicker.TimePicker
import com.wahyusembiring.ui.theme.spacing

@Composable
fun CreateReminderScreen(
    viewModel: CreateReminderScreenViewModel, // ViewModel untuk layar pembuatan pengingat
    navController: NavHostController, // Controller navigasi untuk navigasi antar layar
) {
    // Mengambil state dari ViewModel secara otomatis dengan collectAsState
    val state by viewModel.state.collectAsState()

    // Memanggil fungsi Composable utama untuk layar pembuatan pengingat
    CreateReminderScreen(
        state = state, // Meneruskan state dari ViewModel ke fungsi utama
        onUIEvent = viewModel::onUIEvent, // Callback untuk event UI, diarahkan langsung ke ViewModel
        onNavigateUp = {
            navController.navigateUp() // Menangani navigasi ke layar sebelumnya
        }
    )
}


@Suppress("t") // Menonaktifkan peringatan tertentu pada kompilasi.
@Composable
private fun CreateReminderScreen(
    state: CreateReminderScreenUIState, // State UI yang menggambarkan kondisi layar.
    onUIEvent: (CreateReminderScreenUIEvent) -> Unit, // Callback untuk menangani event UI.
    onNavigateUp: () -> Unit, // Callback untuk navigasi kembali.
) {
    val context = LocalContext.current // Mendapatkan konteks lokal.

    val notificationPermissionRequestLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions() // Kontrak untuk meminta beberapa izin.
        ) { permissions ->
            val allGranted = permissions.values.all { it } // Mengecek apakah semua izin diberikan.
            if (allGranted) {
                Log.d("PermissionCheck", "All permissions granted, launching event")
                onUIEvent(CreateReminderScreenUIEvent.OnTimePickerButtonClick)
            } else {
                Log.d("PermissionCheck", "Permission denied")
            }
        }

    Scaffold { paddingValues -> // Komponen utama dengan kerangka Scaffold.
        Column(
            modifier = Modifier
                .padding(paddingValues) // Mengatur padding berdasarkan Scaffold.
                .fillMaxSize() // Mengisi ukuran layar penuh.
        ) {
            NavigationAndActionButtonHeader(
                onNavigationButtonClicked = onNavigateUp, // Fungsi untuk tombol navigasi.
                actionButtonText = if (state.isEditMode) { // Teks tombol aksi berdasarkan mode edit.
                    stringResource(R.string.edit)
                } else {
                    stringResource(R.string.save)
                },
                onActionButtonClicked = {
                    onUIEvent(CreateReminderScreenUIEvent.OnSaveButtonClicked)
                },
                navigationButtonDescription = stringResource(R.string.close_create_reminder_sheet)
            )
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.Medium) // Padding dengan tema material.
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(), // Mengisi lebar penuh.
                    label = {
                        Text(text = stringResource(R.string.agenda_title)) // Label teks input.
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_title), // Ikon di depan teks input.
                            contentDescription = stringResource(R.string.agenda_title),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true, // Input hanya satu baris.
                    value = state.title, // Nilai teks dari state.
                    onValueChange = { onUIEvent(CreateReminderScreenUIEvent.OnTitleChanged(it)) },
                )
                AddDateButton(
                    date = state.date, // Tanggal dari state.
                    onClicked = { onUIEvent(CreateReminderScreenUIEvent.OnDatePickerButtonClick) }
                )
                DurationInput(
                    durationTime = state.spanTime, // Durasi dari state.
                    onClicked = {
                        onUIEvent(CreateReminderScreenUIEvent.OnDurationTimePicker)
                    }
                )
                AddReminderButton(
                    time = state.time, // Waktu pengingat dari state.
                    onClicked = {
                        Log.d("ButtonClick", "AddReminderButton clicked")
                        onUIEvent(CreateReminderScreenUIEvent.OnTimePickerButtonClick)
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.event_description)) // Label teks deskripsi acara.
                    },
                    value = state.description, // Nilai deskripsi dari state.
                    onValueChange = {
                        onUIEvent(CreateReminderScreenUIEvent.OnReminderDescriptionChanged(it))
                    },
                )
            }
        }
    }

    if (state.showDatePicker) {
        // Menampilkan dialog DatePicker jika state.showDatePicker bernilai true.
        DatePicker(
            onDismissRequest = { onUIEvent(CreateReminderScreenUIEvent.OnDatePickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onDateSelected = { onUIEvent(CreateReminderScreenUIEvent.OnDatePicked(it)) } // Menangani tanggal yang dipilih oleh pengguna.
        )
    }

    if (state.showTimePicker) {
        // Menampilkan dialog TimePicker jika state.showTimePicker bernilai true.
        TimePicker(
            onDismissRequest = { onUIEvent(CreateReminderScreenUIEvent.OnTimePickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onTimeSelected = { onUIEvent(CreateReminderScreenUIEvent.OnTimePicked(it)) } // Menangani waktu yang dipilih oleh pengguna.
        )
    }

    if (state.showDuraPicker) {
        // Menampilkan dialog DurationTimePicker jika state.showDuraPicker bernilai true.
        DurationTimePicker(
            onDismissRequest = { onUIEvent(CreateReminderScreenUIEvent.OnDurationTimePickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onDurationSelected = { onUIEvent(CreateReminderScreenUIEvent.OnDurationTimePicked(it)) } // Menangani durasi yang dipilih oleh pengguna.
        )
    }

    if (state.showAttachmentPicker) {
        // Menampilkan dialog AttachmentPicker jika state.showAttachmentPicker bernilai true.
        AttachmentPicker(
            onDismissRequest = { onUIEvent(CreateReminderScreenUIEvent.OnAttachmentPickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onAttachmentsConfirmed = { onUIEvent(CreateReminderScreenUIEvent.OnAttachmentPicked(it)) } // Menangani lampiran yang dipilih oleh pengguna.
        )
    }

    if (state.showSavingLoading) {
        // Menampilkan dialog loading saat proses penyimpanan berlangsung.
        LoadingAlertDialog(message = stringResource(R.string.saving)) // Pesan yang ditampilkan adalah "saving".
    }

    if (state.showSaveConfirmationDialog) {
        // Menampilkan dialog konfirmasi sebelum menyimpan agenda jika state.showSaveConfirmationDialog bernilai true.
        ConfirmationAlertDialog(
            title = stringResource(R.string.save_reminder), // Judul dialog konfirmasi.
            message = if (state.isEditMode) { // Pesan yang ditampilkan tergantung pada mode edit.
                stringResource(R.string.are_you_sure_you_want_to_save_this_agenda)
            } else {
                stringResource(R.string.are_you_sure_you_want_to_save_this_agenda)
            },
            positiveButtonText = stringResource(R.string.save), // Teks tombol untuk menyimpan.
            onPositiveButtonClick = {
                onUIEvent(CreateReminderScreenUIEvent.OnSaveReminderConfirmClick) // Menangani klik tombol simpan.
                onUIEvent(CreateReminderScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menutup dialog setelah simpan.
            },
            negativeButtonText = stringResource(R.string.cancel), // Teks tombol untuk membatalkan.
            onNegativeButtonClick = {
                onUIEvent(CreateReminderScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menutup dialog jika dibatalkan.
            },
            onDismissRequest = {
                onUIEvent(CreateReminderScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menutup dialog saat pengguna menutup secara manual.
            },
        )
    }

    if (state.showReminderSavedDialog) {
        // Menampilkan dialog sukses setelah agenda berhasil disimpan.
        InformationAlertDialog(
            title = stringResource(R.string.success), // Judul dialog sukses.
            message = stringResource(R.string.reminder_saved), // Pesan yang ditampilkan adalah "reminder saved".
            buttonText = stringResource(R.string.ok), // Teks tombol untuk menutup dialog.
            onButtonClicked = {
                onUIEvent(CreateReminderScreenUIEvent.OnReminderSavedDialogDismiss) // Menutup dialog setelah tombol diklik.
                onNavigateUp() // Navigasi kembali ke layar sebelumnya.
            },
            onDismissRequest = {
                onUIEvent(CreateReminderScreenUIEvent.OnReminderSavedDialogDismiss) // Menutup dialog saat pengguna menutup secara manual.
                onNavigateUp() // Navigasi kembali ke layar sebelumnya.
            },
        )
    }

    if (state.errorMessage != null) {
        // Menampilkan dialog error jika terdapat pesan error di state.errorMessage.
        ErrorAlertDialog(
            message = state.errorMessage.asString(), // Pesan error yang ditampilkan.
            buttonText = stringResource(R.string.ok), // Teks tombol untuk menutup dialog.
            onButtonClicked = {
                onUIEvent(CreateReminderScreenUIEvent.OnErrorDialogDismiss) // Menutup dialog setelah tombol diklik.
            },
            onDismissRequest = {
                onUIEvent(CreateReminderScreenUIEvent.OnErrorDialogDismiss) // Menutup dialog saat pengguna menutup secara manual.
            }
        )
    }

}