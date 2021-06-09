package com.lawlett.planner.di

import com.lawlett.planner.data.room.database.MainDataBase
import com.lawlett.planner.data.room.repositories.HabitRepository
import com.lawlett.planner.data.room.repositories.IdeaRepository
import com.lawlett.planner.data.room.repositories.TaskRepository
import com.lawlett.planner.data.room.viewmodels.HabitViewModel
import com.lawlett.planner.data.room.viewmodels.IdeaViewModel
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { TimingViewModel(get()) }
    viewModel { TaskViewModel(get()) }
    viewModel { IdeaViewModel(get()) }
    viewModel { HabitViewModel(get()) }

    factory { IdeaRepository(get()) }
    factory { TaskRepository(get()) }
    factory { HabitRepository(get()) }

    factory { MainDataBase.getDatabase(androidContext()).habitDao() }
    factory { MainDataBase.getDatabase(androidContext()).ideaDao() }
    factory { MainDataBase.getDatabase(androidContext()).taskDao() }
}