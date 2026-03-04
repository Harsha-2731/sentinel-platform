package com.sentinel.agent.sensor.movement

import android.location.Location
import android.os.Build

class LocationAnalyzer {

    // Advanced: Detect Mock GPS Locations
    fun isMockLocation(location: Location): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            location.isMock
        } else {
            @Suppress("DEPRECATION")
            location.isFromMockProvider
        }
    }

    // Advanced: Physics Cross-Check
    // If GPS speed says we are moving 30 m/s (approx 108 km/h) 
    // but Accelerometer shows stationary resting (approx 9.8 m/s^2 gravity only), it's spoofed.
    fun detectPhysicsAnomaly(location: Location, currentAcceleration: Float): Boolean {
        val speedMps = location.speed
        
        // Let's assume resting accelerometer with phone on desk is ~9.8 m/s^2 depending on vector math.
        // We will simplify: if speed is very high, but recent dynamic acceleration is completely zero/resting 
        // (meaning no vibration, no bumps from a car/train), it's highly suspicious.
        
        // Normal gravity resting vector length is around 9.8
        val restingDelta = Math.abs(currentAcceleration - 9.81f)
        
        // If GPS Speed > 15 m/s (54 km/h) AND Phone is perfectly dead-still
        if (speedMps > 15f && restingDelta < 0.5f) {
            return true // Impossible Physics
        }
        
        return false
    }
}
