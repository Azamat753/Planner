package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.DreamModel
import com.lawlett.planner.data.room.models.SkillModel

@Dao
interface DreamDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(model: DreamModel)

    @Update
    suspend fun update(model: DreamModel)

    @Query("SELECT * FROM dream_table")
    fun getData(): LiveData<List<DreamModel>>

    @Delete
    suspend fun delete(model: DreamModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(list: List<DreamModel?>)
}