package com.wahyusembiring.lecture.screen.addlecture

import android.net.Uri
import androidx.navigation.NavController
import com.wahyusembiring.data.model.OfficeHour
import com.wahyusembiring.ui.util.UIText

// Data class yang menyimpan state dari layar AddLecturerScreen
data class AddLecturerScreenUItate(
    val isEditMode: Boolean = false, // Menyimpan status apakah dalam mode edit atau tidak
    val showSaveConfirmationDialog: Boolean = false, // Menyimpan status apakah dialog konfirmasi simpan ditampilkan
    val showLectureSavedDialog: Boolean = false, // Menyimpan status apakah dialog setelah data disimpan ditampilkan
    val errorMessage: UIText? = null, // Menyimpan pesan kesalahan, jika ada
    val name: String = "", // Menyimpan nama dosen
    val profilePictureUri: Uri? = null, // Menyimpan URI gambar profil dosen
    val phoneNumbers: List<String> = emptyList(), // Menyimpan daftar nomor telepon dosen
    val emails: List<String> = emptyList(), // Menyimpan daftar email dosen
    val addresses: List<String> = emptyList(), // Menyimpan daftar alamat dosen
    val officeHours: List<OfficeHour> = emptyList(), // Menyimpan daftar jam kerja dosen
    val websites: List<String> = emptyList() // Menyimpan daftar website dosen
)

// Sealed class untuk berbagai event UI pada layar AddLecturerScreen
sealed class AddLecturerScreenUIEvent {
    // Event ketika tombol kembali ditekan
    data class OnBackButtonClick(val navController: NavController) : AddLecturerScreenUIEvent()

    // Event ketika nama dosen berubah
    data class OnLecturerNameChange(val name: String) : AddLecturerScreenUIEvent()

    // Event ketika tombol simpan ditekan
    data object OnSaveButtonClick : AddLecturerScreenUIEvent()

    // Event ketika dialog "Lecture Saved" ditutup
    data object OnLecturerSavedDialogDismiss : AddLecturerScreenUIEvent()

    // Event ketika gambar profil dipilih
    data class OnProfilePictureSelected(val uri: Uri?) : AddLecturerScreenUIEvent()

    // Event untuk menangani penambahan atau penghapusan data lainnya (telepon, email, alamat, website, jam kerja)
    data class OnNewPhoneNumber(val phoneNumber: String) : AddLecturerScreenUIEvent()
    data class OnDeletePhoneNumber(val phoneNumber: String) : AddLecturerScreenUIEvent()
    data class OnDeleteEmail(val email: String) : AddLecturerScreenUIEvent()
    data class OnDeleteAddress(val address: String) : AddLecturerScreenUIEvent()
    data class OnDeleteWebsite(val website: String) : AddLecturerScreenUIEvent()
    data class OnNewEmail(val email: String) : AddLecturerScreenUIEvent()
    data class OnDeleteOfficeHour(val officeHour: OfficeHour) : AddLecturerScreenUIEvent()
    data class OnNewAddress(val address: String) : AddLecturerScreenUIEvent()
    data class OnNewOfficeHour(val officeHour: OfficeHour) : AddLecturerScreenUIEvent()
    data class OnNewWebsite(val website: String) : AddLecturerScreenUIEvent()

    // Event untuk menangani dialog konfirmasi simpan
    data object OnSaveConfirmationDialogDismiss : AddLecturerScreenUIEvent()
    data object OnSaveConfirmationDialogConfirm : AddLecturerScreenUIEvent()
    data object OnSaveConfirmationDialogCancel : AddLecturerScreenUIEvent()

    // Event untuk menangani dialog error
    data object OnErrorDialogDismiss : AddLecturerScreenUIEvent()
}
