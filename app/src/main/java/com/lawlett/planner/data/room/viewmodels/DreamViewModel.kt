package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.DreamModel
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.repositories.DreamRepository
import com.lawlett.planner.data.room.repositories.SkillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DreamViewModel(private val repository: DreamRepository) : ViewModel() {
    var skillName: String? = null

    fun insertData(model: DreamModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(model)
        }
    }

    fun update(model: DreamModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: DreamModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (model != null) {
                repository.delete(model)
            }
        }
    }

    fun updatePosition(model: List<DreamModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getData(): LiveData<List<DreamModel>> {
        return repository.getData()
    }
}