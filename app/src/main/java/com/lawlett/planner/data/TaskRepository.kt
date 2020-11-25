package com.lawlett.planner.data

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.Tasks
import com.lawlett.planner.data.room.TasksDao

class TaskRepository(private val tasksDao: TasksDao) {
    val readAllData:LiveData<List<Tasks>> = tasksDao.readAllData()
    fun loadCategory(text: String) =tasksDao.loadCategory(text)

 suspend fun addTask(tasks: Tasks){
        tasksDao.addTask(tasks)
    }
}