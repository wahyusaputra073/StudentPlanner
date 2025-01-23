package com.wahyusembiring.habit.di

import android.app.Application
import com.wahyusembiring.data.repository.DataStoreRepository
import com.wahyusembiring.data.repository.implementation.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Modul Dagger-Hilt untuk menyediakan dependensi terkait penyimpanan preferensi (DataStore)
@Module
@InstallIn(SingletonComponent::class)  // Modul ini diinstal di SingletonComponent (komponen aplikasi)
object PreferencesModule {

    // Menyediakan instance DataStoreRepository yang diimplementasikan oleh DataStoreRepositoryImpl
    @Provides
    @Singleton  // Menyatakan bahwa hanya ada satu instance DataStoreRepository selama siklus hidup aplikasi
    fun provideDataStoreRepository(application: Application): DataStoreRepository {
        // Mengembalikan instance DataStoreRepositoryImpl dengan menggunakan konteks aplikasi
        return DataStoreRepositoryImpl(application.applicationContext)
    }
}