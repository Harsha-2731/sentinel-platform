package com.sentinel.agent.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sentinel.agent.data.local.entity.RiskEventEntity

@Dao
interface RiskEventDao {
    @Insert
    suspend fun insertEvent(event: RiskEventEntity)

    @Query("SELECT * FROM risk_events ORDER BY timestamp DESC")
    suspend fun getAllEvents(): List<RiskEventEntity>

    @Query("SELECT * FROM risk_events WHERE isSynced = 0 ORDER BY timestamp ASC")
    suspend fun getUnsyncedEvents(): List<RiskEventEntity>

    @Query("UPDATE risk_events SET isSynced = 1 WHERE id IN (:eventIds)")
    suspend fun markAsSynced(eventIds: List<Int>)

    @Query("DELETE FROM risk_events WHERE timestamp < :expiryTime")
    suspend fun deleteOldEvents(expiryTime: Long)
}
