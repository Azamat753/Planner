package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.models.TimetableModel

@Dao
interface TimetableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(timetableModel: TimetableModel)

    @Query("SELECT * FROM timetable_table WHERE dayOfWeek=:dayOfWeek ORDER BY startTime ASC ")
    fun loadDataByDayOfWeek(dayOfWeek: String): LiveData<List<TimetableModel>>

    @Update
    suspend fun update(timetableModel: TimetableModel)

    @Delete
    suspend fun delete(timetableModel: TimetableModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
   suspend fun updatePosition(tasks: List<TimetableModel?>)
}