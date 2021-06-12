package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val categoryName: String,
    val taskAmount: Int,
    val categoryImage: Int
)