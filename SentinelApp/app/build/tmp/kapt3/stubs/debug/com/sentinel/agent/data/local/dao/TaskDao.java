package com.sentinel.agent.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0013\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0010"}, d2 = {"Lcom/sentinel/agent/data/local/dao/TaskDao;", "", "deleteOldTasks", "", "expiryTime", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllTasks", "", "Lcom/sentinel/agent/data/local/entity/TaskEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLatestTaskHash", "", "insertTask", "task", "(Lcom/sentinel/agent/data/local/entity/TaskEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface TaskDao {
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertTask(@org.jetbrains.annotations.NotNull
    com.sentinel.agent.data.local.entity.TaskEntity task, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM agent_tasks ORDER BY timestamp DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAllTasks(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.sentinel.agent.data.local.entity.TaskEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT currentHash FROM agent_tasks ORDER BY timestamp DESC LIMIT 1")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getLatestTaskHash(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.String> $completion);
    
    @androidx.room.Query(value = "DELETE FROM agent_tasks WHERE timestamp < :expiryTime")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteOldTasks(long expiryTime, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}