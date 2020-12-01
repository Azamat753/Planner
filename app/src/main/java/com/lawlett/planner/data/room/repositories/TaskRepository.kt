package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.models.Tasks
import com.lawlett.planner.data.room.dao.TasksDao

class TaskRepository(private val tasksDao: TasksDao) {
    fun loadCategory(text: String): LiveData<List<Tasks>> {
       return tasksDao.loadCategory(text)
   }

    suspend fun addTask(tasks: Tasks) {
        tasksDao.addTask(tasks)
    }
}