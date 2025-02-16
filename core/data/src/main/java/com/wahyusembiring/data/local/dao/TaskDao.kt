package com.wahyusembiring.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wahyusembiring.data.model.entity.TaskThesis
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM thesis_task")
    fun getAllTasks(): Flow<List<TaskThesis>>

    @Insert(entity = TaskThesis::class)
    suspend fun insertTask(taskThesis: TaskThesis): Long

    @Insert(entity = TaskThesis::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(taskTheses: List<TaskThesis>): List<Long>

    @Update(entity = TaskThesis::class)
    suspend fun updateTask(taskThesis: TaskThesis)

    @Delete(entity = TaskThesis::class)
    suspend fun deleteTask(taskThesis: TaskThesis)

}