package com.wahyusembiring.data.repository.implementation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.wahyusembiring.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "habit_preferences")  // Mendefinisikan DataStore untuk Preferences dengan nama "habit_preferences"

class DataStoreRepositoryImpl @Inject constructor(  // Implementasi dari DataStoreRepository
    private val appContext: Context  // Menggunakan Context aplikasi untuk akses DataStore
) : DataStoreRepository {

    private object PreferencesKey {  // Mendefinisikan kunci untuk Preferences
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")  // Kunci untuk status onboarding
    }

    private val dataStore = appContext.dataStore  // Menginisialisasi DataStore menggunakan context aplikasi

    override suspend fun saveOnBoardingState(completed: Boolean) {  // Fungsi untuk menyimpan status onboarding
        dataStore.edit { preferences ->  // Mengedit DataStore
            preferences[PreferencesKey.onBoardingKey] = completed  // Menyimpan status onboarding
        }
    }

    override fun readOnBoardingState(): Flow<Boolean> {  // Fungsi untuk membaca status onboarding
        return dataStore.data.map { preferences ->  // Mengambil data dari DataStore
            preferences[PreferencesKey.onBoardingKey] ?: false  // Mengembalikan status onboarding, default false
        }
    }
}
