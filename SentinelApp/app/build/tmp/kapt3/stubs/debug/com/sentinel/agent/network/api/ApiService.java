package com.sentinel.agent.network.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J#\u0010\u0002\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J)\u0010\u0007\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\b0\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J)\u0010\t\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\b0\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J!\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\r\u001a\u00020\u000eH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ!\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00032\b\b\u0001\u0010\r\u001a\u00020\u0011H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J-\u0010\u0013\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00040\u00032\b\b\u0001\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J!\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u00032\b\b\u0001\u0010\u0019\u001a\u00020\u0018H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001aJ!\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00180\u00032\b\b\u0001\u0010\u001c\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001d\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001e"}, d2 = {"Lcom/sentinel/agent/network/api/ApiService;", "", "getPendingCommands", "Lretrofit2/Response;", "", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTasks", "", "getTrustedAgents", "getUserProfile", "Lcom/sentinel/agent/data/model/User;", "loginUser", "request", "Lcom/sentinel/agent/data/model/LoginRequest;", "(Lcom/sentinel/agent/data/model/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "registerUser", "Lcom/sentinel/agent/data/model/RegistrationRequest;", "(Lcom/sentinel/agent/data/model/RegistrationRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "storeTask", "task", "Lcom/sentinel/agent/data/model/TaskRequest;", "(Lcom/sentinel/agent/data/model/TaskRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerEmergency", "Lcom/sentinel/agent/data/model/EmergencyLog;", "emergencyLog", "(Lcom/sentinel/agent/data/model/EmergencyLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEmergencyStatus", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "api/auth/register")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object registerUser(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.sentinel.agent.data.model.RegistrationRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.sentinel.agent.data.model.User>> $completion);
    
    @retrofit2.http.POST(value = "api/auth/login")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object loginUser(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.sentinel.agent.data.model.LoginRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.sentinel.agent.data.model.User>> $completion);
    
    @retrofit2.http.POST(value = "api/emergency/trigger")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object triggerEmergency(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.sentinel.agent.data.model.EmergencyLog emergencyLog, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.sentinel.agent.data.model.EmergencyLog>> $completion);
    
    @retrofit2.http.PUT(value = "api/emergency/{id}/resolve")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object updateEmergencyStatus(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.sentinel.agent.data.model.EmergencyLog>> $completion);
    
    @retrofit2.http.GET(value = "api/user/profile")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getUserProfile(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.sentinel.agent.data.model.User>> $completion);
    
    @retrofit2.http.POST(value = "api/tasks")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object storeTask(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.sentinel.agent.data.model.TaskRequest task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> $completion);
    
    @retrofit2.http.GET(value = "api/tasks")
    @kotlin.jvm.JvmSuppressWildcards
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTasks(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<retrofit2.Response<java.util.List<java.util.Map<java.lang.String, java.lang.Object>>>> $completion);
    
    @retrofit2.http.GET(value = "api/commands/pending")
    @kotlin.jvm.JvmSuppressWildcards
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getPendingCommands(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> $completion);
    
    @retrofit2.http.GET(value = "api/registry/list")
    @kotlin.jvm.JvmSuppressWildcards
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getTrustedAgents(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<retrofit2.Response<java.util.List<java.util.Map<java.lang.String, java.lang.Object>>>> $completion);
}