package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.IdeaDao
import com.lawlett.planner.data.room.models.IdeaModel

class IdeaRepository (private val ideaDao: IdeaDao){
    fun loadIdeas():LiveData<List<IdeaModel>>{
        return ideaDao.getIdeas()
    }

    suspend fun addIdea(ideaModel: IdeaModel){
        ideaDao.addIdea(ideaModel)
    }
    suspend fun update(ideaModel: IdeaModel){
        ideaDao.update(ideaModel)
    }

    suspend fun delete(ideaModel: IdeaModel){
        ideaDao.delete(ideaModel)
    }
    suspend fun updatePosition(ideaList: List<IdeaModel>){
        ideaDao.updatePosition(ideaList)
    }
}