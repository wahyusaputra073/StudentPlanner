package com.wahyusembiring.habit.di

import android.app.Application
import com.wahyusembiring.data.local.Converter
import com.wahyusembiring.data.local.MainDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Modul Dagger-Hilt untuk menyediakan dependensi terkait database lokal
@Module
@InstallIn(SingletonComponent::class)  // Menyatakan modul ini akan diinstal di SingletonComponent (komponen aplikasi)
object LocalDatabaseModule {

    // Menyediakan instance MainDatabase dengan Application dan Converter
    @Provides
    @Singleton  // Menyatakan bahwa instance ini hanya akan ada satu selama siklus hidup aplikasi
    fun provideMainDatabase(
        application: Application,  // Aplikasi untuk mengakses konteks aplikasi
        converter: Converter  // Konverter untuk mengubah data dalam database
    ): MainDatabase {
        return MainDatabase.getSingleton(
            appContext = application.applicationContext,  // Mengambil konteks aplikasi
            converter = converter  // Menggunakan konverter yang diberikan
        )
    }

    // Menyediakan DAO untuk masing-masing entitas dalam database

    // Menyediakan HomeworkDao dari MainDatabase
    @Provides
    @Singleton
    fun provideHomeworkDao(mainDatabase: MainDatabase) = mainDatabase.homeworkDao

    // Menyediakan ExamDao dari MainDatabase
    @Provides
    @Singleton
    fun provideExamDao(mainDatabase: MainDatabase) = mainDatabase.examDao

    // Menyediakan ReminderDao dari MainDatabase
    @Provides
    @Singleton
    fun provideReminderDao(mainDatabase: MainDatabase) = mainDatabase.reminderDao

    // Menyediakan SubjectDao dari MainDatabase
    @Provides
    @Singleton
    fun provideSubjectDao(mainDatabase: MainDatabase) = mainDatabase.subjectDao

    // Menyediakan TaskDao dari MainDatabase
    @Provides
    @Singleton
    fun provideTaskDao(mainDatabase: MainDatabase) = mainDatabase.taskDao

    // Menyediakan ThesisDao dari MainDatabase
    @Provides
    @Singleton
    fun provideThesisDao(mainDatabase: MainDatabase) = mainDatabase.thesisDao

    // Menyediakan LectureDao dari MainDatabase
    @Provides
    @Singleton
    fun provideLectureDao(mainDatabase: MainDatabase) = mainDatabase.lectureDao
}