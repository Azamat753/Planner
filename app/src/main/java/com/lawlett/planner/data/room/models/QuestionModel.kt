package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class QuestionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val question: String?=null,
)