package com.wahyusembiring.data.repository.implementation

import com.wahyusembiring.data.local.dao.TaskDao
import com.wahyusembiring.data.local.dao.ThesisDao
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.data.model.entity.Thesis
import com.wahyusembiring.data.model.ThesisWithTask
import com.wahyusembiring.data.remote.ThesisService
import com.wahyusembiring.data.repository.ThesisRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThesisRepositoryImpl @Inject constructor(
    private val thesisDao: ThesisDao,  // Menggunakan DAO untuk mengakses data tesis
    private val taskDao: TaskDao,      // Menggunakan DAO untuk mengakses data tugas
) : ThesisRepository {

    // Mengambil semua tesis yang ada beserta tugas-tugas yang terkait
    override fun getAllThesis(): Flow<List<ThesisWithTask>> {
        return thesisDao.getAllThesis()  // Mengambil semua tesis dengan tugas terkait
    }

    // Mengambil tesis berdasarkan ID dan tugas-tugas yang terkait
    override fun getThesisById(id: Int): Flow<ThesisWithTask> {
        return thesisDao.getThesisById(id)  // Mengambil tesis berdasarkan ID dengan tugas terkait
    }

    // Menyimpan tesis baru ke dalam database
    override suspend fun saveNewThesis(thesis: Thesis): Long {
        return thesisDao.insertThesis(thesis)  // Menyimpan tesis baru
    }

    // Memperbarui informasi tesis yang sudah ada
    override suspend fun updateThesis(thesis: Thesis) {
        thesisDao.updateThesis(thesis)  // Memperbarui tesis yang ada
    }

    // Memperbarui judul tesis berdasarkan ID
    override suspend fun updateThesisTitleById(id: Int, title: String) {
        thesisDao.updateThesisTitleById(id, title)  // Memperbarui judul tesis berdasarkan ID
    }

    // Menghapus tesis dari database
    override suspend fun deleteThesis(thesis: Thesis) {
        thesisDao.deleteThesis(thesis)  // Menghapus tesis dari database
    }

    // Menyimpan tugas baru yang terkait dengan tesis
    override suspend fun addNewTask(task: Task): Long {
        return taskDao.insertTask(task)  // Menyimpan tugas baru
    }

    // Menghapus tugas yang terkait dengan tesis
    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)  // Menghapus tugas dari database
    }

    // Mengubah status tugas apakah sudah selesai atau belum
    override suspend fun changeTaskCompletedStatus(task: Task, isCompleted: Boolean) {
        taskDao.updateTask(task.copy(isCompleted = isCompleted))  // Memperbarui status tugas
    }
}