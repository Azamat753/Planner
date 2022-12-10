package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.TimetableDao
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.models.TimetableModel

class TimetableRepository(private val dao: TimetableDao) {

    fun loadCategoryLiveData(dayOfWeek: String): LiveData<List<TimetableModel>> {
        return dao.loadDataByDayOfWeek(dayOfWeek)
    }

    suspend fun addTask(timetableModel: TimetableModel) {
        dao.addData(timetableModel)
    }

    suspend fun update(timetableModel: TimetableModel) {
        dao.update(timetableModel)
    }
    suspend fun delete(timetableModel: TimetableModel){
        dao.delete(timetableModel)
    }
    suspend fun updatePosition(tasks: List<TimetableModel>){
        dao.updatePosition(tasks)
    }
}