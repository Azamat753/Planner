package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.AchievementDao
import com.lawlett.planner.data.room.models.AchievementModel

class AchievementRepository(private val answerDao: AchievementDao) {

    fun getData(): LiveData<List<AchievementModel>> {
        return answerDao.getData()
    }

    suspend fun insertData(model: AchievementModel) {
        answerDao.insertData(model)
    }

    suspend fun update(model: AchievementModel) {
        answerDao.update(model)
    }

    suspend fun delete(model: AchievementModel) {
        answerDao.delete(model)
    }

    suspend fun updatePosition(list: List<AchievementModel?>) {
        answerDao.updatePosition(list)
    }
}