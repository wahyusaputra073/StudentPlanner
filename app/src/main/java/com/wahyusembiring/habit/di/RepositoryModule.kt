package com.wahyusembiring.habit.di

import com.wahyusembiring.data.repository.AuthRepository
import com.wahyusembiring.data.repository.EventRepository
import com.wahyusembiring.data.repository.LecturerRepository
import com.wahyusembiring.data.repository.MainRepository
import com.wahyusembiring.data.repository.SubjectRepository
import com.wahyusembiring.data.repository.ThesisRepository
import com.wahyusembiring.data.repository.implementation.AuthRepositoryImpl
import com.wahyusembiring.data.repository.implementation.EventRepositoryImpl
import com.wahyusembiring.data.repository.implementation.LecturerRepositoryImpl
import com.wahyusembiring.data.repository.implementation.MainRepositoryImpl
import com.wahyusembiring.data.repository.implementation.SubjectRepositoryImpl
import com.wahyusembiring.data.repository.implementation.ThesisRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Modul Dagger-Hilt yang menyediakan binding antara interface dan implementasinya untuk berbagai repository
@Module
@InstallIn(SingletonComponent::class)  // Modul ini diinstal di SingletonComponent (komponen aplikasi)
abstract class RepositoryModule {

    // Mengikat EventRepositoryImpl dengan EventRepository
    @Binds
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    // Mengikat SubjectRepositoryImpl dengan SubjectRepository
    @Binds
    abstract fun bindSubjectRepository(impl: SubjectRepositoryImpl): SubjectRepository

    // Mengikat LecturerRepositoryImpl dengan LecturerRepository
    @Binds
    abstract fun bindLectureRepository(impl: LecturerRepositoryImpl): LecturerRepository

    // Mengikat ThesisRepositoryImpl dengan ThesisRepository
    @Binds
    abstract fun bindThesisRepository(impl: ThesisRepositoryImpl): ThesisRepository

    // Mengikat AuthRepositoryImpl dengan AuthRepository
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    // Mengikat MainRepositoryImpl dengan MainRepository
    @Binds
    abstract fun bindMainRepository(impl: MainRepositoryImpl): MainRepository
}
