package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "timetable_table")
data class TimetableModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val dayOfWeek: String,
    var startTime: String,
    var endTime: String? = null,
    val color: Int
):Serializable