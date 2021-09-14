package com.lawlett.planner.di

import com.lawlett.planner.data.room.database.MainDataBase
import com.lawlett.planner.data.room.repositories.*
import com.lawlett.planner.data.room.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { TaskViewModel(get()) }
    viewModel { IdeaViewModel(get()) }
    viewModel { HabitViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { EventViewModel(get()) }
    viewModel { StandUpViewModel(get()) }
    viewModel { AchievementViewModel(get()) }
    viewModel { SkillViewModel(get()) }
    viewModel { FinanceViewModel(get()) }
    viewModel { TimetableViewModel(get()) }

    factory { IdeaRepository(get()) }
    factory { TaskRepository(get()) }
    factory { HabitRepository(get()) }
    factory { CategoryRepository(get()) }
    factory { EventRepository(get()) }
    factory { StandUpRepository(get()) }
    factory { AchievementRepository(get()) }
    factory { SkillRepository(get()) }
    factory { FinanceRepository(get()) }
    factory { TimetableRepository(get()) }

    factory { MainDataBase.getDatabase(androidContext()).habitDao() }
    factory { MainDataBase.getDatabase(androidContext()).categoryDao() }
    factory { MainDataBase.getDatabase(androidContext()).ideaDao() }
    factory { MainDataBase.getDatabase(androidContext()).taskDao() }
    factory { MainDataBase.getDatabase(androidContext()).eventDao() }
    factory { MainDataBase.getDatabase(androidContext()).standUpDao() }
    factory { MainDataBase.getDatabase(androidContext()).achievementDao() }
    factory { MainDataBase.getDatabase(androidContext()).skillDao() }
    factory { MainDataBase.getDatabase(androidContext()).financeDao() }
    factory { MainDataBase.getDatabase(androidContext()).timetableDao() }
}