package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.repositories.IdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IdeaViewModel(private val repository: IdeaRepository) : ViewModel() {
    fun addIdea(idea: IdeaModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addIdea(idea)
        }
    }

    fun update(idea: IdeaModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(idea)
        }
    }

    fun delete(idea: IdeaModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(idea)
        }
    }

    fun updatePosition(ideas: List<IdeaModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePosition(ideas)
        }
    }

    fun getIdeasLiveData(): LiveData<List<IdeaModel>> {
        return repository.loadIdeas()
    }
}
