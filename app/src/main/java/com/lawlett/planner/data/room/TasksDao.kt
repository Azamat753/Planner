package com.lawlett.planner.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(tasks: Tasks)

    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Tasks>>

    @Query("SELECT * FROM tasks_table WHERE category=:category ")
    fun loadCategory(category: String): LiveData<List<Tasks>>
}