package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.SkillModel

@Dao
interface SkillDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(skillModel: SkillModel)

    @Update
    suspend fun update(skillModel: SkillModel)

    @Query("SELECT * FROM skill_table")
    fun getData(): LiveData<List<SkillModel>>

    @Delete
    suspend fun delete(skillModel: SkillModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(list: List<SkillModel?>)
}