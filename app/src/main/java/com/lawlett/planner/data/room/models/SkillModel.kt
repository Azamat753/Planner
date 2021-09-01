package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "skill_table")
data class SkillModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val skillName: String? = null,
    val hour: String? = "0",
    val dateCreated: String? = null
) : Serializable