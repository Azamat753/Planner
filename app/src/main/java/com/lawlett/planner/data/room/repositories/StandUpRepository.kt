package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.StandUpDao
import com.lawlett.planner.data.room.models.StandUpModel

class StandUpRepository(private val standUpDao: StandUpDao) {
    fun getData(): LiveData<List<StandUpModel>> {
        return standUpDao.getData()
    }

    suspend fun insertData(standUpModel: StandUpModel) {
        standUpDao.insertData(standUpModel)
    }

    suspend fun update(standUpModel: StandUpModel) {
        standUpDao.update(standUpModel)
    }

    suspend fun delete(standUpModel: StandUpModel) {
        standUpDao.delete(standUpModel)
    }

    suspend fun updatePosition(list: List<StandUpModel?>) {
        standUpDao.updatePosition(list)
    }
}