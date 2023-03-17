package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dream_table")
data class DreamModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val wokeUp: String,
    val fellAsleep: String,
    val sleptHour:String,
    val dream:String?=null,
    val dateCreated: String?,
    val color:Int
) : Serializable