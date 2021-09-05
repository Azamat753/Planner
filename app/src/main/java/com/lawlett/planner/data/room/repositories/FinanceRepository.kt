package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.FinanceDao
import com.lawlett.planner.data.room.dao.TasksDao
import com.lawlett.planner.data.room.models.FinanceModel
import com.lawlett.planner.data.room.models.TasksModel

class FinanceRepository(private val dao: FinanceDao) {

    fun loadCategory(text: String): LiveData<List<FinanceModel>> {
        return dao.loadCategory(text)
    }
    fun loadData():LiveData<List<FinanceModel>>{
        return dao.loadData()
    }

    suspend fun addModel(model:FinanceModel) {
        dao.addModel(model)
    }

    suspend fun update(model:FinanceModel) {
        dao.update(model)
    }
    suspend fun delete(model:FinanceModel){
        dao.delete(model)
    }
    suspend fun updatePosition(list: List<FinanceModel>){
        dao.updatePosition(list)
    }

}