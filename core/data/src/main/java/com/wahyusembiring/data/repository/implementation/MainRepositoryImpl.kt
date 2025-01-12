package com.wahyusembiring.data.repository.implementation

import com.wahyusembiring.data.local.dao.ExamDao
import com.wahyusembiring.data.local.dao.HomeworkDao
import com.wahyusembiring.data.local.dao.LecturerDao
import com.wahyusembiring.data.local.dao.ReminderDao
import com.wahyusembiring.data.local.dao.SubjectDao
import com.wahyusembiring.data.local.dao.TaskDao
import com.wahyusembiring.data.local.dao.ThesisDao
import com.wahyusembiring.data.remote.ExamService
import com.wahyusembiring.data.remote.HomeworkService
import com.wahyusembiring.data.remote.LecturerService
import com.wahyusembiring.data.remote.ReminderService
import com.wahyusembiring.data.remote.SubjectService
import com.wahyusembiring.data.remote.ThesisService
import com.wahyusembiring.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.wahyusembiring.data.Result

class MainRepositoryImpl @Inject constructor(  // Implementasi dari MainRepository
    private val examService: ExamService,  // Layanan untuk mengakses data ujian
    private val examDao: ExamDao,  // DAO untuk operasi database pada data ujian
    private val homeworkService: HomeworkService,  // Layanan untuk mengakses data tugas
    private val homeworkDao: HomeworkDao,  // DAO untuk operasi database pada data tugas
    private val reminderService: ReminderService,  // Layanan untuk mengakses data pengingat
    private val reminderDao: ReminderDao,  // DAO untuk operasi database pada data pengingat
    private val subjectService: SubjectService,  // Layanan untuk mengakses data mata kuliah
    private val subjectDao: SubjectDao,  // DAO untuk operasi database pada data mata kuliah
    private val thesisService: ThesisService,  // Layanan untuk mengakses data tesis
    private val thesisDao: ThesisDao,  // DAO untuk operasi database pada data tesis
    private val taskDao: TaskDao,  // DAO untuk operasi database pada data tugas
    private val lecturerService: LecturerService,  // Layanan untuk mengakses data dosen
    private val lecturerDao: LecturerDao  // DAO untuk operasi database pada data dosen
) : MainRepository {

    override suspend fun syncToLocal(): Flow<Result<Unit>> = flow {  // Sinkronisasi data dari cloud ke lokal
        emit(Result.Loading())  // Menandakan bahwa proses sedang berlangsung
        try {
            // Mengambil data dosen dari service dan menghapus data lama dari database
            val id = lecturerService.getAllLecturer()
            lecturerDao.deleteLecturerById(id.toString())

            // Menyimpan data mata kuliah, tugas, ujian, pengingat, dan tesis beserta tugas terkait ke database lokal
            val subjects = subjectService.getAllSubject()
            subjectDao.insertSubject(subjects)

            val homeworks = homeworkService.getAllHomework()
            homeworkDao.insertHomework(homeworks)

            val exams = examService.getAllExam()
            examDao.insertExam(exams)

            val reminders = reminderService.getAllReminder()
            reminderDao.insertReminder(reminders)

            val thesisWithTask = thesisService.getAllThesisWithTask()
            thesisDao.insertThesis(thesisWithTask.map { it.thesis })
            taskDao.insertTask(thesisWithTask.flatMap { it.tasks })

            emit(Result.Success(Unit))  // Proses berhasil
        } catch (thr: Throwable) {  // Menangani kesalahan
            emit(Result.Error(thr))  // Menyampaikan kesalahan
        }
    }

    override suspend fun syncToCloud(): Flow<Result<Unit>> = flow {  // Sinkronisasi data dari lokal ke cloud
        emit(Result.Loading())  // Menandakan bahwa proses sedang berlangsung
        try {
            // Mengambil data dosen, mata kuliah, tugas, ujian, pengingat, dan tesis dari database lokal dan mengirimkannya ke cloud
            val lecturers = lecturerDao.getAllLecturer().first()
            lecturers.forEach { lecturerService.saveLecturer(it) }

            val subjects = subjectDao.getAllSubject().first()
            subjects.forEach { subjectService.saveSubject(it) }

            val homeworks = homeworkDao.getAllHomework().first()
            homeworks.forEach { homeworkService.saveHomework(it) }

            val exams = examDao.getAllExam().first()
            exams.forEach { examService.saveExam(it) }

            val reminders = reminderDao.getAllReminder().first()
            reminders.forEach { reminderService.saveReminder(it) }

            val thesisWithTask = thesisDao.getAllThesis().first()
            thesisWithTask.forEach { thesisService.saveThesisWithTask(it) }

            emit(Result.Success(Unit))  // Proses berhasil
        } catch (thr: Throwable) {  // Menangani kesalahan
            emit(Result.Error(thr))  // Menyampaikan kesalahan
        }
    }
}