package com.sentinel.agent.sensor.movement;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\n"}, d2 = {"Lcom/sentinel/agent/sensor/movement/LocationAnalyzer;", "", "()V", "detectPhysicsAnomaly", "", "location", "Landroid/location/Location;", "currentAcceleration", "", "isMockLocation", "app_debug"})
public final class LocationAnalyzer {
    
    public LocationAnalyzer() {
        super();
    }
    
    public final boolean isMockLocation(@org.jetbrains.annotations.NotNull
    android.location.Location location) {
        return false;
    }
    
    public final boolean detectPhysicsAnomaly(@org.jetbrains.annotations.NotNull
    android.location.Location location, float currentAcceleration) {
        return false;
    }
}