package com.wahyusembiring.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wahyusembiring.data.model.entity.Agenda
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder")
    fun getAllReminder(): Flow<List<Agenda>>

    @Query("SELECT * FROM reminder WHERE id = :id")
    fun getReminderById(id: Int): Flow<Agenda?>

    @Query(
        "SELECT * " +
                "FROM reminder " +
                "WHERE date >= :minDate AND date <= :maxDate " +
                "ORDER BY date ASC"
    )
    fun getAllReminder(minDate: Long, maxDate: Long): Flow<List<Agenda>>

    @Insert(entity = Agenda::class)
    suspend fun insertReminder(reminder: Agenda): Long

    @Insert(entity = Agenda::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminders: List<Agenda>): List<Long>

    @Update(entity = Agenda::class)
    suspend fun updateReminder(reminder: Agenda)

    @Delete(entity = Agenda::class)
    suspend fun deleteReminder(reminder: Agenda)

}