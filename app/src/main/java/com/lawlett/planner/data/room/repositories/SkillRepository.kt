package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.SkillDao
import com.lawlett.planner.data.room.dao.StandUpDao
import com.lawlett.planner.data.room.models.SkillModel
import com.lawlett.planner.data.room.models.StandUpModel

class SkillRepository(private val skillDao: SkillDao) {
    fun getData(): LiveData<List<SkillModel>> {
        return skillDao.getData()
    }

    suspend fun insertData(model: SkillModel) {
        skillDao.insertData(model)
    }

    suspend fun update(model: SkillModel) {
        skillDao.update(model)
    }

    suspend fun delete(model: SkillModel) {
        skillDao.delete(model)
    }

    suspend fun updatePosition(list: List<SkillModel?>) {
        skillDao.updatePosition(list)
    }
}