package com.lawlett.planner.di

import com.lawlett.planner.data.room.viewmodels.TaskViewModel
import com.lawlett.planner.data.room.viewmodels.TimingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule =module{
    viewModel{ TimingViewModel(get()) }
    viewModel { TaskViewModel(get()) }
}