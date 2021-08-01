package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.models.QuestionModel
import com.lawlett.planner.data.room.models.StandUpModel
import com.lawlett.planner.data.room.repositories.HabitRepository
import com.lawlett.planner.data.room.repositories.QuestionRepository
import com.lawlett.planner.data.room.repositories.StandUpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionViewModel(private val repository: QuestionRepository) : ViewModel() {

    fun insertData(model: QuestionModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(model)
        }
    }

    fun update(model: QuestionModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }

    fun delete(model: QuestionModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(model)
        }
    }

    fun updatePosition(model: List<QuestionModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(model)
        }
    }

    fun getData(): LiveData<List<QuestionModel>> {
        return repository.getData()
    }
}