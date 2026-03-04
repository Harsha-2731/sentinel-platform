package com.sentinel.agent.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001f\u0010\u000f\u001a\u00020\u00032\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0013"}, d2 = {"Lcom/sentinel/agent/data/local/dao/RiskEventDao;", "", "deleteOldEvents", "", "expiryTime", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllEvents", "", "Lcom/sentinel/agent/data/local/entity/RiskEventEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getUnsyncedEvents", "insertEvent", "event", "(Lcom/sentinel/agent/data/local/entity/RiskEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAsSynced", "eventIds", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface RiskEventDao {
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertEvent(@org.jetbrains.annotations.NotNull
    com.sentinel.agent.data.local.entity.RiskEventEntity event, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM risk_events ORDER BY timestamp DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAllEvents(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.sentinel.agent.data.local.entity.RiskEventEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM risk_events WHERE isSynced = 0 ORDER BY timestamp ASC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getUnsyncedEvents(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.sentinel.agent.data.local.entity.RiskEventEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE risk_events SET isSynced = 1 WHERE id IN (:eventIds)")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object markAsSynced(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Integer> eventIds, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM risk_events WHERE timestamp < :expiryTime")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteOldEvents(long expiryTime, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}