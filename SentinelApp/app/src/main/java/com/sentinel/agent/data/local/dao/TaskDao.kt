package com.sentinel.agent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sentinel.agent.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM agent_tasks ORDER BY timestamp DESC")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT currentHash FROM agent_tasks ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestTaskHash(): String?

    @Query("DELETE FROM agent_tasks WHERE timestamp < :expiryTime")
    suspend fun deleteOldTasks(expiryTime: Long)
}
