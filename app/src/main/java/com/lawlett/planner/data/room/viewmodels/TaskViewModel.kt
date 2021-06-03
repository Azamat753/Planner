package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun addTask(task: TasksModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun update(tasksModel: TasksModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(tasksModel)
        }
    }
    fun delete(tasksModel: TasksModel){
        viewModelScope.launch (Dispatchers.IO){
            repository.delete(tasksModel)
        }
    }
    fun updatePosition(tasks: List<TasksModel>){
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePosition(tasks)
        }
    }

    fun getCategoryLiveData(category: String): LiveData<List<TasksModel>> {
        return repository.loadCategoryLiveData(category)
    }

}

