package com.lawlett.planner.data.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.Tasks
import com.lawlett.planner.data.room.TasksDatabase
import com.lawlett.planner.data.room.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    init {
        val taskDao = TasksDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
    }

    fun addTask(task: Tasks) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }

    }

    fun getCategory(category: String): LiveData<List<Tasks>> = repository.loadCategory(category)

}

