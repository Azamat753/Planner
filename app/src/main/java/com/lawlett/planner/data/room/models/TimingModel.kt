package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timing_table")
data class TimingModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val timerTitle: String?=null,
    val timerMinutes: Int?=null,
    val timerDay:String?=null,

    val stopwatch: String?=null,
    val stopwatchDay: String?=null
)