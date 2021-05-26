package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.TasksDao
import com.lawlett.planner.data.room.models.Tasks

class TaskRepository(private val tasksDao: TasksDao) {
    fun loadCategoryLiveData(text: String): LiveData<List<Tasks>> {
        return tasksDao.loadCategoryLiveData(text)
    }

    suspend fun addTask(tasks: Tasks) {
        tasksDao.addTask(tasks)
    }

    suspend fun update(tasks: Tasks) {
        tasksDao.update(tasks)
    }
    suspend fun delete(tasks: Tasks){
        tasksDao.delete(tasks)
    }
    suspend fun updatePosition(tasks: List<Tasks>){
        tasksDao.updateWord(tasks)
    }
}