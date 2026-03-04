package com.sentinel.agent.data.model

data class User(
    val _id: String,
    val name: String,
    val email: String,
    val deviceId: String,
    val trustedContacts: List<String>,
    val riskPreference: String,
    val token: String? = null,
    val hmacSecret: String? = null
)
