package com.sentinel.agent.data.model

data class EmergencyLog(
    val _id: String?,
    val userId: String,
    val timestamp: String,
    val latitude: Double,
    val longitude: Double,
    val riskScore: Int,
    val movementData: String, // Stringified JSON
    val status: String,
    val audioFileUrl: String?,
    val txHash: String? = null,
    val blockNumber: Int? = null,
    val chainId: Int? = null
)
