package com.lawlett.planner.data.room.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lawlett.planner.data.room.TasksDatabase
import com.lawlett.planner.data.room.models.Timing
import com.lawlett.planner.data.room.repositories.TimingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimingViewModel(application: Application):AndroidViewModel(application) {
    private val repository: TimingRepository
    init {
        val timingDao= TasksDatabase.getDatabase(application).timingDao()
        repository= TimingRepository(timingDao)
    }
    fun addTask(timing: Timing){
        viewModelScope.launch ( Dispatchers.IO ){
            repository.addTask(timing)
        }
    }
//    fun getData():LiveData<List<Timing>> =repository.loadData()
}
