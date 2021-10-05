package com.lawlett.planner.ui.adapter

import android.view.View
import com.lawlett.planner.data.room.models.TasksModel

interface CheckListener {
    fun checkClick(model: TasksModel,position:Int)
    fun longClick(model: TasksModel,itemView:View)
}
