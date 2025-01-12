package com.wahyusembiring.data.repository.implementation

import com.wahyusembiring.data.local.dao.SubjectDao
import com.wahyusembiring.data.model.SubjectWithExam
import com.wahyusembiring.data.model.SubjectWithExamAndHomework
import com.wahyusembiring.data.model.SubjectWithLecturer
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao,  // Menyuntikkan dependensi SubjectDao untuk akses data
) : SubjectRepository {

    // Mengambil semua data mata kuliah dari database
    override fun getAllSubject(): Flow<List<Subject>> {
        return subjectDao.getAllSubject()  // Mengambil daftar mata kuliah
    }

    // Mengambil data mata kuliah dengan informasi dosen berdasarkan ID mata kuliah
    override fun getSubjectWithLecturerById(id: Int): Flow<SubjectWithLecturer?> {
        val subjectWithLecturer = subjectDao.getSubjectWithLecturerById(id)
        return subjectWithLecturer  // Mengembalikan data mata kuliah dengan dosen yang mengajar
    }

    // Menyimpan data mata kuliah baru ke database
    override suspend fun saveSubject(subject: Subject) {
        subjectDao.insertSubject(subject)  // Menyimpan mata kuliah
    }

    // Menghapus data mata kuliah berdasarkan ID mata kuliah
    override suspend fun onDeleteSubject(subject: Subject) {
        subjectDao.deleteSubjectById(subject.id)  // Menghapus mata kuliah berdasarkan ID
    }

    // Memperbarui data mata kuliah yang sudah ada
    override suspend fun updateSubject(subject: Subject) {
        subjectDao.updateSubject(subject)  // Memperbarui data mata kuliah
    }

    // Mengambil semua mata kuliah yang terkait dengan ujian
    override fun getAllSubjectWithExam(): Flow<List<SubjectWithExam>> {
        return subjectDao.getAllSubjectWithExam()  // Mengambil data mata kuliah dengan ujian terkait
    }

    // Mengambil semua mata kuliah yang terkait dengan ujian dan tugas
    override fun getAllSubjectWithExamAndHomework(): Flow<List<SubjectWithExamAndHomework>> {
        return subjectDao.getSubjectWithExamAndHomework()  // Mengambil data mata kuliah dengan ujian dan tugas terkait
    }

    // Mengambil semua mata kuliah yang terkait dengan ujian dan tugas yang sudah dinilai
    override fun getAllSubjectWithExamAndHomework(scored: Boolean): Flow<List<SubjectWithExamAndHomework>> {
        return subjectDao.getSubjectWithExamAndHomework(scored)  // Mengambil data mata kuliah dengan ujian dan tugas yang sudah dinilai
    }
}