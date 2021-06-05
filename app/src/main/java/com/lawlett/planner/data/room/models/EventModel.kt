package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_table")
data class EventModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val title:String,
    val date:String,
    val startTime:String,
    val endTime:String,
    val color:Int
)