package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.TasksModel

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(tasksModel: TasksModel)

    @Query("SELECT * FROM tasks_table WHERE category=:category ")
    fun loadCategoryLiveData(category: String): LiveData<List<TasksModel>>

    @Update
    suspend fun update(tasksModel: TasksModel)

    @Delete
    suspend fun delete(tasksModel: TasksModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
   suspend fun updatePosition(tasks: List<TasksModel?>)
}