package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_table")
data class HabitModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val image: Int,
    val allDays: String,
    val currentDay:String,
    val myDay:String
)