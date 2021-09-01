package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.*
import com.lawlett.planner.data.room.repositories.AchievementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AchievementViewModel(private val repository: AchievementRepository) : ViewModel() {

    fun insertData(model: AchievementModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(model)
        }
    }

    fun update(model: AchievementModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: AchievementModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(model)
        }
    }

    fun updatePosition(model: List<AchievementModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getData(): LiveData<List<AchievementModel>> {
        return repository.getData()
    }
}