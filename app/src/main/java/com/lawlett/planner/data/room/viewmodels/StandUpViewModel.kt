package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.repositories.StandUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StandUpViewModel(private val repository: StandUpRepository) : ViewModel() {

    fun insertData(model: StandUpModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(model)
        }
    }

    fun update(model: StandUpModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: StandUpModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(model)
        }
    }

    fun updatePosition(model: List<StandUpModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getData(): LiveData<List<StandUpModel>> {
        return repository.getData()
    }
}