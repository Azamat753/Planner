package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.Tasks

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(tasks: Tasks)

    @Query("SELECT * FROM tasks_table WHERE category=:category ")
    fun loadCategoryLiveData(category: String): LiveData<List<Tasks>>

    @Update
    suspend fun update(tasks: Tasks)
}