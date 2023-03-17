package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.repositories.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {
    fun insertData(model: HabitModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addIdea(model)
        }
    }

    fun update(model: HabitModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: HabitModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(model)
        }
    }

    fun updatePosition(model: List<HabitModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getHabitsLiveData(): LiveData<List<HabitModel>> {
        return repository.loadIdeas()
    }
}