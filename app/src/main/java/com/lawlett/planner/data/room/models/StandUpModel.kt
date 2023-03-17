package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "standUp_table")
data class StandUpModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val whatDone: String,
    val whatPlan: String,
    val problems: String,
    val information: String?=null,
    val dateCreated: String
):Serializable