package com.sentinel.agent.service.monitoring

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sentinel.agent.risk.RiskCalculator
import com.sentinel.agent.risk.HoneytrapManager
import com.sentinel.agent.sensor.movement.MovementAnalyzer
import com.sentinel.agent.service.emergency.EmergencyManager
import kotlinx.coroutines.*
import android.util.Log
import com.sentinel.agent.data.local.AppDatabase
import com.sentinel.agent.data.local.entity.RiskEventEntity
import android.content.BroadcastReceiver
import android.content.IntentFilter
import com.sentinel.agent.utils.SecurityHelper
import android.app.usage.UsageStatsManager
import com.sentinel.agent.network.client.RetrofitClient
import java.util.concurrent.TimeUnit
import java.util.*

class MonitoringService : Service() {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var movementAnalyzer: MovementAnalyzer
    
    private val emergencyManager = EmergencyManager()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val db by lazy { AppDatabase.getDatabase(this) }
    private val securityHelper by lazy { SecurityHelper(this) }
    
    private lateinit var honeytrapManager: HoneytrapManager
    
    private var monitoringJob: Job? = null
    private var isEmergencyActive = false

    private val resetEmergencyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.sentinel.agent.RESET_EMERGENCY") {
                isEmergencyActive = false
                com.sentinel.agent.risk.RiskCalculator.resetScore()
            } else if (intent?.action == "com.sentinel.agent.SIMULATE_TAMPER") {
                com.sentinel.agent.risk.RiskCalculator.onAnomalyDetected("DEVICE_TAMPERED", "DEBUG: Simulated Hardware Tampering.")
                checkRiskLevel()
            }
        }
    }

    companion object {
        const val CHANNEL_ID = "sentinel_monitoring_channel"
        const val NOTIFICATION_ID = 1001
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        registerReceiver(resetEmergencyReceiver, IntentFilter("com.sentinel.agent.RESET_EMERGENCY"), Context.RECEIVER_NOT_EXPORTED)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        
        // V7: Initialize and Deploy Honeytrap
        honeytrapManager = HoneytrapManager(this) { delta, reasoning ->
            com.sentinel.agent.risk.RiskCalculator.onHoneytrapTriggered(delta, reasoning)
            checkRiskLevel()
        }
        honeytrapManager.deployHoneytrap()
        
        movementAnalyzer = MovementAnalyzer { peakAcceleration ->
            // Logic: Fall detected -> increase risk, start inactivity timer
            com.sentinel.agent.risk.RiskCalculator.onSuddenFall()
            checkRiskLevel()
            
            // Wait 10 seconds to check for inactivity
            serviceScope.launch {
                delay(10000) // 10 seconds
                // In a full implementation, we'd check if movement resumed.
                // Assuming no movement for Phase 1 simulation:
                com.sentinel.agent.risk.RiskCalculator.onNoMovementAfterFall()
                checkRiskLevel()
            }
        }
        
        // V3 Hardening: Setup Explainable Logging
        com.sentinel.agent.risk.RiskCalculator.setRiskEventListener(object : RiskCalculator.RiskEventListener {
            override fun onRiskEvent(eventType: String, delta: Int, currentRisk: Int, reasoning: String) {
                serviceScope.launch {
                    val event = RiskEventEntity(
                        timestamp = System.currentTimeMillis(),
                        eventType = eventType,
                        riskDelta = delta,
                        currentRiskScore = currentRisk,
                        reasoning = reasoning
                    )
                    db.riskEventDao().insertEvent(event)
                    Log.d("MonitoringService", "Stored Risk Event: \$eventType - \$reasoning")
                }
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sentinel: Your Phone is Protected")
            .setContentText("Personal AI Guardian is actively shielding you from scams.")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
            
        startForeground(NOTIFICATION_ID, notification)
        startSensors()
        startSurveillanceLoop() // Replaced startLocationUpdates to encompass broader checks

        return START_STICKY
    }
    
    private fun startSensors() {
        sensorManager.registerListener(
            movementAnalyzer, 
            accelerometer, 
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    
    // V3 Hardening: Background Surveillance Loop for integrity
    private fun startSurveillanceLoop() {
        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                // 1. Fetch GPS location periodically (mocked for now)
                fetchLocation()
                
                // 2. Check for newly enabled ADB/USB Debugging
                if (securityHelper.isAdbEnabled()) {
                    Log.w("MonitoringService", "SECURITY ALERT: ADB/USB Debugging enabled dynamically.")
                    com.sentinel.agent.risk.RiskCalculator.onAdbDetected()
                    checkRiskLevel()
                }

                // 3. Poll for Remote Web Dashboard Commands
                fetchPendingCommands()

                // 3. V3 Hardening: Memory Hooking Detection
                if (securityHelper.isFridaDetected()) {
                    Log.e("MonitoringService", "CRITICAL: Frida/Hooking detected.")
                    com.sentinel.agent.risk.RiskCalculator.onHookingDetected()
                    checkRiskLevel()
                }

                // 4. V3 Hardening: Application Integrity Check
                // Note: Binary signature hex should be the developer's release key
                if (!securityHelper.isApkSignatureValid("EXPECTED_RELEASE_SIGNATURE_HEX")) {
                    Log.e("MonitoringService", "CRITICAL: APK Tampered / Repackaged.")
                    com.sentinel.agent.risk.RiskCalculator.onTamperDetected()
                    checkRiskLevel()
                }
                
                // 5. V31: Advanced Device Integrity Check
                val integrity = com.sentinel.agent.service.integrity.IntegrityManager.checkIntegrity(this@MonitoringService)
                if (integrity is com.sentinel.agent.service.integrity.IntegrityManager.IntegrityResult.COMPROMISED) {
                    Log.e("MonitoringService", "CRITICAL: Device Integrity Compromised: ${integrity.reason}")
                    com.sentinel.agent.risk.RiskCalculator.onAnomalyDetected("DEVICE_TAMPERED", "Hardware/Root Tampering Detected: ${integrity.reason}")
                    checkRiskLevel()
                }

                // 6. Behavioral Context: Foreground App Tracking
                checkForegroundApp()

                delay(2000) // V29: Respond to dashboard commands in near real-time (2s)
            }
        }
    }

    private fun fetchLocation() {
        // Mock GPS Fetch
        // In a real implementation, this would initiate location updates
        // and process the results.
        // For now, just a placeholder to simulate activity.
        // delay(10000) // This delay is handled by the surveillance loop's delay
        // Logic: evaluate speed, update risk if anomalies.
    }

    private suspend fun fetchPendingCommands() {
        try {
            val response = RetrofitClient.createService({ securityHelper.getJwtToken() }, { securityHelper.getHmacSecret() ?: "" }).getPendingCommands()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null && data["success"] == true) {
                    val commands = data["commands"] as? List<Map<String, Any>>
                    commands?.forEach { cmd ->
                        val action = cmd["action"] as? String
                        Log.e("MonitoringService", "CRITICAL: Received Remote Dashboard Command: $action")
                        if (action == "LOCK_DEVICE" || action == "WIPE_DATA") {
                            launchEmergencyLockdown("Remote Command Received: $action")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Silently ignore network errors during background polling
        }
    }

    private fun checkRiskLevel() {
        when (com.sentinel.agent.risk.RiskCalculator.getRiskLevel()) {
            RiskCalculator.RiskLevel.NORMAL -> { /* Do nothing */ }
            RiskCalculator.RiskLevel.SUSPICION -> {
                // User vibration alert, 5 sec cancel option logic will be here
                emergencyManager.triggerSuspicionMode(applicationContext, com.sentinel.agent.risk.RiskCalculator.currentRiskScore)
            }
            RiskCalculator.RiskLevel.EMERGENCY -> {
                if (!isEmergencyActive) {
                    launchEmergencyLockdown("System Integrity Compromised. Revoking Agent Identity.")
                }
            }
        }
    }
    
    private var lastForegroundPackage: String? = null

    private fun checkForegroundApp() {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60, time)

        if (stats != null) {
            val sortedStats = stats.sortedByDescending { it.lastTimeUsed }
            if (sortedStats.isNotEmpty()) {
                val currentPackage = sortedStats[0].packageName
                
                if (currentPackage != lastForegroundPackage) {
                    lastForegroundPackage = currentPackage
                    Log.d("MonitoringService", "APP_CONTEXT_SWITCH: Now monitoring $currentPackage")
                    
                    // Log the app switch event for audit trail
                    com.sentinel.agent.risk.RiskCalculator.onAnomalyDetected("APP_USAGE_LOG", "User switched to: $currentPackage")
                    
                    // Autonomous Sensitivity Detection: SHOPPING / FINANCE / BANKING
                    try {
                        val appInfo = packageManager.getApplicationInfo(currentPackage, 0)
                        val isSensitive = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            // Using numerical constants for broad compatibility
                            appInfo.category == 4 || // CATEGORY_SHOPPING
                            appInfo.category == 7 || // CATEGORY_PRODUCTIVITY
                            currentPackage.contains("shopping") || currentPackage.contains("payment") || currentPackage.contains("cart")
                        } else {
                            currentPackage.contains("shopping") || currentPackage.contains("payment")
                        }

                        if (isSensitive) {
                            Log.i("MonitoringService", "CONTEXT_AWARE: Sensitive App Category detected ($currentPackage). Escalating vigilance.")
                            com.sentinel.agent.risk.RiskCalculator.onAnomalyDetected("SENSITIVE_APP_LAUNCH", "Economic/Finance agent context detected. High-sensitivity behavioral monitoring enabled.")
                            checkRiskLevel()
                        }
                    } catch (e: Exception) {
                        // Package not found or other info error
                    }
                }
            }
        }
    }

    private fun launchEmergencyLockdown(reason: String) {
        if (!isEmergencyActive) {
            isEmergencyActive = true
            emergencyManager.triggerEmergencyMode(applicationContext, 100)
            
            // V3 Hardening: Launch Lockdown UI
            val intent = Intent(this, com.sentinel.agent.ui.EmergencyActivity::class.java).apply {
                putExtra("REASONING", reason)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(resetEmergencyReceiver)
        sensorManager.unregisterListener(movementAnalyzer)
        honeytrapManager.removeHoneytrap()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Sentinel Monitoring Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}
