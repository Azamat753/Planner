package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timing_table")
data class Timing(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val timerTitle: String,
    val timerMinutes: Int,
    val timerDay:String,

    val stopwatch: String,
    val stopwatchMinutes: Int,
    val stopwatchDay: String
)