package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.AchievementDao
import com.lawlett.planner.data.room.models.AchievementModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AchievementRepository(private val achievementDao: AchievementDao) {

    fun getData(): LiveData<List<AchievementModel>> {
        return achievementDao.getData()
    }

     fun getCurrentLevel(): LiveData<AchievementModel> {
        return achievementDao.getCurrentLevel()
    }

    suspend fun insertData(model: AchievementModel) {
        achievementDao.insertData(model)
    }

    suspend fun update(model: AchievementModel) {
        achievementDao.update(model)
    }

    suspend fun delete(model: AchievementModel) {
        achievementDao.delete(model)
    }

    suspend fun updatePosition(list: List<AchievementModel?>) {
        achievementDao.updatePosition(list)
    }
}