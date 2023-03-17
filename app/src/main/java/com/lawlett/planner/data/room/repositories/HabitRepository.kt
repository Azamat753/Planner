package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.HabitDao
import com.lawlett.planner.data.room.models.HabitModel

class HabitRepository(private val habitDao: HabitDao) {
    fun loadIdeas(): LiveData<List<HabitModel>> {
        return habitDao.getHabits()
    }

    suspend fun addIdea(habitModel: HabitModel) {
        habitDao.addHabit(habitModel)
    }

    suspend fun update(habitModel: HabitModel) {
        habitDao.update(habitModel)
    }

    suspend fun delete(habitModel: HabitModel) {
        habitDao.delete(habitModel)
    }

    suspend fun updatePosition(habitList: List<HabitModel>) {
        habitDao.updatePosition(habitList)
    }
}