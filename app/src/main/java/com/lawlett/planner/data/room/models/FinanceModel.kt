package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "finance_table")
data class FinanceModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val category: String,
    val description: String,
    val amount: Int,
    val date: String? = null,
    val isIncome: Boolean?=null
):Serializable