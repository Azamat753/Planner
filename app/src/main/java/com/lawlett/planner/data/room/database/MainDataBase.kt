package com.lawlett.planner.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lawlett.planner.data.room.dao.*
import com.lawlett.planner.data.room.models.*

@Database(entities = [TasksModel::class, TimingModel::class,IdeaModel::class,HabitModel::class,CategoryModel::class,EventModel::class], version = 1, exportSchema = false)
abstract class MainDataBase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
    abstract fun timingDao(): TimingDao
    abstract fun ideaDao():IdeaDao
    abstract fun habitDao():HabitDao
    abstract fun categoryDao():CategoryDao
    abstract fun eventDao():EventDao

    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null

        fun getDatabase(context: Context): MainDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "task_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}