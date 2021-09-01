package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.repositories.SkillRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SkillViewModel(private val repository: SkillRepository) : ViewModel() {
    var skillName: String? = null

    fun insertData(model: SkillModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(model)
        }
    }

    fun update(model: SkillModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: SkillModel?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (model != null) {
                repository.delete(model)
            }
        }
    }

    fun updatePosition(model: List<SkillModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getData(): LiveData<List<SkillModel>> {
        return repository.getData()
    }
}