package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.QuestionDao
import com.lawlett.planner.data.room.dao.StandUpDao
import com.lawlett.planner.data.room.models.QuestionModel
import com.lawlett.planner.data.room.models.StandUpModel

class QuestionRepository(private val questionDao: QuestionDao) {

    fun getData(): LiveData<List<QuestionModel>> {
        return questionDao.getData()
    }

    suspend fun insertData(questionModel: QuestionModel) {
        questionDao.insertData(questionModel)
    }

    suspend fun update(questionModel: QuestionModel) {
        questionDao.update(questionModel)
    }

    suspend fun delete(questionModel: QuestionModel) {
        questionDao.delete(questionModel)
    }

    suspend fun updatePosition(list: List<QuestionModel?>) {
        questionDao.updatePosition(list)
    }
}