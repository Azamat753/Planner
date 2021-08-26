package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class TasksModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val category:String,
    val task: String,
    var isDone: Boolean,
    var doneAmount:Int?=null


) {
    override fun toString(): String {
        return "TasksModel(id=$id, category='$category', task='$task', isDone=$isDone, doneAmount=$doneAmount)"
    }
}