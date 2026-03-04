package com.sentinel.agent.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sentinel.agent.data.local.dao.RiskEventDao
import com.sentinel.agent.data.local.dao.TaskDao
import com.sentinel.agent.data.local.entity.RiskEventEntity
import com.sentinel.agent.data.local.entity.TaskEntity

@Database(entities = [RiskEventEntity::class, TaskEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun riskEventDao(): RiskEventDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sentinel_database"
                )
                .fallbackToDestructiveMigration() // V35: Auto-wipe on schema change for POC
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
