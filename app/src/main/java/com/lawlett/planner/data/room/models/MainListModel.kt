package com.lawlett.planner.data.room.models

data class MainListModel(
    val listIdea: List<IdeaModel>?=null,
    val listHabit: List<HabitModel>?=null,
    val listEvent: List<EventModel>?=null,
    val listTask: List<CategoryModel>?=null
)
