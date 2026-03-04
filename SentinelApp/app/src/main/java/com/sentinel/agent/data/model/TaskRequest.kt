package com.sentinel.agent.data.model

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("agentId") val agentId: String,
    @SerializedName("agentName") val agentName: String,
    @SerializedName("taskDescription") val taskDescription: String,
    @SerializedName("actionType") val actionType: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("status") val status: String
)
