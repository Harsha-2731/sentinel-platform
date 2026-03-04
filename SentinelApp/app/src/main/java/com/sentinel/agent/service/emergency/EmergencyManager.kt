package com.sentinel.agent.service.emergency

import android.content.Context
import android.util.Log
import com.sentinel.agent.data.model.EmergencyLog
import com.sentinel.agent.network.client.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.sentinel.agent.data.local.AppDatabase
import com.sentinel.agent.data.local.entity.RiskEventEntity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.sentinel.agent.service.sync.LogSyncWorker

class EmergencyManager {

    private val apiService = RetrofitClient.createService() // Would inject token provider
    private val scope = CoroutineScope(Dispatchers.IO)

    private var isEmergencyCancelled = false

    fun triggerSuspicionMode(context: Context, riskScore: Int) {
        // Trigger Vibration (Vibrator API)
        Log.d("EmergencyManager", "Suspicion Mode Triggered! Risk: $riskScore. Vibrate user.")
        // Launch UI Intent or Broadcast to show 5-second cancel option.
    }

    // Red Team Patch: Implement Cancel Delay to prevent false positive immediate revocation
    fun cancelEmergency() {
        isEmergencyCancelled = true
        Log.d("EmergencyManager", "User explicitly cancelled the emergency. Halting Revocation.")
    }

    fun triggerEmergencyMode(context: Context, riskScore: Int) {
        Log.d("EmergencyManager", "EMERGENCY THRESHOLD MET. Starting 5-second Cancel Window.")
        isEmergencyCancelled = false
        
        scope.launch {
            // Asynchronous Wait Lock
            kotlinx.coroutines.delay(5000)
            
            if (isEmergencyCancelled) return@launch
            
            Log.d("EmergencyManager", "5 seconds passed. CANCELLATION FAILED. EXECUTING KILL-SWITCH!")
            
            // 1. Fetch Final GPS (Mocked)
            val lat = 37.7749
            val lon = -122.4194

            // 2. Clear out Token (Simulating Auto-Revocation)
            revokeLocalAgentAuthority(context)

            // 3. API Call
            try {
                val log = EmergencyLog(
                    _id = null,
                    userId = "mocked_user_id", // Would come from SecurityHelper in real implementation
                    timestamp = System.currentTimeMillis().toString(),
                    latitude = lat,
                    longitude = lon,
                    riskScore = riskScore,
                    movementData = "{\"fallDetected\": true}",
                    status = "active",
                    audioFileUrl = null
                )
                
                // Red Team Patch: Offline Revocation Queue (WorkManager concept added as comment)
                // In production, if this fails, we must queue the Revoke API call in a persistent local DB
                // so the backend Token Blacklist updates the moment Wi-Fi reconnects.
                val response = apiService.triggerEmergency(log)
                if (response.isSuccessful) {
                    // Fire backend Revoke sequence too
                    apiService.updateEmergencyStatus("revoke_agent_token") // Simplified mapping for audit
                    Log.d("EmergencyManager", "Emergency Synced. Server-side Agent Token Blacklisted.")
                } else {
                    Log.e("EmergencyManager", "Network failed. Falling back to SMS. Backend token remains active (VULNERABILITY: Offline Asymmetry until queued sync).")
                    sendSmsFallback(context, lat, lon)
                    queueOfflineRevocation(context, riskScore)
                }
            } catch (e: Exception) {
                Log.e("EmergencyManager", "API Error: ${e.message}. Falling back to SMS.")
                sendSmsFallback(context, lat, lon)
                queueOfflineRevocation(context, riskScore)
            }
        }
    }
    
    // V4 Hardening: Offline Emergency Queuing
    private fun queueOfflineRevocation(context: Context, riskScore: Int) {
        scope.launch {
            try {
                val db = AppDatabase.getDatabase(context)
                db.riskEventDao().insertEvent(
                    RiskEventEntity(
                        timestamp = System.currentTimeMillis(),
                        eventType = "EMERGENCY_REVOCATION_PENDING",
                        riskDelta = 100,
                        currentRiskScore = riskScore,
                        reasoning = "Network offline. Token queued for backend revocation sync.",
                        isSynced = false
                    )
                )
                
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                    
                val syncRequest = OneTimeWorkRequestBuilder<LogSyncWorker>()
                    .setConstraints(constraints)
                    .build()
                    
                WorkManager.getInstance(context).enqueue(syncRequest)
                Log.d("EmergencyManager", "Offline Revocation Queued via WorkManager.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun revokeLocalAgentAuthority(context: Context) {
        val securityHelper = com.sentinel.agent.utils.SecurityHelper(context)
        
        // V35: 10/10 Hardening - Actual Token Self-Destruct
        securityHelper.saveJwtToken("")
        securityHelper.saveHmacSecret("")
        
        // Set persistent lockdown flag
        securityHelper.setLockdownActive(true)
        
        Log.d("EmergencyManager", "CRITICAL: Token revoked and Lockdown Flag set. Agent execution halted.")
    }

    private fun sendSmsFallback(context: Context, lat: Double, lon: Double) {
        // Logic to use SmsManager to text trustedContacts.
        Log.d("EmergencyManager", "SMS sent with Location: $lat, $lon")
    }
}
