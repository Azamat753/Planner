package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.HabitModel

@Dao
interface HabitDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHabit(habitModel: HabitModel)

    @Query("SELECT * FROM habit_table")
    fun getHabits(): LiveData<List<HabitModel>>

    @Update
    suspend fun update(habitModel: HabitModel)

    @Delete
    suspend fun delete(habitModel: HabitModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(ideas: List<HabitModel>)

}