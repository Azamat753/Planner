package com.lawlett.planner.di

import androidx.room.RoomDatabase
import com.lawlett.planner.data.room.TasksDatabase
import com.lawlett.planner.data.room.repositories.TaskRepository
import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule =module{
    viewModel{ TimingViewModel(get())}
    viewModel { TaskViewModel(get())}

    factory { TaskRepository(get()) }
    factory { TasksDatabase.getDatabase(androidContext()).taskDao()}
}