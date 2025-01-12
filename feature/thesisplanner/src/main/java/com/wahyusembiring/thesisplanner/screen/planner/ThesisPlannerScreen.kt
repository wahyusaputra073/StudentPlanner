package com.wahyusembiring.thesisplanner.screen.planner

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.data.util.toFile
import com.wahyusembiring.thesisplanner.R
import com.wahyusembiring.thesisplanner.component.ArticleList
import com.wahyusembiring.thesisplanner.component.Section
import com.wahyusembiring.thesisplanner.component.TaskList
import com.wahyusembiring.ui.component.button.AddDateButton
import com.wahyusembiring.ui.component.modalbottomsheet.component.NavigationAndActionButtonHeader
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.picker.datepicker.DatePicker
import com.wahyusembiring.ui.theme.spacing
import com.wahyusembiring.ui.util.checkForPermissionOrLaunchPermissionLauncher
import com.wahyusembiring.ui.util.getFileAccessPermissionRequest
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun ThesisPlannerScreen(
    viewModel: ThesisPlannerScreenViewModel,
    drawerState: DrawerState,
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ThesisPlannerScreen(
        state = state,
        onUIEvent = viewModel::onUIEvent,
        onNavigateBack = {
            navController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class) // Menggunakan API eksperimen dari Material3
@Composable
private fun ThesisPlannerScreen(
    state: ThesisPlannerScreenUIState, // State untuk tampilan ThesisPlannerScreen
    onUIEvent: (ThesisPlannerScreenUIEvent) -> Unit, // Fungsi callback untuk menangani event UI
    onNavigateBack: () -> Unit // Fungsi callback untuk navigasi kembali
) {
    val scrollState = rememberScrollState() // State untuk mengingat posisi scroll
    val coroutineScope = rememberCoroutineScope() // Scope untuk menjalankan coroutine
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // State untuk BottomSheet

    val context = LocalContext.current // Mengambil konteks lokal aplikasi

    // Launcher untuk membuka dokumen multiple dengan izin baca
    val documentPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            coroutineScope.launch { // Menjalankan operasi di dalam coroutine
                uris.forEach {
                    // Memberikan izin baca pada URI yang dipilih
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                val files = uris.map { it.toFile(context) } // Mengonversi URI ke file
                onUIEvent(ThesisPlannerScreenUIEvent.OnDocumentPickerResult(files)) // Menangani hasil pemilihan dokumen
            }
        }


    val documentPermissionRequestLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            // Meluncurkan picker dokumen setelah izin diberikan
            documentPickerLauncher.launch(arrayOf("application/pdf"))
        }

    Scaffold(
        topBar = { // Membuat bagian atas layar (TopAppBar)
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.thesis_planner)) // Menampilkan teks judul
                },
                navigationIcon = {
                    // Menambahkan tombol navigasi untuk kembali
                    IconButton(
                        onClick = onNavigateBack // Fungsi navigasi kembali saat tombol diklik
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack, // Ikon panah kembali
                            contentDescription = stringResource(R.string.back_to_thesis_selection_screen) // Deskripsi ikon untuk aksesibilitas
                        )
                    }
                }
            )
        }
    )
    { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding) // Padding untuk kolom
                .fillMaxSize() // Mengisi seluruh ukuran layar
                .scrollable(
                    state = scrollState, // State untuk mengatur scroll
                    orientation = Orientation.Vertical // Mengatur orientasi scroll ke vertikal
                )
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth() // Membuat lebar penuh untuk teks input
                    .padding(
                        horizontal = MaterialTheme.spacing.Medium, // Padding horizontal medium
                        vertical = MaterialTheme.spacing.Large // Padding vertikal besar
                    ),
                label = { Text(text = stringResource(R.string.thesis_title)) }, // Label untuk field
                value = state.editedThesisTitle, // Nilai inputan untuk judul tesis
                singleLine = true, // Membatasi input hanya satu baris
                onValueChange = { onUIEvent(ThesisPlannerScreenUIEvent.OnThesisTitleChange(it)) } // Fungsi untuk menangani perubahan nilai
            )
            HorizontalDivider() // Divider horizontal untuk pemisah antar elemen
            Section(
                title = stringResource(R.string.articles), // Judul bagian artikel
                trailingContent = { // Konten bagian kanan (ikon tambah artikel)
                    IconButton(
                        onClick = {
                            checkForPermissionOrLaunchPermissionLauncher(
                                context = context,
                                permissionToRequest = getFileAccessPermissionRequest(),
                                permissionRequestLauncher = documentPermissionRequestLauncher,
                                onPermissionAlreadyGranted = {
                                    // Meluncurkan document picker untuk memilih artikel PDF
                                    documentPickerLauncher.launch(
                                        arrayOf("application/pdf")
                                    )
                                }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // Ikon tambah
                            contentDescription = stringResource(id = R.string.add_article) // Deskripsi ikon untuk aksesibilitas
                        )
                    }
                }
            ) {
                // Menampilkan daftar artikel
                ArticleList(
                    articles = state.articles, // Daftar artikel
                    onArticleClick = { onUIEvent(ThesisPlannerScreenUIEvent.OnArticleClick(it)) }, // Fungsi untuk menangani klik artikel
                    onDeleteArticleClick = {
                        onUIEvent(ThesisPlannerScreenUIEvent.OnDeleteArticleClick(it)) // Fungsi untuk menghapus artikel
                    }
                )
            }
            HorizontalDivider() // Divider horizontal untuk pemisah
            Section(
                title = stringResource(R.string.task), // Judul bagian tugas
                trailingContent = { // Konten bagian kanan (ikon tambah tugas)
                    IconButton(
                        onClick = {
                            onUIEvent(ThesisPlannerScreenUIEvent.OnCreateTaskButtonClick) // Fungsi untuk menangani klik tambah tugas
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // Ikon tambah
                            contentDescription = stringResource(id = R.string.add_task) // Deskripsi ikon untuk aksesibilitas
                        )
                    }
                }
            ) {
                // Menampilkan daftar tugas
                TaskList(
                    tasks = state.tasks, // Daftar tugas
                    onCompletedStatusChange = { task, isCompleted ->
                        onUIEvent(ThesisPlannerScreenUIEvent.OnTaskCompletedStatusChange(task, isCompleted)) // Fungsi untuk menangani perubahan status tugas
                    },
                    onDeleteTaskClick = { onUIEvent(ThesisPlannerScreenUIEvent.OnDeleteTaskClick(it)) } // Fungsi untuk menghapus tugas
                )
            }
            HorizontalDivider() // Divider horizontal untuk pemisah
        }
    }

    if (state.showCreateTaskDialog) { // Cek apakah dialog pembuatan tugas harus ditampilkan
        ModalBottomSheet( // Menampilkan modal bottom sheet
            sheetState = sheetState,
            onDismissRequest = { // Menangani permintaan untuk menutup bottom sheet
                coroutineScope
                    .launch { sheetState.hide() } // Sembunyikan bottom sheet
                    .invokeOnCompletion {
                        onUIEvent(ThesisPlannerScreenUIEvent.OnCreateTaskDialogDismiss) // Kirim event saat dialog ditutup
                    }
            }
        ) {
            var task by remember { // Menyimpan status tugas yang sedang diedit
                mutableStateOf(
                    Task(
                        thesisId = 0,
                        name = "",
                        dueDate = Date(System.currentTimeMillis()) // Tanggal jatuh tempo default adalah sekarang
                    )
                )
            }

            NavigationAndActionButtonHeader( // Header dengan tombol navigasi dan aksi
                onNavigationButtonClicked = { // Menangani klik tombol cancel
                    coroutineScope
                        .launch { sheetState.hide() } // Sembunyikan bottom sheet
                        .invokeOnCompletion {
                            onUIEvent(ThesisPlannerScreenUIEvent.OnCreateTaskDialogDismiss) // Kirim event saat dialog ditutup
                        }
                },
                onActionButtonClicked = { // Menangani klik tombol simpan
                    onUIEvent(ThesisPlannerScreenUIEvent.OnSaveTaskClick(task)) // Simpan tugas
                    coroutineScope
                        .launch { sheetState.hide() } // Sembunyikan bottom sheet
                        .invokeOnCompletion {
                            onUIEvent(ThesisPlannerScreenUIEvent.OnCreateTaskDialogDismiss) // Kirim event saat dialog ditutup
                        }
                },
                navigationButtonDescription = "Cancel create task" // Deskripsi tombol cancel
            )
            OutlinedTextField( // Input untuk nama tugas
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.Medium,
                        vertical = MaterialTheme.spacing.Large
                    ),
                label = { Text(text = stringResource(R.string.task_name)) },
                singleLine = true,
                value = task.name,
                onValueChange = { task = task.copy(name = it) }, // Update nama tugas
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
            Text( // Menampilkan label untuk memilih tanggal jatuh tempo
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.Medium),
                text = stringResource(R.string.due_date),
                style = MaterialTheme.typography.bodySmall,
            )
            AddDateButton( // Tombol untuk memilih tanggal jatuh tempo
                date = task.dueDate,
                onClicked = {
                    onUIEvent(ThesisPlannerScreenUIEvent.OnDatePickerButtonClick) // Tampilkan date picker
                }
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.Large))
            if (state.showDatePicker) { // Jika date picker perlu ditampilkan
                DatePicker( // Menampilkan date picker
                    onDismissRequest = {
                        onUIEvent(ThesisPlannerScreenUIEvent.OnDatePickerDismiss) // Menangani pembatalan date picker
                    },
                    onDateSelected = {
                        task = task.copy(dueDate = it) // Update tanggal jatuh tempo tugas
                    }
                )
            }
        }
    }

    if (state.articlePendingDelete != null) { // Cek apakah ada artikel yang menunggu konfirmasi penghapusan
        ConfirmationAlertDialog( // Menampilkan dialog konfirmasi penghapusan artikel
            title = stringResource(R.string.delete_article),
            message = stringResource(R.string.are_you_sure_you_want_to_delete_this_thesis),
            positiveButtonText = stringResource(R.string.yes),
            onPositiveButtonClick = {
                onUIEvent(ThesisPlannerScreenUIEvent.OnDeleteArticleConfirm(state.articlePendingDelete)) // Konfirmasi penghapusan artikel
                onUIEvent(ThesisPlannerScreenUIEvent.OnArticleDeleteDialogDismiss) // Sembunyikan dialog penghapusan artikel
            },
            negativeButtonText = stringResource(R.string.no),
            onNegativeButtonClick = {
                onUIEvent(ThesisPlannerScreenUIEvent.OnArticleDeleteDialogDismiss) // Batalkan penghapusan artikel
            },
            onDismissRequest = {
                onUIEvent(ThesisPlannerScreenUIEvent.OnArticleDeleteDialogDismiss) // Menangani penutupan dialog
            },
        )
    }

    if (state.taskPendingDelete != null) { // Cek apakah ada tugas yang menunggu konfirmasi penghapusan
        ConfirmationAlertDialog( // Menampilkan dialog konfirmasi penghapusan tugas
            title = stringResource(R.string.delete_task),
            message = stringResource(R.string.are_you_sure_you_want_to_delete_this_task),
            positiveButtonText = stringResource(R.string.yes),
            onPositiveButtonClick = {
                onUIEvent(ThesisPlannerScreenUIEvent.OnTaskDeleteConfirm(state.taskPendingDelete)) // Konfirmasi penghapusan tugas
                onUIEvent(ThesisPlannerScreenUIEvent.OnTaskDeleteDialogDismiss) // Sembunyikan dialog penghapusan tugas
            },
            negativeButtonText = stringResource(R.string.no),
            onNegativeButtonClick = {
                onUIEvent(ThesisPlannerScreenUIEvent.OnTaskDeleteDialogDismiss) // Batalkan penghapusan tugas
            },
            onDismissRequest = {
                onUIEvent(ThesisPlannerScreenUIEvent.OnTaskDeleteDialogDismiss) // Menangani penutupan dialog
            },
        )
    }
}