package com.sentinel.agent.network.api

import com.sentinel.agent.data.model.EmergencyLog
import com.sentinel.agent.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/register")
    suspend fun registerUser(@Body request: com.sentinel.agent.data.model.RegistrationRequest): Response<User>

    @POST("api/auth/login")
    suspend fun loginUser(@Body request: com.sentinel.agent.data.model.LoginRequest): Response<User>

    @POST("api/emergency/trigger")
    suspend fun triggerEmergency(@Body emergencyLog: EmergencyLog): Response<EmergencyLog>

    @PUT("api/emergency/{id}/resolve")
    suspend fun updateEmergencyStatus(@Path("id") id: String): Response<EmergencyLog>

    @GET("api/user/profile")
    suspend fun getUserProfile(): Response<User>

    @POST("api/tasks")
    suspend fun storeTask(@Body task: com.sentinel.agent.data.model.TaskRequest): Response<Map<String, Any>>

    @JvmSuppressWildcards
    @GET("api/tasks")
    suspend fun getTasks(): Response<List<Map<String, Any>>>

    @JvmSuppressWildcards
    @GET("api/commands/pending")
    suspend fun getPendingCommands(): Response<Map<String, Any>>

    @JvmSuppressWildcards
    @GET("api/registry/list")
    suspend fun getTrustedAgents(): Response<List<Map<String, Any>>>
}
