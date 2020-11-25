package com.lawlett.planner.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.Tasks
import com.lawlett.planner.data.room.TasksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class TaskViewModel(application: Application):AndroidViewModel(application)  {
    var readAllData :LiveData<List<Tasks>>? = null
    private val repository:TaskRepository

    init {
        val taskDao=TasksDatabase.getDatabase(application).taskDao()
        repository= TaskRepository(taskDao)
    }
    fun addTask(task:Tasks){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTask(task)
        }
    }

    fun getCategory(category:String):LiveData<List<Tasks>> = repository.loadCategory(category)

//    fun loadCatgeory(text: String) = repository.loadCategory(text)
}