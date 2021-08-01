package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.*

@Dao
interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(answerModel: AnswerModel)

    @Update
    suspend fun update(answerModel: AnswerModel)

    @Query("SELECT * FROM answer_table")
    fun getData(): LiveData<List<AnswerModel>>

    @Delete
    suspend fun delete(answerModel: AnswerModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(list: List<AnswerModel?>)
}