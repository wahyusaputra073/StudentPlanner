package com.wahyusembiring.data.repository.implementation

import com.wahyusembiring.data.local.dao.ExamDao
import com.wahyusembiring.data.local.dao.HomeworkDao
import com.wahyusembiring.data.local.dao.ReminderDao
import com.wahyusembiring.data.model.ExamWithSubject
import com.wahyusembiring.data.model.HomeworkWithSubject
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.Task
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.data.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(  // Kelas EventRepositoryImpl yang mengimplementasikan EventRepository, menggunakan dependensi injection untuk DAO
    private val examDao: ExamDao,  // DAO untuk mengakses data exam
    private val homeworkDao: HomeworkDao,  // DAO untuk mengakses data homework
    private val reminderDao: ReminderDao,  // DAO untuk mengakses data reminder
) : EventRepository {  // Mengimplementasikan EventRepository

    override fun getAllEvent(): Flow<List<Any>> {  // Fungsi untuk mengambil semua event (homework, exam, reminder) dalam satu Flow
        val homeworkFlow = homeworkDao.getAllHomeworkWithSubject()  // Mengambil semua homework dengan subject
        val examFlow = examDao.getAllExamWithSubject()  // Mengambil semua exam dengan subject
        val reminderFlow = reminderDao.getAllReminder()  // Mengambil semua reminder
        return combine(homeworkFlow, examFlow, reminderFlow) { homework, exam, reminder ->  // Menggabungkan tiga Flow menjadi satu
            val events = mutableListOf<Any>()  // Membuat list mutable untuk menampung event
            events.addAll(homework)  // Menambahkan semua homework ke dalam list
            events.addAll(exam)  // Menambahkan semua exam ke dalam list
            events.addAll(reminder)  // Menambahkan semua reminder ke dalam list
            events  // Mengembalikan daftar lengkap event
        }
    }


    override fun getAllHomeworkWithSubject(  // Fungsi untuk mengambil semua homework dengan subject, bisa memfilter berdasarkan rentang tanggal
        minDate: Long?,  // Parameter tanggal mulai (opsional)
        maxDate: Long?  // Parameter tanggal akhir (opsional)
    ): Flow<List<HomeworkWithSubject>> {  // Mengembalikan Flow yang berisi daftar HomeworkWithSubject
        return if (minDate == null || maxDate == null) {  // Jika rentang tanggal tidak diberikan
            homeworkDao.getAllHomeworkWithSubject()  // Ambil semua homework dengan subject tanpa filter
        } else {
            homeworkDao.getAllHomeworkWithSubject(minDate, maxDate)  // Ambil homework dengan subject dalam rentang tanggal yang diberikan
        }
    }

    override fun getHomeworkById(id: Int): Flow<HomeworkWithSubject?> {  // Fungsi untuk mengambil homework berdasarkan ID
        return homeworkDao.getHomeworkById(id)  // Mengambil data homework dengan ID tertentu dari DAO
    }

    override suspend fun saveHomework(homework: Task): Long {  // Fungsi untuk menyimpan data homework
        return homeworkDao.insertHomework(homework)  // Menyimpan homework ke database dan mengembalikan ID yang dihasilkan
    }

    override suspend fun updateHomework(homework: Task) {  // Fungsi untuk memperbarui data homework
        homeworkDao.updateHomework(homework)  // Memperbarui data homework di database
    }

    override fun getAllExamWithSubject(): Flow<List<ExamWithSubject>> {  // Fungsi untuk mengambil semua exam dengan subject
        return examDao.getAllExamWithSubject()  // Mengambil semua exam dengan subject dari DAO
    }

    override suspend fun saveExam(exam: Exam): Long {  // Fungsi untuk menyimpan exam
        return examDao.insertExam(exam)  // Menyimpan exam ke database dan mengembalikan ID yang dihasilkan
    }

    override suspend fun updateExam(exam: Exam) {  // Fungsi untuk memperbarui data exam
        examDao.updateExam(exam)  // Memperbarui exam di database
    }

    override fun getAllReminder(minDate: Long?, maxDate: Long?): Flow<List<Agenda>> {  // Fungsi untuk mengambil semua reminder, bisa memfilter berdasarkan rentang tanggal
        return if (minDate == null || maxDate == null) {  // Jika rentang tanggal tidak diberikan
            reminderDao.getAllReminder()  // Ambil semua reminder tanpa filter
        } else {
            reminderDao.getAllReminder(minDate, maxDate)  // Ambil reminder dalam rentang tanggal yang diberikan
        }
    }


    override suspend fun saveReminder(reminder: Agenda): Long {  // Fungsi untuk menyimpan reminder
        return reminderDao.insertReminder(reminder)  // Menyimpan reminder ke database dan mengembalikan ID yang dihasilkan
    }

    override suspend fun updateReminder(reminder: Agenda) {  // Fungsi untuk memperbarui reminder
        reminderDao.updateReminder(reminder)  // Memperbarui reminder di database
    }

    override suspend fun deleteExam(exam: Exam) {  // Fungsi untuk menghapus exam
        examDao.deleteExam(exam)  // Menghapus exam dari database
    }

    override suspend fun deleteHomework(homework: Task) {  // Fungsi untuk menghapus homework
        homeworkDao.deleteHomework(homework)  // Menghapus homework dari database
    }

    override suspend fun deleteReminder(reminder: Agenda) {  // Fungsi untuk menghapus reminder
        reminderDao.deleteReminder(reminder)  // Menghapus reminder dari database
    }

    override fun getExamById(id: Int): Flow<ExamWithSubject?> {  // Fungsi untuk mengambil exam berdasarkan ID
        return examDao.getExamById(id)  // Mengambil exam berdasarkan ID dari database
    }

    override fun getReminderById(id: Int): Flow<Agenda?> {  // Fungsi untuk mengambil reminder berdasarkan ID
        return reminderDao.getReminderById(id)  // Mengambil reminder berdasarkan ID dari database
    }
}