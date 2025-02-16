package com.wahyusembiring.agenda

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import com.wahyusembiring.data.model.Time
import com.wahyusembiring.reminder.R
import com.wahyusembiring.ui.component.button.AddAgendaReminderButton
import com.wahyusembiring.ui.component.button.AddDateButton
import com.wahyusembiring.ui.component.button.AddReminderButton
import com.wahyusembiring.ui.component.modalbottomsheet.component.NavigationAndActionButtonHeader
import com.wahyusembiring.ui.component.officehourinput.DurationInput
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.error.ErrorAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.loading.LoadingAlertDialog
import com.wahyusembiring.ui.component.popup.picker.attachmentpicker.AttachmentPicker
import com.wahyusembiring.ui.component.popup.picker.datepicker.DatePicker
import com.wahyusembiring.ui.component.popup.picker.durationtimepicker.DurationTimePicker
import com.wahyusembiring.ui.component.popup.picker.durationtimepicker.TimePickerOptionAgenda
import com.wahyusembiring.ui.component.popup.picker.timepicker.TimePicker
import com.wahyusembiring.ui.theme.spacing

@Composable
fun CreateReminderScreen(
    viewModel: AddAgendaScreenViewModel, // ViewModel untuk layar pembuatan pengingat
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
    state: AddAgendaScreenUIState, // State UI yang menggambarkan kondisi layar.
    onUIEvent: (AddAgendaScreenUIEvent) -> Unit, // Callback untuk menangani event UI.
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
                onUIEvent(AddAgendaScreenUIEvent.OnTimePickerButtonClick)
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
                    onUIEvent(AddAgendaScreenUIEvent.OnSaveButtonClicked)
                },
                navigationButtonDescription = stringResource(R.string.close_create_agenda_sheet)
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
                    onValueChange = { onUIEvent(AddAgendaScreenUIEvent.OnTitleChanged(it)) },
                )
                AddDateButton(
                    date = state.date, // Tanggal dari state.
                    onClicked = { onUIEvent(AddAgendaScreenUIEvent.OnDatePickerButtonClick) }
                )
                DurationInput(
                    durationTime = state.spanTime, // Durasi dari state.
                    onClicked = {
                        onUIEvent(AddAgendaScreenUIEvent.OnDurationTimePicker)
                    }
                )


                AddAgendaReminderButton(
                    time = state.time, // Waktu pengingat dari state.
                    startTime = state.spanTime?.startTime ?: Time(0, 0), // Gunakan startTime jika spanTime tidak null, atau Time(0, 0) sebagai default.
                    onClicked = {
                        Log.d("ButtonClick", "AddReminderButton clicked")
                        onUIEvent(AddAgendaScreenUIEvent.OnTimePickerButtonClick)
                    }
                )


                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.event_description)) // Label teks deskripsi acara.
                    },
                    value = state.description, // Nilai deskripsi dari state.
                    onValueChange = {
                        onUIEvent(AddAgendaScreenUIEvent.OnReminderDescriptionChanged(it))
                    },
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.Small),
                    onClick = { onUIEvent(AddAgendaScreenUIEvent.OnSendEmailButtonClicked) }
                ) {
                    Icon(
                        painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_title),
                        contentDescription = "Send Email",
                        modifier = Modifier.padding(end = MaterialTheme.spacing.Small)
                    )
                    Text(text = "Send via Email")
                }
            }
        }
    }

    if (state.showDatePicker) {
        // Menampilkan dialog DatePicker jika state.showDatePicker bernilai true.
        DatePicker(
            onDismissRequest = { onUIEvent(AddAgendaScreenUIEvent.OnDatePickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onDateSelected = { onUIEvent(AddAgendaScreenUIEvent.OnDatePicked(it)) } // Menangani tanggal yang dipilih oleh pengguna.
        )
    }

    if (state.showTimePicker) {
        state.spanTime?.let { spanTime ->  // Menggunakan safe call dengan let
            TimePickerOptionAgenda(
                spanTime = spanTime,  // Sekarang spanTime sudah non-null
                onDismissRequest = { onUIEvent(AddAgendaScreenUIEvent.OnTimePickerDismiss) },
                onTimeSelected = { onUIEvent(AddAgendaScreenUIEvent.OnTimePicked(it)) }
            )
        }
    }

    if (state.showDuraPicker) {
        // Menampilkan dialog DurationTimePicker jika state.showDuraPicker bernilai true.
        DurationTimePicker(
            onDismissRequest = { onUIEvent(AddAgendaScreenUIEvent.OnDurationTimePickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onDurationSelected = { onUIEvent(AddAgendaScreenUIEvent.OnDurationTimePicked(it)) } // Menangani durasi yang dipilih oleh pengguna.
        )
    }

    if (state.showAttachmentPicker) {
        // Menampilkan dialog AttachmentPicker jika state.showAttachmentPicker bernilai true.
        AttachmentPicker(
            onDismissRequest = { onUIEvent(AddAgendaScreenUIEvent.OnAttachmentPickerDismiss) }, // Menutup dialog saat pengguna membatalkan.
            onAttachmentsConfirmed = { onUIEvent(AddAgendaScreenUIEvent.OnAttachmentPicked(it)) } // Menangani lampiran yang dipilih oleh pengguna.
        )
    }

    if (state.showSavingLoading) {
        // Menampilkan dialog loading saat proses penyimpanan berlangsung.
        LoadingAlertDialog(message = stringResource(R.string.saving)) // Pesan yang ditampilkan adalah "saving".
    }

    if (state.showSaveConfirmationDialog) {
        // Menampilkan dialog konfirmasi sebelum menyimpan agenda jika state.showSaveConfirmationDialog bernilai true.
        ConfirmationAlertDialog(
            title = stringResource(R.string.save_agenda), // Judul dialog konfirmasi.
            message = if (state.isEditMode) { // Pesan yang ditampilkan tergantung pada mode edit.
                stringResource(R.string.are_you_sure_you_want_to_save_this_agenda)
            } else {
                stringResource(R.string.are_you_sure_you_want_to_save_this_agenda)
            },
            positiveButtonText = stringResource(R.string.save), // Teks tombol untuk menyimpan.
            onPositiveButtonClick = {
                onUIEvent(AddAgendaScreenUIEvent.OnSaveReminderConfirmClick) // Menangani klik tombol simpan.
                onUIEvent(AddAgendaScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menutup dialog setelah simpan.
            },
            negativeButtonText = stringResource(R.string.cancel), // Teks tombol untuk membatalkan.
            onNegativeButtonClick = {
                onUIEvent(AddAgendaScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menutup dialog jika dibatalkan.
            },
            onDismissRequest = {
                onUIEvent(AddAgendaScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menutup dialog saat pengguna menutup secara manual.
            },
        )
    }

    if (state.showReminderSavedDialog) {
        // Menampilkan dialog sukses setelah agenda berhasil disimpan.
        InformationAlertDialog(
            title = stringResource(R.string.success), // Judul dialog sukses.
            message = stringResource(R.string.agenda_saved), // Pesan yang ditampilkan adalah "agenda saved".
            buttonText = stringResource(R.string.ok), // Teks tombol untuk menutup dialog.
            onButtonClicked = {
                onUIEvent(AddAgendaScreenUIEvent.OnReminderSavedDialogDismiss) // Menutup dialog setelah tombol diklik.
                onNavigateUp() // Navigasi kembali ke layar sebelumnya.
            },
            onDismissRequest = {
                onUIEvent(AddAgendaScreenUIEvent.OnReminderSavedDialogDismiss) // Menutup dialog saat pengguna menutup secara manual.
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
                onUIEvent(AddAgendaScreenUIEvent.OnErrorDialogDismiss) // Menutup dialog setelah tombol diklik.
            },
            onDismissRequest = {
                onUIEvent(AddAgendaScreenUIEvent.OnErrorDialogDismiss) // Menutup dialog saat pengguna menutup secara manual.
            }
        )
    }

}