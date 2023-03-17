package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.models.TimetableModel
import com.lawlett.planner.data.room.repositories.TaskRepository
import com.lawlett.planner.data.room.repositories.TimetableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimetableViewModel(private val repository: TimetableRepository) : ViewModel() {

    fun addData(model:TimetableModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(model)
        }
    }

    fun update(model:TimetableModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }
    fun delete(model:TimetableModel){
        viewModelScope.launch (Dispatchers.IO){
            repository.delete(model)
        }
    }
    fun updatePosition(tasks: List<TimetableModel>){
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePosition(tasks)
        }
    }

    fun getDataOfDayOfWeek(dayOfWeek: String): LiveData<List<TimetableModel>> {
        return repository.loadCategoryLiveData(dayOfWeek)
    }
}

