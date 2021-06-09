package com.lawlett.planner.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lawlett.planner.data.room.dao.HabitDao
import com.lawlett.planner.data.room.dao.IdeaDao
import com.lawlett.planner.data.room.dao.TasksDao
import com.lawlett.planner.data.room.dao.TimingDao
import com.lawlett.planner.data.room.models.HabitModel
import com.lawlett.planner.data.room.models.IdeaModel
import com.lawlett.planner.data.room.models.TasksModel
import com.lawlett.planner.data.room.models.TimingModel

@Database(entities = [TasksModel::class, TimingModel::class,IdeaModel::class,HabitModel::class], version = 1, exportSchema = false)
abstract class MainDataBase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
    abstract fun timingDao(): TimingDao
    abstract fun ideaDao():IdeaDao
    abstract fun habitDao():HabitDao

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