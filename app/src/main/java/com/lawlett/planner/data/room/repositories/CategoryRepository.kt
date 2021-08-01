package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.CategoryDao
import com.lawlett.planner.data.room.dao.IdeaDao
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.models.TasksModel

class CategoryRepository (private val categoryDao: CategoryDao){
    fun loadCategory():LiveData<List<CategoryModel>>{
        return categoryDao.getCategory()
    }

    fun loadCategoryByName(text: String):LiveData <CategoryModel> {
        return categoryDao.loadCategoryByName(text)
    }

    suspend fun addIdea(categoryModel: CategoryModel){
        categoryDao.addCategory(categoryModel)
    }
    suspend fun update(categoryModel: CategoryModel){
        categoryDao.update(categoryModel)
    }

    suspend fun delete(categoryModel: CategoryModel){
        categoryDao.delete(categoryModel)
    }
    suspend fun updatePosition(ideaList: List<CategoryModel>){
        categoryDao.updatePosition(ideaList)
    }
}