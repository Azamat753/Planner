package com.lawlett.planner.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lawlett.planner.data.room.dao.*
import com.lawlett.planner.data.room.models.*

@Database(
    entities = [TasksModel::class,  IdeaModel::class, HabitModel::class,
        CategoryModel::class, EventModel::class, StandUpModel::class,  AchievementModel::class,SkillModel::class],
    version = 1,
    exportSchema = false
)
abstract class MainDataBase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
    abstract fun ideaDao(): IdeaDao
    abstract fun habitDao(): HabitDao
    abstract fun categoryDao(): CategoryDao
    abstract fun eventDao(): EventDao
    abstract fun standUpDao(): StandUpDao
    abstract fun achievementDao(): AchievementDao
    abstract fun skillDao(): SkillDao

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

                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}