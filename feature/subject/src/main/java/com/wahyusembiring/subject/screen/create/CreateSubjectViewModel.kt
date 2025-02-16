package com.wahyusembiring.subject.screen.create

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.LecturerRepository
import com.wahyusembiring.data.repository.SubjectRepository
import com.wahyusembiring.subject.R
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
    assistedFactory = CreateSubjectViewModel.Factory::class,
)
class CreateSubjectViewModel @AssistedInject constructor(  // Kelas ViewModel untuk CreateSubjectScreen, menggunakan dependency injection
    private val subjectRepository: SubjectRepository,  // Repository untuk mengelola data mata kuliah
    private val lecturerRepository: LecturerRepository,  // Repository untuk mengelola data pengajar
    @Assisted private val subjectId: Int  // ID mata kuliah yang diteruskan untuk mengedit
) : ViewModel() {  // ViewModel untuk menyimpan dan mengelola state UI

    @AssistedFactory  // Factory interface untuk membuat instance ViewModel dengan parameter subjectId
    interface Factory {
        fun create(subjectId: Int = -1): CreateSubjectViewModel  // Method untuk membuat ViewModel baru
    }

    private val _state = MutableStateFlow(CreateSubjectScreenUIState())  // State yang disimpan di ViewModel
    val state = _state.asStateFlow()  // State yang bisa diakses oleh UI

    init {  // Inisialisasi ViewModel, memeriksa apakah sedang dalam mode edit dan memuat data
        if (subjectId != -1) {  // Jika subjectId valid, berarti sedang dalam mode edit
            _state.update { it.copy(isEditMode = true) }
        }
        viewModelScope.launch {  // Memulai coroutine untuk mengambil data dari repository
            launch {  // Mengambil daftar pengajar dari lecturerRepository
                lecturerRepository.getAllLecturer().collect { lectures ->
                    _state.update { it.copy(lecturers = lectures) }  // Memperbarui daftar pengajar
                }
            }
            launch {  // Mengambil detail mata kuliah jika sedang dalam mode edit
                if (subjectId != -1) {
                    subjectRepository.getSubjectWithLecturerById(subjectId).collect { subjectWithLecturer ->
                        _state.update {
                            it.copy(
                                name = subjectWithLecturer?.subject?.name ?: "",
                                color = subjectWithLecturer?.subject?.color ?: Color.Unspecified,
                                room = subjectWithLecturer?.subject?.room ?: "",
                                description = subjectWithLecturer?.subject?.description ?: "",
                                primaryLecturer = subjectWithLecturer?.lecturer,  // Updated from lecturer to primaryLecturer
                                secondaryLecturer = subjectWithLecturer?.secondaryLecturer,  // Initialize secondary lecturer as null
                            )
                        }
                    }
                }
            }
        }
    }

    fun onUIEvent(event: CreateSubjectScreenUIEvent) {  // Menangani event dari UI
        when (event) {
            is CreateSubjectScreenUIEvent.OnSubjectNameChanged -> updateSubjectName(event.name)
            is CreateSubjectScreenUIEvent.OnRoomChanged -> updateRoom(event.room)
            is CreateSubjectScreenUIEvent.OnSaveButtonClicked -> onSaveButtonClicked()
            is CreateSubjectScreenUIEvent.OnPickColorButtonClicked -> onPickColorButtonClicked()
//            is CreateSubjectScreenUIEvent.OnLecturerSelected -> onLecturerSelected(event.lecturer)
            is CreateSubjectScreenUIEvent.OnColorPicked -> onColorPicked(event.color)
            is CreateSubjectScreenUIEvent.OnColorPickerDismiss -> onColorPickerDismiss()
            is CreateSubjectScreenUIEvent.OnErrorDialogDismiss -> onErrorDialogDismiss()
            is CreateSubjectScreenUIEvent.OnSaveConfirmationDialogConfirm -> onSaveConfirmationDialogConfirm()
            is CreateSubjectScreenUIEvent.OnSaveConfirmationDialogDismiss -> onSaveConfirmationDialogDismiss()
            is CreateSubjectScreenUIEvent.OnSubjectSavedDialogDismiss -> onSubjectSavedDialogDismiss()

            is CreateSubjectScreenUIEvent.OnPrimaryLecturerSelected -> onPrimaryLecturerSelected(event.lecturer)
            is CreateSubjectScreenUIEvent.OnSecondaryLecturerSelected -> onSecondaryLecturerSelected(event.lecturer)
            is CreateSubjectScreenUIEvent.OnLecturerRemoved -> onLecturerRemoved(event.isSecondary)
        }
    }


//    private fun onPrimaryLecturerSelected(lecturer: Lecturer) {
//        _state.update {
//            it.copy(
//                primaryLecturer = lecturer,
//                // Clear secondary lecturer if it's the same as the new primary
//                primaryLecturer = if (it.primaryLecturer?.id == lecturer.id) null else it.secondaryLecturer
//            )
//        }
//    }

    private fun onPrimaryLecturerSelected(lecturer: Lecturer) {
        _state.update { it.copy(primaryLecturer = lecturer) }
    }

    private fun onSecondaryLecturerSelected(lecturer: Lecturer) {
        _state.update { it.copy(secondaryLecturer = lecturer) }
    }

    private fun onLecturerRemoved(isSecondary: Boolean) {
        _state.update {
            if (isSecondary) {
                it.copy(secondaryLecturer = null)
            } else {
                it.copy(primaryLecturer = null)
            }
        }
    }




    private fun onSubjectSavedDialogDismiss() {  // Menangani penutupan dialog setelah mata kuliah disimpan
        _state.update { it.copy(showSubjectSavedDialog = false) }
    }

    private fun onSaveConfirmationDialogDismiss() {  // Menangani penutupan dialog konfirmasi simpan
        _state.update { it.copy(showSaveConfirmationDialog = false) }
    }

    private fun onErrorDialogDismiss() {  // Menangani penutupan dialog error
        _state.update { it.copy(errorMessage = null) }
    }

    private fun onColorPickerDismiss() {  // Menangani penutupan dialog pemilih warna
        _state.update { it.copy(showColorPicker = false) }
    }

    private fun onColorPicked(color: Color) {  // Menangani pemilihan warna
        _state.update { it.copy(color = color) }
    }

//    private fun onLecturerSelected(lecturer: Lecturer) {  // Menangani pemilihan pengajar
//        _state.update { it.copy(lecturer = lecturer) }
//    }

    private fun onPickColorButtonClicked() {  // Menangani klik tombol pemilihan warna
        _state.update { it.copy(showColorPicker = true) }
    }

    private fun onSaveButtonClicked() {  // Menangani klik tombol simpan
        _state.update { it.copy(showSaveConfirmationDialog = true) }
    }

    private fun onSaveConfirmationDialogConfirm() {  // Menangani konfirmasi simpan
        _state.update { it.copy(showSavingLoading = true) }
        viewModelScope.launch {  // Menyimpan mata kuliah secara aman dalam coroutine
            saveSubjectSafely()
        }
    }

    private suspend fun saveSubjectSafely() {  // Fungsi untuk menyimpan mata kuliah dengan penanganan exception
        try {
            saveSubject()  // Menyimpan mata kuliah
            _state.update {
                it.copy(
                    showSavingLoading = false,  // Menyembunyikan loading
                    showSubjectSavedDialog = true  // Menampilkan dialog mata kuliah berhasil disimpan
                )
            }
        } catch (e: MissingRequiredFieldException) {  // Menangani jika ada field yang kurang
            handleMissingFieldException(e)
        }
    }

    private suspend fun saveSubject() {
        val subject = Subject(
            name = _state.value.name.ifBlank { throw MissingRequiredFieldException.SubjectName() },
            color = _state.value.color,
            room = _state.value.room.ifBlank { throw MissingRequiredFieldException.Room() },
            description = _state.value.description,
            lecturerId = _state.value.primaryLecturer?.id ?: throw MissingRequiredFieldException.Lecture(),
            secondaryLecturerId = _state.value.secondaryLecturer?.id
        )

        if (state.value.isEditMode) {
            subjectRepository.updateSubject(subject.copy(id = subjectId))
        } else {
            subjectRepository.saveSubject(subject)
        }
    }

    private fun handleMissingFieldException(e: MissingRequiredFieldException) {  // Menangani exception jika ada field yang tidak diisi
        _state.update { it.copy(showSavingLoading = false) }
        val errorMessage = when (e) {
            is MissingRequiredFieldException.SubjectName -> UIText.StringResource(R.string.subject_name_is_required)  // Pesan error untuk nama
            is MissingRequiredFieldException.Room -> UIText.StringResource(R.string.room_is_required)  // Pesan error untuk ruang
            is MissingRequiredFieldException.Lecture -> UIText.StringResource(R.string.please_select_a_lecture)  // Pesan error untuk pengajar
            is MissingRequiredFieldException.Lecture2 -> UIText.StringResource(R.string.please_select_a_lecture)
        }
        _state.update { it.copy(errorMessage = errorMessage) }
    }

    private fun updateSubjectName(name: String) {  // Memperbarui nama mata kuliah
        _state.update { it.copy(name = name) }
    }

    private fun updateRoom(room: String) {  // Memperbarui ruang mata kuliah
        _state.update { it.copy(room = room) }
    }
}