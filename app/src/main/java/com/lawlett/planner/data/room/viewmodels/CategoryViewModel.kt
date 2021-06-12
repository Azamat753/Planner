package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.CategoryModel
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.repositories.CategoryRepository
import com.lawlett.planner.data.room.repositories.IdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {
    fun addCategory(categoryModel: CategoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addIdea(categoryModel)
        }
    }

    fun update(categoryModel: CategoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(categoryModel)
        }
    }

    fun delete(categoryModel: CategoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(categoryModel)
        }
    }

    fun updatePosition(categoryModel: List<CategoryModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(categoryModel)
        }
    }

    fun getCategoryLiveData(): LiveData<List<CategoryModel>> {
        return repository.loadCategory()
    }
}
