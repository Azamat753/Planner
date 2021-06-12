package com.lawlett.planner.data.room.repositories

import androidx.lifecycle.LiveData
import com.lawlett.planner.data.room.dao.EventDao
import com.lawlett.planner.data.room.dao.IdeaDao
import com.lawlett.planner.data.room.models.EventModel
import com.lawlett.planner.data.room.models.IdeaModel

class EventRepository (private val dao: EventDao){
    fun loadData():LiveData<List<EventModel>>{
        return dao.getEvents()
    }

    suspend fun addData(model: EventModel){
        dao.addEvent(model)
    }
    suspend fun update(model: EventModel){
        dao.update(model)
    }

    suspend fun delete(model: EventModel){
        dao.delete(model)
    }
    suspend fun updatePosition(ideaList: List<EventModel>){
        dao.updatePosition(ideaList)
    }
}