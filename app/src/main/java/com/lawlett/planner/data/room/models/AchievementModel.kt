package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement_table")
data class AchievementModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val level:Int=0,
    val icon:String?=null,
    val achievement:String?=null
)