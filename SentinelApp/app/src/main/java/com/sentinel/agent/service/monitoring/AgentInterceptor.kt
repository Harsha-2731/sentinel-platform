package com.sentinel.agent.service.monitoring

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.sentinel.agent.data.local.AppDatabase
import com.sentinel.agent.data.local.entity.TaskEntity
import com.sentinel.agent.network.client.RetrofitClient
import com.sentinel.agent.risk.RiskCalculator
import com.sentinel.agent.risk.ThreatIntelManager
import com.sentinel.agent.sdk.SentinelSDK
import com.sentinel.agent.ui.EmergencyActivity
import com.sentinel.agent.utils.IdentityVerifier
import com.sentinel.agent.utils.SecurityHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * V34: Sentinel Policy Interceptor (10/10 Architecture)
 * 
 * Implements Tiered Response, Hash Chaining, and Threat Intelligence scanning.
 */
class AgentInterceptor : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SentinelSDK.ACTION_AGENT_EXECUTE) {
            val agentName = intent.getStringExtra(SentinelSDK.EXTRA_AGENT_NAME) ?: "Unknown Agent"
            val actionType = intent.getStringExtra(SentinelSDK.EXTRA_ACTION_TYPE) ?: "Generic"
            val amount = intent.getIntExtra(SentinelSDK.EXTRA_AMOUNT, 0)

            // ✅ Enforce a spending limit. Use a fallback of 1000 if not set.
            val prefs = context.getSharedPreferences("sentinel_prefs", Context.MODE_PRIVATE)
            val savedLimitStr = prefs.getString("max_agent_spend", "")
            val savedLimit = if (!savedLimitStr.isNullOrEmpty()) savedLimitStr.toIntOrNull() else 1000
            val hasSpendingLimit = savedLimit != null && savedLimit > 0
            
            val agentId = intent.getStringExtra(SentinelSDK.EXTRA_AGENT_ID) ?: "unknown.agent"
            val signature = intent.getStringExtra(SentinelSDK.EXTRA_SIGNATURE)
            val timestamp = intent.getLongExtra(SentinelSDK.EXTRA_TIMESTAMP, 0L)
            
            // 1. Identity Verification
            val payload = "$agentName:$actionType:$amount:$timestamp"
            val isVerified = IdentityVerifier.verifyAgentAction(agentId, payload, signature)

            // 2. Threat Intelligence Scan (V34)
            val threat = ThreatIntelManager.checkPackage(context, agentId)
            if (threat != null) {
                Log.e("AgentInterceptor", "MALICIOUS AGENT DETECTED: $agentId (${threat.threatType})")
                RiskCalculator.onAnomalyDetected("THREAT_DETECTED", "Malicious app signature found: $agentId")
                handleTieredResponse(context, agentId, agentName, actionType, amount, "blocked_malicious")
                return
            }

            // 4. OS Visibility Cross-Verification (V42)
            val scrapedPrefs = context.getSharedPreferences("sentinel_scraped_data", Context.MODE_PRIVATE)
            val visualPrice = scrapedPrefs.getInt("last_visual_price", -1)
            val visualTime = scrapedPrefs.getLong("last_price_timestamp", 0L)
            
            // Heuristic: If we saw a price in the last 60 seconds, and this charge is > that price.
            val isVisualMismatch = visualPrice != -1 && 
                                  (System.currentTimeMillis() - visualTime < 60000) &&
                                  (amount > visualPrice + 5) // ₹5 buffer

            if (isVisualMismatch) {
                Log.e("AgentInterceptor", "CRITICAL FRAUD: Visual Price (₹$visualPrice) != Intent Price (₹$amount)")
                RiskCalculator.onPolicyViolation("UI_INTENT_MISMATCH: Visual price ₹$visualPrice but agent requested ₹$amount.")
            }

            // 5. Tiered Incident Response (V34)
            val riskLevel = RiskCalculator.getRiskLevel()
            Log.d("AgentInterceptor", "Current Risk Level: $riskLevel (Score: ${RiskCalculator.currentRiskScore})")

            val shouldBlock = com.sentinel.agent.risk.SandboxingManager.shouldBlockAgent(agentId) ||
                              riskLevel == RiskCalculator.RiskLevel.EMERGENCY ||
                              (hasSpendingLimit && amount > savedLimit!!) ||
                              !isVerified ||
                              isVisualMismatch

            if (shouldBlock) {
                val status = when {
                    !isVerified -> "blocked_identity_fail"
                    isVisualMismatch -> "blocked_fraud_mismatch"
                    (hasSpendingLimit && amount > savedLimit!!) -> "blocked_policy_overspend"
                    else -> "blocked_behavioral_anomaly"
                }
                handleTieredResponse(context, agentId, agentName, actionType, amount, status)
            } else {
                // Legitimate activity
                if (riskLevel == RiskCalculator.RiskLevel.SUSPICION) {
                    Toast.makeText(context, "Sentinel Alert: Monitored activity from $agentName", Toast.LENGTH_SHORT).show()
                }
                shipTaskToBackend(context, agentId, agentName, actionType, amount, "allowed")
            }
        }
    }

    private fun handleTieredResponse(context: Context, agentId: String, name: String, type: String, amount: Int, status: String) {
        val reasoning = "⚠ Fraud Detected\n\nAgent attempted ₹$amount charge.\nTransaction blocked by Sentinel."
        
        // Level 1: Full-Screen Intent Interception (Bypasses Background Restrictions)
        val fullScreenIntent = Intent(context, EmergencyActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("REASONING", reasoning)
        }
        
        val fullScreenPendingIntent = android.app.PendingIntent.getActivity(
            context, 0, fullScreenIntent, 
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val channelId = "sentinel_alerts"
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId, "Sentinel Security Alerts", 
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical security interceptions"
                setBypassDnd(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = androidx.core.app.NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("SENTINEL BLOCK: $name")
            .setContentText("Malicious transaction detected and blocked.")
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_MAX)
            .setCategory(androidx.core.app.NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())

        // Send Violation Broadcast back to the Agent for UI feedback
        val violationIntent = Intent("com.sentinel.agent.POLICY_VIOLATION").apply {
            setPackage(agentId) // Targeted callback
            putExtra("STATUS", status)
            putExtra("REASON", "Spending limit exceeded or identity verification failed.")
        }
        context.sendBroadcast(violationIntent)

        // Level 2: Log Immutably
        shipTaskToBackend(context, agentId, name, type, amount, status)
    }

    private fun shipTaskToBackend(context: Context, agentId: String, name: String, type: String, amount: Int, status: String) {
        val securityHelper = SecurityHelper(context)
        val apiService = RetrofitClient.createService(
            getToken = { securityHelper.getJwtToken() },
            getSecret = { securityHelper.getHmacSecret() }
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val taskDao = db.taskDao()

                // V34: Immutable Hash Chaining
                val prevHash = taskDao.getLatestTaskHash() ?: "SENTINEL_GENESIS_BLOCK"
                val taskPayload = "$prevHash:$name:$type:$amount:$status:${System.currentTimeMillis()}"
                val currentHash = IdentityVerifier.sha256(taskPayload)

                val localTask = TaskEntity(
                    agentName = name,
                    taskDescription = "AI Agent execution: $type",
                    actionType = type,
                    amount = amount,
                    status = status,
                    timestamp = System.currentTimeMillis(),
                    previousHash = prevHash,
                    currentHash = currentHash
                )
                taskDao.insertTask(localTask)
                Log.d("AgentInterceptor", "Chained task stored. Hash: $currentHash")

                // 2. Ship to Remote Backend
                val taskRequest = com.sentinel.agent.data.model.TaskRequest(
                    agentId = agentId,
                    agentName = name,
                    taskDescription = "AI Agent execution: $type",
                    actionType = type,
                    amount = amount,
                    status = status
                )
                apiService.storeTask(taskRequest)
            } catch (e: Exception) {
                Log.e("AgentInterceptor", "Failed to store/ship task: ${e.message}")
            }
        }
    }
}
