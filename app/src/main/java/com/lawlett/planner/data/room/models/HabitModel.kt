package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "habit_table")
data class HabitModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val icon: String,
    val allDays: String,
    val currentDay:Int=0,
    val myDay:Int=-1,
    val remindTime:String?=null
) : Serializable