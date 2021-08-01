package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.*
import com.lawlett.planner.data.room.repositories.AnswerRepository
import com.lawlett.planner.data.room.repositories.HabitRepository
import com.lawlett.planner.data.room.repositories.QuestionRepository
import com.lawlett.planner.data.room.repositories.StandUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnswerViewModel(private val repository: AnswerRepository) : ViewModel() {

    fun insertData(model: AnswerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(model)
        }
    }

    fun update(model: AnswerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: AnswerModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(model)
        }
    }

    fun updatePosition(model: List<AnswerModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getData(): LiveData<List<AnswerModel>> {
        return repository.getData()
    }
}