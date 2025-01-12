package com.wahyusembiring.lecture.screen.addlecture

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wahyusembiring.lecture.R
import com.wahyusembiring.ui.component.emailinput.EmailInput
import com.wahyusembiring.ui.component.multiaddressinput.MultiAddressInput
import com.wahyusembiring.ui.component.officehourinput.OfficeHourInput
import com.wahyusembiring.ui.component.phonenumberinput.PhoneNumberInput
import com.wahyusembiring.ui.component.popup.alertdialog.confirmation.ConfirmationAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.error.ErrorAlertDialog
import com.wahyusembiring.ui.component.popup.alertdialog.information.InformationAlertDialog
import com.wahyusembiring.ui.component.profilepicturepicker.ProfilePicturePicker
import com.wahyusembiring.ui.component.websiteinput.WebsiteInput
import com.wahyusembiring.ui.theme.spacing

@Composable
fun AddLectureScreen(
    viewModel: AddLecturerScreenViewModel, // Menerima ViewModel untuk menangani logika dan state
    navController: NavController // Menerima NavController untuk navigasi antar layar
) {
    val state by viewModel.state.collectAsStateWithLifecycle() // Mengambil state dari ViewModel secara lifecycle-aware

    AddLectureScreen( // Memanggil composable AddLectureScreen yang berisi UI untuk layar ini
        state = state, // Mengirim state yang sudah diambil dari ViewModel
        onUIEvent = viewModel::onUIEvent, // Mengirimkan fungsi onUIEvent untuk menangani event UI
        navController = navController, // Mengirimkan NavController untuk navigasi
        navigateUp = { // Fungsi untuk kembali ke layar sebelumnya
            navController.navigateUp()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class) // Menandakan penggunaan API eksperimen Material3
@Composable
private fun AddLectureScreen(
    state: AddLecturerScreenUItate, // State untuk menyimpan data UI layar ini
    onUIEvent: (AddLecturerScreenUIEvent) -> Unit, // Fungsi untuk mengirimkan event UI ke ViewModel
    navController: NavController, // Digunakan untuk navigasi antar layar
    navigateUp: () -> Unit // Fungsi untuk navigasi kembali
) {

    val context = LocalContext.current // Mengambil context lokal untuk akses aplikasi atau sistem

    Scaffold( // Scaffold untuk membuat struktur dasar layar dengan top bar, konten utama, dsb.
        topBar = { // Membuat top bar dengan tombol navigasi dan tombol aksi (save/edit)
            TopAppBar(
                navigationIcon = { // Tombol kembali untuk navigasi
                    IconButton(
                        onClick = {
                            onUIEvent(AddLecturerScreenUIEvent.OnBackButtonClick(navController)) // Menangani aksi tombol kembali
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = { // Menambahkan tombol simpan/edit pada bagian title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onUIEvent(AddLecturerScreenUIEvent.OnSaveButtonClick) // Menangani klik tombol simpan/edit
                            }
                        ) {
                            if (state.isEditMode) {
                                Text(text = stringResource(R.string.edit)) // Tampilkan tombol edit jika dalam mode edit
                            } else {
                                Text(text = stringResource(R.string.save)) // Tampilkan tombol simpan jika dalam mode baru
                            }
                        }
                    }
                }
            )
        }
    ) { scaffoldPadding -> // Konten utama dari Scaffold
        LazyColumn( // Menggunakan LazyColumn untuk daftar elemen yang dapat digulir
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding) // Menambahkan padding agar konten tidak menempel ke tepi
        ) {
            item { // Item pertama: Pemilihan foto profil
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ProfilePicturePicker(
                        modifier = Modifier.size(100.dp),
                        imageUri = state.profilePictureUri, // Menampilkan foto profil
                        onImageSelected = {
                            onUIEvent(AddLecturerScreenUIEvent.OnProfilePictureSelected(it)) // Menangani aksi pilih gambar
                        }
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium)) // Spacer untuk jarak
            }
            item { // Item kedua: Input nama dosen
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.Medium),
                    value = state.name, // Menampilkan nama dosen
                    onValueChange = {
                        onUIEvent(AddLecturerScreenUIEvent.OnLecturerNameChange(it)) // Menangani perubahan nama dosen
                    },
                    label = {
                        Text(text = stringResource(R.string.lecture_name)) // Label untuk input nama dosen
                    }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
            }
            item { // Item ketiga: Input nomor telepon
                PhoneNumberInput(
                    phoneNumbers = state.phoneNumbers, // Menampilkan daftar nomor telepon
                    onNewPhoneNumber = {
                        onUIEvent(AddLecturerScreenUIEvent.OnNewPhoneNumber(it)) // Menangani penambahan nomor telepon baru
                    },
                    onDeletePhoneNumber = { phoneNumber ->
                        onUIEvent(AddLecturerScreenUIEvent.OnDeletePhoneNumber(phoneNumber)) // Menangani penghapusan nomor telepon
                    }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
            }
            item { // Item keempat: Input email
                EmailInput(
                    emails = state.emails, // Menampilkan daftar email
                    onNewEmail = {
                        onUIEvent(AddLecturerScreenUIEvent.OnNewEmail(it)) // Menangani penambahan email baru
                    },
                    onDeleteEmail = { email ->
                        onUIEvent(AddLecturerScreenUIEvent.OnDeleteEmail(email)) // Menangani penghapusan email
                    }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
            }
            item { // Item kelima: Input alamat
                MultiAddressInput(
                    addresses = state.addresses, // Menampilkan daftar alamat
                    onNewAddress = {
                        onUIEvent(AddLecturerScreenUIEvent.OnNewAddress(it)) // Menangani penambahan alamat baru
                    },
                    onDeleteAddress = { address ->
                        onUIEvent(AddLecturerScreenUIEvent.OnDeleteAddress(address)) // Menangani penghapusan alamat
                    }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
            }

            item { // Item keenam: Input website
                WebsiteInput(
                    websites = state.websites, // Menampilkan daftar website
                    onNewWebsiteAddClick = {
                        onUIEvent(AddLecturerScreenUIEvent.OnNewWebsite(it)) // Menangani penambahan website baru
                    },
                    onDeleteWebsite = { website ->
                        onUIEvent(AddLecturerScreenUIEvent.OnDeleteWebsite(website)) // Menangani penghapusan website
                    }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Large))
            }

            item { // Item ketujuh: Input jam kerja
                OfficeHourInput(
                    officeHours = state.officeHours, // Menampilkan daftar jam kerja
                    onNewOfficeHour = {
                        onUIEvent(AddLecturerScreenUIEvent.OnNewOfficeHour(it)) // Menangani penambahan jam kerja baru
                    },
                    onDeleteOfficeHour = { officeHour ->
                        onUIEvent(AddLecturerScreenUIEvent.OnDeleteOfficeHour(officeHour)) // Menangani penghapusan jam kerja
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.Medium))
            }

        }
    }
    // Dialog konfirmasi simpan data dosen
    if (state.showSaveConfirmationDialog) {
        ConfirmationAlertDialog(
            onPositiveButtonClick = {
                onUIEvent(AddLecturerScreenUIEvent.OnSaveConfirmationDialogConfirm) // Menangani klik tombol konfirmasi simpan
            },
            onNegativeButtonClick = {
                onUIEvent(AddLecturerScreenUIEvent.OnSaveConfirmationDialogCancel) // Menangani klik tombol pembatalan
            },
            onDismissRequest = {
                onUIEvent(AddLecturerScreenUIEvent.OnSaveConfirmationDialogDismiss) // Menangani dismiss dialog
            },
            title = stringResource(R.string.save_lecture),
            message = stringResource(R.string.are_you_sure_you_want_to_save_this_lecture),
            positiveButtonText = stringResource(R.string.save),
            negativeButtonText = stringResource(R.string.cancel),
        )
    }
    // Dialog info bahwa data dosen berhasil disimpan
    if (state.showLectureSavedDialog) {
        InformationAlertDialog(
            onButtonClicked = {
                onUIEvent(AddLecturerScreenUIEvent.OnLecturerSavedDialogDismiss) // Menangani klik tombol OK
                navigateUp() // Kembali ke layar sebelumnya setelah disimpan
            },
            buttonText = stringResource(id = R.string.ok),
            title = stringResource(R.string.lecture_saved),
            message = "",
            onDismissRequest = {
                onUIEvent(AddLecturerScreenUIEvent.OnLecturerSavedDialogDismiss) // Menangani dismiss dialog
            },
        )
    }
    // Dialog error jika terjadi kesalahan saat penyimpanan
    if (state.errorMessage != null) {
        ErrorAlertDialog(
            message = state.errorMessage.asString(), // Menampilkan pesan kesalahan
            buttonText = stringResource(R.string.ok),
            onDismissRequest = {
                onUIEvent(AddLecturerScreenUIEvent.OnErrorDialogDismiss) // Menangani dismiss dialog error
            }
        )
    }
}
