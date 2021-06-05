package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.TasksDao
import com.lawlett.planner.data.room.models.TasksModel

class TaskRepository(private val tasksDao: TasksDao) {
    fun loadCategoryLiveData(text: String): LiveData<List<TasksModel>> {
        return tasksDao.loadCategoryLiveData(text)
    }

    suspend fun addTask(tasksModel: TasksModel) {
        tasksDao.addTask(tasksModel)
    }

    suspend fun update(tasksModel: TasksModel) {
        tasksDao.update(tasksModel)
    }
    suspend fun delete(tasksModel: TasksModel){
        tasksDao.delete(tasksModel)
    }
    suspend fun updatePosition(tasks: List<TasksModel>){
        tasksDao.updateWord(tasks)
    }
}