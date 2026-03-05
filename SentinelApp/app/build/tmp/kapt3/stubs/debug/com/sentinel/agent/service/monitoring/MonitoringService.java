package com.sentinel.agent.service.monitoring;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0084\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 @2\u00020\u0001:\u0001@B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\'\u001a\u00020(H\u0002J\b\u0010)\u001a\u00020(H\u0002J\b\u0010*\u001a\u00020(H\u0002J\b\u0010+\u001a\u00020(H\u0002J\u0011\u0010,\u001a\u00020(H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-J\b\u0010.\u001a\u00020/H\u0002J\u0010\u00100\u001a\u00020(2\u0006\u00101\u001a\u00020\u0017H\u0002J\u0014\u00102\u001a\u0004\u0018\u0001032\b\u00104\u001a\u0004\u0018\u000105H\u0016J\b\u00106\u001a\u00020(H\u0016J\b\u00107\u001a\u00020(H\u0016J\"\u00108\u001a\u00020/2\b\u00104\u001a\u0004\u0018\u0001052\u0006\u00109\u001a\u00020/2\u0006\u0010:\u001a\u00020/H\u0016J\b\u0010;\u001a\u00020(H\u0002J\u0011\u0010<\u001a\u00020(H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-J\u0011\u0010=\u001a\u00020(H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-J\b\u0010>\u001a\u00020(H\u0002J\b\u0010?\u001a\u00020(H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001e\u001a\u00020\u001f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\"\u0010\r\u001a\u0004\b \u0010!R\u000e\u0010#\u001a\u00020$X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006A"}, d2 = {"Lcom/sentinel/agent/service/monitoring/MonitoringService;", "Landroid/app/Service;", "()V", "accelerometer", "Landroid/hardware/Sensor;", "currentLatitude", "", "currentLongitude", "db", "Lcom/sentinel/agent/data/local/AppDatabase;", "getDb", "()Lcom/sentinel/agent/data/local/AppDatabase;", "db$delegate", "Lkotlin/Lazy;", "emergencyManager", "Lcom/sentinel/agent/service/emergency/EmergencyManager;", "fusedLocationClient", "Lcom/google/android/gms/location/FusedLocationProviderClient;", "honeytrapManager", "Lcom/sentinel/agent/risk/HoneytrapManager;", "isEmergencyActive", "", "lastForegroundPackage", "", "monitoringJob", "Lkotlinx/coroutines/Job;", "movementAnalyzer", "Lcom/sentinel/agent/sensor/movement/MovementAnalyzer;", "resetEmergencyReceiver", "Landroid/content/BroadcastReceiver;", "securityHelper", "Lcom/sentinel/agent/utils/SecurityHelper;", "getSecurityHelper", "()Lcom/sentinel/agent/utils/SecurityHelper;", "securityHelper$delegate", "sensorManager", "Landroid/hardware/SensorManager;", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "checkForegroundApp", "", "checkRiskLevel", "createNotificationChannel", "fetchLocation", "fetchPendingCommands", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBatteryLevel", "", "launchEmergencyLockdown", "reason", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "flags", "startId", "performNuclearWipe", "registerDevice", "sendHeartbeat", "startSensors", "startSurveillanceLoop", "Companion", "app_debug"})
public final class MonitoringService extends android.app.Service {
    private android.hardware.SensorManager sensorManager;
    private android.hardware.Sensor accelerometer;
    private com.sentinel.agent.sensor.movement.MovementAnalyzer movementAnalyzer;
    @org.jetbrains.annotations.NotNull
    private final com.sentinel.agent.service.emergency.EmergencyManager emergencyManager = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy db$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy securityHelper$delegate = null;
    private com.google.android.gms.location.FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private com.sentinel.agent.risk.HoneytrapManager honeytrapManager;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job monitoringJob;
    private boolean isEmergencyActive = false;
    @org.jetbrains.annotations.NotNull
    private final android.content.BroadcastReceiver resetEmergencyReceiver = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String CHANNEL_ID = "sentinel_monitoring_channel";
    public static final int NOTIFICATION_ID = 1001;
    @org.jetbrains.annotations.Nullable
    private java.lang.String lastForegroundPackage;
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.service.monitoring.MonitoringService.Companion Companion = null;
    
    public MonitoringService() {
        super();
    }
    
    private final com.sentinel.agent.data.local.AppDatabase getDb() {
        return null;
    }
    
    private final com.sentinel.agent.utils.SecurityHelper getSecurityHelper() {
        return null;
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    private final void startSensors() {
    }
    
    private final void startSurveillanceLoop() {
    }
    
    private final java.lang.Object registerDevice(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object sendHeartbeat(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final int getBatteryLevel() {
        return 0;
    }
    
    private final void fetchLocation() {
    }
    
    private final java.lang.Object fetchPendingCommands(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void checkRiskLevel() {
    }
    
    private final void checkForegroundApp() {
    }
    
    private final void launchEmergencyLockdown(java.lang.String reason) {
    }
    
    private final void performNuclearWipe() {
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    private final void createNotificationChannel() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/sentinel/agent/service/monitoring/MonitoringService$Companion;", "", "()V", "CHANNEL_ID", "", "NOTIFICATION_ID", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}