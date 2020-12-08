package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lawlett.planner.data.room.models.Timing

@Dao
interface TimingDao {
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun addTask(timing: Timing)
//
//    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
//    fun readAllData(): LiveData<List<Timing>>

}