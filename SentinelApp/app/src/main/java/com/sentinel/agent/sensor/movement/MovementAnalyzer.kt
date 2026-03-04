package com.sentinel.agent.sensor.movement

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import kotlin.math.sqrt

// Rule-based Fall Detection 
class MovementAnalyzer(private val onFallDetected: (Float) -> Unit) : SensorEventListener {

    // Thresholds matching Build Spec (Phase 1)
    private val FALL_ACCELERATION_THRESHOLD = 25f // m/s²
    private var lastAcceleration = 0f
    private var isFalling = false

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calculate total acceleration vector
            val currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            // Logic: IF acceleration spike > 25 m/s² AND sudden drop THEN possible fall
            if (currentAcceleration > FALL_ACCELERATION_THRESHOLD) {
                isFalling = true
                lastAcceleration = currentAcceleration
            } else if (isFalling && currentAcceleration < 5f) { // Sudden drop
                // Fall confirmed, alert listeners
                onFallDetected(lastAcceleration)
                isFalling = false
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }
}
