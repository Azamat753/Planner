package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.models.TasksModel

@Dao
interface FinanceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addModel(model:FinanceModel)

    @Query("SELECT * FROM finance_table WHERE category=:category ")
    fun loadCategory(category: String): LiveData<List<FinanceModel>>

    @Query("SELECT * FROM finance_table")
    fun loadData(): LiveData<List<FinanceModel>>

    @Update
    suspend fun update(model:FinanceModel)

    @Delete
    suspend fun delete(model:FinanceModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
   suspend fun updatePosition(tasks: List<FinanceModel?>)
}