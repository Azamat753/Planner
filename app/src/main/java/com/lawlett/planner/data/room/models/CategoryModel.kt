package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "category_table")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val categoryName: String? = null,
    var taskAmount: Int? = null,
    val categoryIcon: String? = null,
    val doneTaskAmount: Int = 0,
    val isBlock: Boolean = false
) : Serializable