package com.wahyusembiring.habit.di

import android.app.Application
import com.wahyusembiring.data.local.Converter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Modul Dagger-Hilt yang menyediakan dependensi untuk utility classes seperti converter
@Module
@InstallIn(SingletonComponent::class)  // Modul ini diinstal di SingletonComponent (komponen aplikasi)
object UtilityModule {

    // Menyediakan instance Converter yang akan disuntikkan ke dependensi lainnya
    @Provides
    @Singleton  // Hanya ada satu instance Converter yang digunakan sepanjang aplikasi
    fun provideTypeConverter(application: Application): Converter {
        return Converter(application.applicationContext)
    }
}