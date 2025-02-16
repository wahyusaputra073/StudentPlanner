package com.wahyusembiring.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wahyusembiring.data.local.dao.ExamDao
import com.wahyusembiring.data.local.dao.HomeworkDao
import com.wahyusembiring.data.local.dao.LecturerDao
import com.wahyusembiring.data.local.dao.ReminderDao
import com.wahyusembiring.data.local.dao.SubjectDao
import com.wahyusembiring.data.local.dao.TaskDao
import com.wahyusembiring.data.local.dao.ThesisDao
import com.wahyusembiring.data.model.entity.Exam
import com.wahyusembiring.data.model.entity.TaskThesis
import com.wahyusembiring.data.model.entity.Lecturer
import com.wahyusembiring.data.model.entity.Agenda
import com.wahyusembiring.data.model.entity.Subject
import com.wahyusembiring.data.model.entity.Thesis
import com.wahyusembiring.data.model.entity.Task

@Database(  // Menandai kelas ini sebagai database Room
    entities = [  // Entitas yang termasuk dalam database
        TaskThesis::class,
        Subject::class,
        Lecturer::class,
        Exam::class,
        Agenda::class,
        Thesis::class,
        Task::class
    ],
    version = 1,  // Versi database
    exportSchema = false  // Tidak mengekspor schema ke file
)

@TypeConverters(Converter::class)  // Menandai kelas ini untuk menggunakan konverter tipe data kustom
abstract class MainDatabase : RoomDatabase() {  // Kelas utama database Room

    // DAO (Data Access Object) untuk masing-masing entitas
    abstract val homeworkDao: HomeworkDao
    abstract val subjectDao: SubjectDao
    abstract val examDao: ExamDao
    abstract val reminderDao: ReminderDao
    abstract val thesisDao: ThesisDao
    abstract val taskDao: TaskDao
    abstract val lectureDao: LecturerDao

    companion object {  // Companion object untuk menyimpan instance singleton
        private const val DATABASE_NAME = "habit.db"  // Nama database

        @Volatile  // Menjamin visibilitas instance di berbagai thread
        private var INSTANCE: MainDatabase? = null

        // Fungsi untuk mendapatkan instance singleton database
        fun getSingleton(
            appContext: Context,  // Context aplikasi
            converter: Converter  // Converter untuk konversi tipe data kustom
        ): MainDatabase {
            // Mengambil instance jika sudah ada, jika belum buat dan simpan
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    appContext,
                    MainDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration(true)  // Hapus dan buat ulang database jika migrasi gagal
                    .addTypeConverter(converter)  // Menambahkan konverter untuk tipe data kustom
                    .build().also { INSTANCE = it }  // Bangun dan set instance singleton
            }
        }
    }
}
