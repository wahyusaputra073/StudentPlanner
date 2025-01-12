package com.wahyusembiring.lecture.screen.addlecture

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wahyusembiring.data.model.OfficeHour
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.repository.LecturerRepository
import com.wahyusembiring.lecture.R
import com.wahyusembiring.ui.util.UIText
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = AddLecturerScreenViewModel.Factory::class,
)
class AddLecturerScreenViewModel @AssistedInject constructor(
    private val lecturerRepository: LecturerRepository,
    private val application: Application,
    @Assisted private val lecturerId: Int = -1,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(lecturerId: Int = -1): AddLecturerScreenViewModel
    }

    private val _state = MutableStateFlow(AddLecturerScreenUItate()) // Menyimpan state untuk UI
    val state = _state.asStateFlow() // Menyediakan state untuk diakses oleh UI

    init {
        _state.update {
            it.copy(isEditMode = lecturerId != -1) // Mengatur mode edit jika lecturerId ada
        }
        if (lecturerId != -1) {
            viewModelScope.launch {
                lecturerRepository.getLecturerById(lecturerId).collect { lecturer ->
                    _state.update { // Mengupdate state dengan data lecturer yang sudah ada
                        it.copy(
                            profilePictureUri = lecturer?.photo,
                            name = lecturer?.name ?: it.name,
                            phoneNumbers = lecturer?.phone ?: it.phoneNumbers,
                            emails = lecturer?.email ?: it.emails,
                            addresses = lecturer?.address ?: it.addresses,
                            officeHours = lecturer?.officeHour ?: it.officeHours,
                            websites = lecturer?.website ?: it.websites
                        )
                    }
                }
            }
        }
    }

    fun onUIEvent(event: AddLecturerScreenUIEvent) {
        when (event) {
            is AddLecturerScreenUIEvent.OnBackButtonClick -> onBackButtonClick(event.navController) // Menangani klik tombol kembali
            is AddLecturerScreenUIEvent.OnSaveButtonClick -> onSaveButtonClick() // Menangani klik tombol simpan
            is AddLecturerScreenUIEvent.OnLecturerNameChange -> onLectureNameChange(event.name) // Menangani perubahan nama lecturer
            is AddLecturerScreenUIEvent.OnSaveConfirmationDialogCancel -> onSaveConfirmationDialogCancel() // Menangani pembatalan dialog konfirmasi simpan
            is AddLecturerScreenUIEvent.OnSaveConfirmationDialogConfirm -> onSaveConfirmationDialogConfirm() // Menangani konfirmasi simpan data
            is AddLecturerScreenUIEvent.OnSaveConfirmationDialogDismiss -> onSaveConfirmationDialogDismiss() // Menangani penutupan dialog simpan
            is AddLecturerScreenUIEvent.OnErrorDialogDismiss -> onErrorDialogDismiss() // Menangani penutupan dialog error
            is AddLecturerScreenUIEvent.OnProfilePictureSelected -> onProfilePictureSelected(event.uri) // Menangani pemilihan gambar profil
            is AddLecturerScreenUIEvent.OnNewPhoneNumber -> onNewPhoneNumber(event.phoneNumber) // Menangani penambahan nomor telepon baru
            is AddLecturerScreenUIEvent.OnDeletePhoneNumber -> onDeletePhoneNumber(event.phoneNumber) // Menangani penghapusan nomor telepon
            is AddLecturerScreenUIEvent.OnDeleteAddress -> onDeleteAddress(event.address) // Menangani penghapusan alamat
            is AddLecturerScreenUIEvent.OnDeleteWebsite -> onDeleteWebsite(event.website) // Menangani penghapusan website
            is AddLecturerScreenUIEvent.OnDeleteOfficeHour -> onDeleteOfficeHour(event.officeHour) // Menangani penghapusan jam kantor
            is AddLecturerScreenUIEvent.OnDeleteEmail-> onDeleteEmail(event.email) // Menangani penghapusan email
            is AddLecturerScreenUIEvent.OnNewEmail -> onNewEmail(event.email) // Menangani penambahan email baru
            is AddLecturerScreenUIEvent.OnNewAddress -> onNewAddress(event.address) // Menangani penambahan alamat baru
            is AddLecturerScreenUIEvent.OnNewOfficeHour -> onNewOfficeHour(event.officeHour) // Menangani penambahan jam kantor baru
            is AddLecturerScreenUIEvent.OnNewWebsite -> onNewWebsite(event.website) // Menangani penambahan website baru
            is AddLecturerScreenUIEvent.OnLecturerSavedDialogDismiss -> onLectureSavedDialogDismiss() // Menangani penutupan dialog setelah data disimpan
        }
    }

    private fun onLectureSavedDialogDismiss() {
        _state.update {
            it.copy(showLectureSavedDialog = false) // Menutup dialog setelah menyimpan
        }
    }

    private fun onNewWebsite(website: String) {
        _state.update {
            it.copy(websites = it.websites + website) // Menambah website baru ke dalam daftar
        }
    }

    private fun onNewOfficeHour(officeHour: OfficeHour) {
        _state.update {
            it.copy(officeHours = it.officeHours + officeHour) // Menambah jam kantor baru ke dalam daftar
        }
    }

    private fun onDeleteOfficeHour(officeHour: OfficeHour) {
        _state.update {
            it.copy(
                officeHours = it.officeHours.filter { existingOfficeHour ->
                    existingOfficeHour.day != officeHour.day ||
                            existingOfficeHour.startTime != officeHour.startTime ||
                            existingOfficeHour.endTime != officeHour.endTime // Menghapus jam kantor tertentu
                }
            )
        }
    }

    private fun onNewAddress(address: String) {
        _state.update {
            it.copy(addresses = it.addresses + address) // Menambah alamat baru ke dalam daftar
        }
    }

    private fun onNewEmail(email: String) {
        _state.update {
            it.copy(emails = it.emails + email) // Menambah email baru ke dalam daftar
        }
    }

    private fun onNewPhoneNumber(phoneNumber: String) {
        _state.update {
            it.copy(phoneNumbers = it.phoneNumbers + phoneNumber) // Menambah nomor telepon baru ke dalam daftar
        }
    }

    private fun onDeletePhoneNumber(phoneNumber: String) {
        _state.update {
            it.copy(
                phoneNumbers = it.phoneNumbers.filter { existingPhoneNumber ->
                    existingPhoneNumber != phoneNumber // Menghapus nomor telepon tertentu
                }
            )
        }
    }

    private fun onDeleteAddress(address: String) {
        _state.update {
            it.copy(
                addresses = it.addresses.filter { existingAddress ->
                    existingAddress != address // Menghapus alamat tertentu
                }
            )
        }
    }

    private fun onDeleteEmail(email: String) {
        _state.update {
            it.copy(
                emails = it.emails.filter { existingEmail ->
                    existingEmail != email // Menghapus email tertentu
                }
            )
        }
    }

    private fun onDeleteWebsite(website: String) {
        _state.update {
            it.copy(
                websites = it.websites.filter { existingWebsite ->
                    existingWebsite != website // Menghapus website tertentu
                }
            )
        }
    }

    private fun onProfilePictureSelected(uri: Uri?) {
        _state.update {
            it.copy(profilePictureUri = uri ?: it.profilePictureUri) // Menyimpan uri gambar profil yang dipilih
        }
    }

    private fun onErrorDialogDismiss() {
        _state.update {
            it.copy(errorMessage = null) // Menutup dialog error
        }
    }

    private fun onSaveConfirmationDialogConfirm() {
        onSaveConfirmationDialogDismiss() // Menutup dialog konfirmasi simpan
        try {
            val lecture = Lecturer(
                photo = _state.value.profilePictureUri.also {
                    if (it != null) {
                        application.applicationContext.contentResolver
                            .takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION) // Meminta izin untuk mengakses gambar profil
                    }
                },
                name = _state.value.name.ifBlank {
                    throw ValidationException(
                        UIText.StringResource(R.string.lecture_name_cannot_be_empty) // Validasi nama lecturer tidak boleh kosong
                    )
                },
                phone = _state.value.phoneNumbers,
                email = _state.value.emails,
                address = _state.value.addresses,
                officeHour = _state.value.officeHours,
                website = _state.value.websites,
            )
            viewModelScope.launch {
                if (_state.value.isEditMode) {
                    lecturerRepository.updateLecturer(lecture.copy(id = lecturerId)) // Memperbarui data lecturer jika dalam mode edit
                } else {
                    lecturerRepository.insertLecturer(lecture) // Menyimpan data lecturer baru
                }
                _state.update { it.copy(showLectureSavedDialog = true) } // Menampilkan dialog setelah data disimpan
            }
        } catch (validationException: ValidationException) {
            _state.update {
                it.copy(errorMessage = validationException.displayMessage) // Menampilkan pesan error jika validasi gagal
            }
        }
    }

    private fun onSaveConfirmationDialogDismiss() {
        _state.update {
            it.copy(showSaveConfirmationDialog = false) // Menutup dialog konfirmasi simpan
        }
    }

    private fun onSaveConfirmationDialogCancel() {
        _state.update {
            it.copy(showSaveConfirmationDialog = false) // Menutup dialog konfirmasi simpan jika dibatalkan
        }
    }

    private fun onLectureNameChange(name: String) {
        _state.update {
            it.copy(name = name) // Mengupdate nama lecturer
        }
    }

    private fun onSaveButtonClick() {
        _state.update {
            it.copy(showSaveConfirmationDialog = true) // Menampilkan dialog konfirmasi simpan
        }
    }

    private fun onBackButtonClick(navController: NavController) {
        navController.navigateUp() // Navigasi kembali
    }
}