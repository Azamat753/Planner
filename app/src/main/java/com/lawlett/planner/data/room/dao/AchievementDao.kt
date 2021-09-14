package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.*

@Dao
interface AchievementDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(achievementModel: AchievementModel)

    @Update
    suspend fun update(achievementModel: AchievementModel)

    @Query("SELECT * FROM achievement_table")
    fun getData(): LiveData<List<AchievementModel>>

    @Query("SELECT * FROM achievement_table WHERE id=0")
     fun getCurrentLevel(): LiveData<AchievementModel>

    @Delete
    suspend fun delete(achievementModel: AchievementModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(list: List<AchievementModel?>)
}