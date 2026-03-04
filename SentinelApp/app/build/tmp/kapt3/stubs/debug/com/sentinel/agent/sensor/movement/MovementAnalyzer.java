package com.sentinel.agent.sensor.movement;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u000b\u001a\u00020\u00052\b\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0012\u0010\u0010\u001a\u00020\u00052\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/sentinel/agent/sensor/movement/MovementAnalyzer;", "Landroid/hardware/SensorEventListener;", "onFallDetected", "Lkotlin/Function1;", "", "", "(Lkotlin/jvm/functions/Function1;)V", "FALL_ACCELERATION_THRESHOLD", "isFalling", "", "lastAcceleration", "onAccuracyChanged", "sensor", "Landroid/hardware/Sensor;", "accuracy", "", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "app_debug"})
public final class MovementAnalyzer implements android.hardware.SensorEventListener {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<java.lang.Float, kotlin.Unit> onFallDetected = null;
    private final float FALL_ACCELERATION_THRESHOLD = 25.0F;
    private float lastAcceleration = 0.0F;
    private boolean isFalling = false;
    
    public MovementAnalyzer(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onFallDetected) {
        super();
    }
    
    @java.lang.Override
    public void onSensorChanged(@org.jetbrains.annotations.Nullable
    android.hardware.SensorEvent event) {
    }
    
    @java.lang.Override
    public void onAccuracyChanged(@org.jetbrains.annotations.Nullable
    android.hardware.Sensor sensor, int accuracy) {
    }
}