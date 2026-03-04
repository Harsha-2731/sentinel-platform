package com.sentinel.agent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * V3.1: Local Task Storage
 * Stores AI Agent activities directly in the Sentinel Agent's local database.
 */
@Entity(tableName = "agent_tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val agentName: String,
    val taskDescription: String,
    val actionType: String,
    val amount: Int,
    val status: String, // "blocked" or "allowed"
    val timestamp: Long,
    val previousHash: String?, // V34: Chain identifier
    val currentHash: String    // V34: Hash of current block
)
