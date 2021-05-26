package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.lawlett.planner.data.room.models.Tasks
import com.lawlett.planner.data.room.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun addTask(task: Tasks) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun update(tasks: Tasks) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(tasks)
        }
    }
    fun delete(tasks: Tasks){
        viewModelScope.launch (Dispatchers.IO){
            repository.delete(tasks)
        }
    }
    fun updatePosition(tasks: List<Tasks>){
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePosition(tasks)
        }
    }

    fun getCategoryLiveData(category: String): LiveData<List<Tasks>> {
        return repository.loadCategoryLiveData(category)
    }

}

