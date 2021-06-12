package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.CategoryModel

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCategory(categoryModel: CategoryModel)

    @Query("SELECT * FROM category_table")
    fun getCategory(): LiveData<List<CategoryModel>>

    @Update
    suspend fun update(categoryModel: CategoryModel)

    @Delete
    suspend fun delete(categoryModel: CategoryModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(categoryModel: List<CategoryModel?>)

}