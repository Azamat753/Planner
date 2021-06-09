package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lawlett.planner.data.room.models.TimingModel

@Dao
interface TimingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(timingModel: TimingModel)

    @Query("SELECT * FROM tasks_table")
    fun readAllData(): LiveData<List<TimingModel>>

}