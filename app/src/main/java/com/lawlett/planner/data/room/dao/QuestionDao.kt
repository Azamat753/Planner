package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.models.QuestionModel
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.models.TasksModel

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(questionModel: QuestionModel)

    @Update
    suspend fun update(questionModel: QuestionModel)

    @Query("SELECT * FROM question_table")
    fun getData(): LiveData<List<QuestionModel>>

    @Delete
    suspend fun delete(questionModel: QuestionModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(list: List<QuestionModel?>)
}