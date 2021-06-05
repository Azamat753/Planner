package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.models.TimingModel
import com.lawlett.planner.data.room.dao.TimingDao

class TimingRepository(private val timingDao: TimingDao) {
    fun loadData():LiveData<List<TimingModel>>{
        return timingDao.readAllData()
    }
    suspend fun addTask(timingModel: TimingModel){
        timingDao.addTask(timingModel)
    }
}