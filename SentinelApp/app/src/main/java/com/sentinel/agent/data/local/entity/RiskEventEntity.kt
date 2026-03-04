package com.sentinel.agent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "risk_events")
data class RiskEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val eventType: String,
    val riskDelta: Int,
    val currentRiskScore: Int,
    val reasoning: String,
    val isSynced: Boolean = false
)
