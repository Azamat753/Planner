package com.lawlett.planner.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ThemeModel(
    val colorText:String,
    var colorAttr:Int
)