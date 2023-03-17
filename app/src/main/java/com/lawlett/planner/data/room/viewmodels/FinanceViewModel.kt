package com.lawlett.planner.data.room.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.repositories.FinanceRepository
import com.lawlett.planner.data.room.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinanceViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun addModel(model:FinanceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addModel(model)
        }
    }

    fun update(model:FinanceModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(model)
        }
    }
    fun delete(model:FinanceModel){
        viewModelScope.launch (Dispatchers.IO){
            repository.delete(model)
        }
    }
    fun updatePosition(model: List<FinanceModel>){
        viewModelScope.launch(Dispatchers.IO){
            repository.updatePosition(model)
        }
    }

    fun getCategory(category: String): LiveData<List<FinanceModel>> {
        return repository.loadCategory(category)
    }
    fun getData():LiveData<List<FinanceModel>>{
        return repository.loadData()
    }
}

