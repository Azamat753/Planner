package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.models.Timing
import com.lawlett.planner.data.room.dao.TimingDao

class TimingRepository(private val timingDao: TimingDao) {
//    fun loadData():LiveData<List<Timing>>{
//        return timingDao.readAllData()
//    }
    suspend fun addTask(timing: Timing){
//        timingDao.addTask(timing)
    }
}