package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.AnswerDao
import com.lawlett.planner.data.room.dao.QuestionDao
import com.lawlett.planner.data.room.dao.StandUpDao
import com.lawlett.planner.data.room.models.AnswerModel
import com.lawlett.planner.data.room.models.QuestionModel
import com.lawlett.planner.data.room.models.StandUpModel

class AnswerRepository(private val answerDao: AnswerDao) {

    fun getData(): LiveData<List<AnswerModel>> {
        return answerDao.getData()
    }

    suspend fun insertData(answerModel: AnswerModel) {
        answerDao.insertData(answerModel)
    }

    suspend fun update(answerModel: AnswerModel) {
        answerDao.update(answerModel)
    }

    suspend fun delete(answerModel: AnswerModel) {
        answerDao.delete(answerModel)
    }

    suspend fun updatePosition(list: List<AnswerModel?>) {
        answerDao.updatePosition(list)
    }
}