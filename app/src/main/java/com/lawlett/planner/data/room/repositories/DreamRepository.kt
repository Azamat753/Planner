package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.DreamDao
import com.lawlett.planner.data.room.dao.SkillDao
import com.lawlett.planner.data.room.models.DreamModel
import com.lawlett.planner.data.room.models.SkillModel

class DreamRepository(private val dao: DreamDao) {
    fun getData(): LiveData<List<DreamModel>> {
        return dao.getData()
    }

    suspend fun insertData(model: DreamModel) {
        dao.insertData(model)
    }

    suspend fun update(model: DreamModel) {
        dao.update(model)
    }

    suspend fun delete(model: DreamModel) {
        dao.delete(model)
    }

    suspend fun updatePosition(list: List<DreamModel?>) {
        dao.updatePosition(list)
    }
}