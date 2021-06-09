package com.lawlett.planner.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lawlett.planner.data.room.models.IdeaModel

@Dao
interface IdeaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addIdea(ideaModel: IdeaModel)

    @Query("SELECT * FROM ideas_table ")
    fun getIdeas(): LiveData<List<IdeaModel>>

    @Update
    suspend fun update(ideaModel: IdeaModel)

    @Delete
    suspend fun delete(ideaModel: IdeaModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePosition(ideas: List<IdeaModel?>)

}