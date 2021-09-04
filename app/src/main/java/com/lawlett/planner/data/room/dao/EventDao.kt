package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.EventModel

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEvent(eventModel: EventModel)

    @Query("SELECT * FROM events_table ")
    fun getEvents(): LiveData<List<EventModel>>

    @Update
    suspend fun update(eventModel: EventModel)

    @Delete
    suspend fun delete(eventModel: EventModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(ideas: List<EventModel?>)
}