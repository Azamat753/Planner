package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ideas_table")
data class IdeaModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val image:String,
    val color: Int
)
