package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lawlett.planner.data.room.models.Tasks

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(tasks: Tasks)

    @Query("SELECT * FROM tasks_table WHERE category=:category ")
     fun loadCategory(category: String): LiveData<List<Tasks>>
}