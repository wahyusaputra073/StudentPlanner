package com.wahyusembiring.exam

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.wahyusembiring.common.navigation.Screen
import com.wahyusembiring.common.util.getNotificationReminderPermission
import com.wahyusembiring.data.model.entity.ExamCategory
import com.wahyusembiring.ui.component.button.AddAttachmentButton
import com.wahyusembiring.ui.component.button.AddDateButton
import com.wahyusembiring.ui.component.button.AddReminderButton
import com.wahyusembiring.ui.component.button.AddDeadlineButton
import com.wahyusembiring.ui.component.button.AddSubjectButton
import com.wahyusembiring.ui.component.button.ExamCategoryPickerButton
import com.wahyusembiring.ui.component.modalbottomsheet.component.NavigationAndActionButtonHeader
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.error.ErrorAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.loading.LoadingAlertDialog
import com.wahyusembiring.ui.component.popup.picker.attachmentpicker.AttachmentPicker
import com.wahyusembiring.ui.component.popup.picker.datepicker.DatePicker
import com.wahyusembiring.ui.component.popup.picker.examcategorypicker.ExamCategoryPicker
import com.wahyusembiring.ui.component.popup.picker.subjectpicker.SubjectPicker
import com.wahyusembiring.ui.component.popup.picker.timepicker.DeadlineTimePicker
import com.wahyusembiring.ui.component.popup.picker.timepicker.TimePicker
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.checkForPermissionOrLaunchPermissionLauncher

@Composable
fun ExamScreen(  // Fungsi Composable untuk tampilan layar ujian
    viewModel: ExamScreenViewModel,  // ViewModel yang digunakan untuk mengelola state
    navController: NavHostController,  // Navigator untuk mengatur navigasi antar layar
) {
    val state by viewModel.state.collectAsState()  // Mengambil state dari ViewModel dengan cara mengobservasi perubahan
    ExamScreenUI(  // Menampilkan UI dari layar ujian
        state = state,  // State yang diterima dari ViewModel
        onUIEvent = viewModel::onUIEvent,  // Event UI yang diteruskan ke ViewModel
        onNavigateBack = {  // Fungsi untuk menavigasi kembali ke layar sebelumnya
            navController.navigateUp()
        },
        onNavigateToCreateSubjectScreen = {  // Fungsi untuk menavigasi ke layar pembuatan subject
            navController.navigate(Screen.CreateSubject())
        }
    )
}


@Suppress("t")  // Mengabaikan peringatan terkait kode yang tidak digunakan
@OptIn(ExperimentalMaterial3Api::class)  // Menandai penggunaan API eksperimental
@Composable
private fun ExamScreenUI(  // Fungsi Composable untuk tampilan UI layar ujian
    state: ExamScreenUIState,  // State yang berisi data dan status tampilan
    onUIEvent: (ExamScreenUIEvent) -> Unit,  // Fungsi untuk mengirimkan event ke ViewModel
    onNavigateBack: () -> Unit,  // Fungsi untuk navigasi kembali
    onNavigateToCreateSubjectScreen: () -> Unit,  // Fungsi untuk navigasi ke layar pembuatan subject
) {
    val context = LocalContext.current  // Mendapatkan konteks aplikasi saat ini
    val notificationPermissionRequestLauncher =  // Meluncurkan permintaan izin untuk notifikasi
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->  // Mengelola hasil izin yang diminta
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d("PermissionCheck", "All permissions granted, launching event")
                onUIEvent(ExamScreenUIEvent.OnExamTimePickerClick)  // Meluncurkan event jika izin diterima
            } else {
                Log.d("PermissionCheck", "Permission denied")  // Menangani jika izin ditolak
            }
        }

    val notificationDeadlinePermissionRequestLauncher =  // Permintaan izin untuk deadline notifikasi
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                Log.d("PermissionCheck", "All permissions granted, launching event")
                onUIEvent(ExamScreenUIEvent.OnExamDeadlineTimePickerClick)
            } else {
                Log.d("PermissionCheck", "Permission denied")
            }
        }

    Scaffold { paddingValues ->  // Scaffold untuk layout dasar dengan padding
        Column(
            modifier = Modifier
                .padding(paddingValues)  // Menambahkan padding ke kolom
                .fillMaxSize()  // Mengisi seluruh ukuran layar
        ) {
            NavigationAndActionButtonHeader(  // Header dengan tombol navigasi dan aksi
                onNavigationButtonClicked = onNavigateBack,
                actionButtonText = if (state.isEditMode) {  // Menentukan teks tombol berdasarkan mode edit
                    stringResource(R.string.edit)
                } else {
                    stringResource(R.string.save)
                },
                onActionButtonClicked = { onUIEvent(ExamScreenUIEvent.OnSaveExamButtonClick) },
                navigationButtonDescription = stringResource(R.string.close_add_exam_sheet)
            )
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.Medium)  // Padding untuk konten dalam kolom
            ) {
                OutlinedTextField(  // Input untuk nama ujian
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.exam_title))
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = com.wahyusembiring.ui.R.drawable.ic_title),
                            contentDescription = stringResource(R.string.exam_name),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    value = state.name,  // Menggunakan nilai dari state
                    onValueChange = { onUIEvent(ExamScreenUIEvent.OnExamNameChanged(it)) },  // Mengubah nama ujian
                )
                AddDateButton(  // Tombol untuk memilih tanggal ujian
                    date = state.date,
                    onClicked = { onUIEvent(ExamScreenUIEvent.OnExamDatePickerClick) }
                )
                AddDeadlineButton(  // Tombol untuk memilih waktu deadline ujian
                    times = state.times,
                    onClicked = {
                        Log.d("ButtonClick", "AddReminderButton clicked")
                        onUIEvent(ExamScreenUIEvent.OnExamDeadlineTimePickerClick)
                    }
                )

                AddReminderButton(  // Tombol untuk memilih waktu pengingat ujian
                    time = state.time,
                    onClicked = {
                        Log.d("ButtonClick", "AddReminderButton clicked")
                        onUIEvent(ExamScreenUIEvent.OnExamTimePickerClick)
                    }
                )
                ExamCategoryPickerButton(  // Tombol untuk memilih kategori ujian
                    examCategory = state.category,
                    onClicked = { onUIEvent(ExamScreenUIEvent.OnExamCategoryPickerClick) }
                )
                AddSubjectButton(  // Tombol untuk memilih subject ujian
                    subject = state.subject,
                    onClicked = { onUIEvent(ExamScreenUIEvent.OnExamSubjectPickerClick) }
                )

                OutlinedTextField(  // Input untuk deskripsi ujian
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.event_description))
                    },
                    value = state.description,  // Nilai deskripsi ujian
                    onValueChange = {
                        onUIEvent(ExamScreenUIEvent.OnExamDescriptionChanged(it))  // Mengubah deskripsi
                    },
                )

            }
        }
    }

    // Dialog dan picker berdasarkan state untuk memilih tanggal, waktu, kategori, dll.
    if (state.showDatePicker) {
        DatePicker(
            onDismissRequest = { onUIEvent(ExamScreenUIEvent.OnDatePickedDismiss) },
            onDateSelected = { onUIEvent(ExamScreenUIEvent.OnDatePicked(it)) }
        )
    }

    if (state.showTimePicker) {
        TimePicker(
            onDismissRequest = { onUIEvent(ExamScreenUIEvent.OnTimePickedDismiss) },
            onTimeSelected = { onUIEvent(ExamScreenUIEvent.OnTimePicked(it)) }
        )
    }

    if (state.showDeadlineTimePicker) {
        DeadlineTimePicker(
            onDismissRequest = { onUIEvent(ExamScreenUIEvent.OnDeadlineTimePickedDismiss) },
            onTimeSelected = { onUIEvent(ExamScreenUIEvent.OnDeadlineTimePicked(it)) }
        )
    }

    if (state.showSubjectPicker) {
        SubjectPicker(
            subjects = state.subjects,
            onDismissRequest = { onUIEvent(ExamScreenUIEvent.OnSubjectPickedDismiss) },
            onSubjectSelected = { onUIEvent(ExamScreenUIEvent.OnSubjectPicked(it)) },
            navigateToCreateSubjectScreen = onNavigateToCreateSubjectScreen
        )
    }

    if (state.showCategoryPicker) {
        ExamCategoryPicker(
            initialCategory = ExamCategory.WRITTEN,
            onDismissRequest = { onUIEvent(ExamScreenUIEvent.OnCategoryPickedDismiss) },
            onCategoryPicked = { onUIEvent(ExamScreenUIEvent.OnCategoryPicked(it)) }
        )
    }

    // Menampilkan dialog untuk konfirmasi penyimpanan atau error
    if (state.showSavingLoading) {
        LoadingAlertDialog(message = stringResource(R.string.saving))
    }

    if (state.showSaveConfirmationDialog) {
        ConfirmationAlertDialog(
            title = stringResource(R.string.save_exam),
            message = stringResource(R.string.are_you_sure_you_want_to_save_this_exam),
            positiveButtonText = stringResource(R.string.save),
            onPositiveButtonClick = {
                onUIEvent(ExamScreenUIEvent.OnSaveExamConfirmClick)
                onUIEvent(ExamScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
            negativeButtonText = stringResource(R.string.cancel),
            onNegativeButtonClick = {
                onUIEvent(ExamScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
            onDismissRequest = {
                onUIEvent(ExamScreenUIEvent.OnSaveConfirmationDialogDismiss)
            },
        )
    }

    if (state.showExamSavedDialog) {
        InformationAlertDialog(
            title = stringResource(R.string.success),
            message = stringResource(R.string.exam_saved),
            buttonText = stringResource(R.string.ok),
            onButtonClicked = {
                onUIEvent(ExamScreenUIEvent.OnExamSavedDialogDismiss)
                onNavigateBack()
            },
            onDismissRequest = {
                onUIEvent(ExamScreenUIEvent.OnExamSavedDialogDismiss)
                onNavigateBack()
            },
        )
    }

    if (state.errorMessage != null) {
        ErrorAlertDialog(
            message = state.errorMessage.asString(),
            buttonText = stringResource(R.string.ok),
            onButtonClicked = {
                onUIEvent(ExamScreenUIEvent.OnErrorDialogDismiss)
            },
            onDismissRequest = {
                onUIEvent(ExamScreenUIEvent.OnErrorDialogDismiss)
            }
        )
    }
}
