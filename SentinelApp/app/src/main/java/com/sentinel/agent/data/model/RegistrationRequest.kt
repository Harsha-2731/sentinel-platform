package com.sentinel.agent.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("riskPreference") val riskPreference: String
)
