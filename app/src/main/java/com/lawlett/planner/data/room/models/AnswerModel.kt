package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer_table")
data class AnswerModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val answer:String,
)