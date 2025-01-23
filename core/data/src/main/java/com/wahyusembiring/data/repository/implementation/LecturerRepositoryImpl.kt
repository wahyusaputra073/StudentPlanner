package com.wahyusembiring.data.repository.implementation

import com.wahyusembiring.data.local.dao.LecturerDao
import com.wahyusembiring.data.local.dao.SubjectDao
import com.wahyusembiring.data.model.LecturerWithSubject
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.remote.LecturerService
import com.wahyusembiring.data.repository.LecturerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LecturerRepositoryImpl @Inject constructor(  // Implementasi dari LecturerRepository
    private val lecturerDao: LecturerDao,  // Menggunakan LecturerDao untuk akses data
) : LecturerRepository {

    override fun getAllLecturer(): Flow<List<Lecturer>> {  // Mengambil semua data dosen
        return lecturerDao.getAllLecturer()  // Mengambil data dosen dari DAO
    }

    override fun getAllLecturerWithSubjects(): Flow<List<LecturerWithSubject>> {  // Mengambil dosen beserta mata kuliah yang diajarkan
        return lecturerDao.getAllLecturerWithSubject()  // Mengambil data dosen dan mata kuliah dari DAO
    }

    override suspend fun insertLecturer(lecturer: Lecturer): Long {  // Menyimpan data dosen baru
        return lecturerDao.insertLecturer(lecturer)  // Memasukkan dosen ke dalam database melalui DAO
    }

    override fun getLecturerById(id: Int): Flow<Lecturer?> {  // Mengambil data dosen berdasarkan ID
        return lecturerDao.getLecturerById(id)  // Mengambil dosen berdasarkan ID dari DAO
    }

    override suspend fun updateLecturer(lecturer: Lecturer) {  // Mengupdate data dosen
        lecturerDao.updateLecturer(lecturer)  // Mengupdate dosen di database melalui DAO
    }

    override suspend fun deleteLecturer(id: Int) {  // Menghapus dosen berdasarkan ID
        lecturerDao.deleteLecturerById(id.toString())  // Menghapus dosen dari database berdasarkan ID
    }

    override suspend fun deletePhoneNumber(phoneNumber: String) {  // Menghapus nomor telepon dosen
        lecturerDao.deletePhoneNumber(phoneNumber)  // Menghapus nomor telepon dosen dari database melalui DAO
    }
}