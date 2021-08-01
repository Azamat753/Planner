package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.models.TasksModel

@Dao
interface StandUpDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(standUpModel: StandUpModel)

    @Update
    suspend fun update(standUpModel: StandUpModel)

    @Query("SELECT * FROM standUp_table")
    fun getData(): LiveData<List<StandUpModel>>

    @Delete
    suspend fun delete(standUpModel: StandUpModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(list: List<StandUpModel?>)
}