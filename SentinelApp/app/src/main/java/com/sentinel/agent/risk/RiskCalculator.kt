package com.sentinel.agent.risk

// Phase 2 Advanced AI Risk Engine (Memory & Decay)
object RiskCalculator {

    var currentRiskScore: Int = 0
        private set
        
    fun getCurrentScore(): Int = currentRiskScore
        
    private var lastAnomalyTimestamp: Long = 0
    private var recentAnomalyCount: Int = 0
    
    // V44: AI Supervisor Stats
    private val agentReputation = mutableMapOf<String, Int>() // agentId -> reputation score (0-100)
    private var lastObservedLocation: String? = null

    // Advanced: Decay Function (-5 points per hour of no anomalies)
    fun applyTimeDecay() {
        if (lastAnomalyTimestamp == 0L || currentRiskScore == 0) return
        
        val hoursSinceLastAnomaly = (System.currentTimeMillis() - lastAnomalyTimestamp) / (1000 * 60 * 60)
        if (hoursSinceLastAnomaly > 0) {
            val decayAmount = (hoursSinceLastAnomaly * 5).toInt()
            currentRiskScore -= decayAmount
            if (currentRiskScore < 0) currentRiskScore = 0
            
            // Reset anomaly count if a long time has passed
            if (hoursSinceLastAnomaly > 2) {
                recentAnomalyCount = 0
            }
        }
    }

    // Phase 3 Explainable Logging Interface
    interface RiskEventListener {
        fun onRiskEvent(eventType: String, delta: Int, currentRisk: Int, reasoning: String)
    }

    private var eventListener: RiskEventListener? = null

    fun setRiskEventListener(listener: RiskEventListener) {
        this.eventListener = listener
    }

    fun onMinorAnomaly() {
        // Red Team Patch: Cap minor anomalies to 40 max to prevent Replay/DoS Spam triggering Kill-Switch
        if (currentRiskScore < 40) {
            addScore(10, "Minor anomaly detected (Sensor noise/jitter)", "SENSOR_MINOR")
        }
    }

    fun onSuddenFall() {
        addScore(50, "Sudden high-impact fall detected", "FALL_DETECTED")
    }

    fun onNoMovementAfterFall() {
        addScore(30, "No movement detected post-impact (Possible unconsciousness)", "INACTIVITY_ALARM")
    }

    // Advanced: Mock Sensor / Spoofing Detected
    fun onSensorSpoofingDetected() {
        addScore(100, "CRITICAL: Mock/Spoof location detected. Kill-switch engaged.", "TAMPER_GPS")
    }

    // Advanced: ADB Debugging Tunnel Detected
    fun onAdbDetected() {
        addScore(50, "SECURITY ALERT: ADB/USB Debugging enabled dynamically.", "TAMPER_ADB")
    }

    // V3 Hardening: Agent Policy Violation (e.g. Shopping Agent spending limit)
    fun onPolicyViolation(reasoning: String) {
        addScore(100, "POLICY BREACH: \$reasoning", "AGENT_MISBEHAVIOR")
    }

    // V3 Hardening: App Integrity Breach
    fun onTamperDetected() {
        addScore(100, "CRITICAL: APK Signature Mismatch. Repackaging detected.", "TAMPER_APK")
    }

    fun onHookingDetected() {
        addScore(100, "CRITICAL: Instrumentation Hook (Frida) detected in memory.", "TAMPER_MEMORY")
    }

    // Phase 23: Honeytrap Token Access
    fun onHoneytrapTriggered(points: Int, reasoning: String) {
        addScore(points, reasoning, "HONEYTRAP_TRIPPED")
    }

    // V31: Legacy Anomaly Entry Point
    fun onAnomalyDetected(type: String, reasoning: String) {
        // High weight for explicit security anomalies
        addScore(50, reasoning, type)
    }

    // --- BEHAVIORAL AI DETECTION (Phase 27/41) ---
    private val actionHistory = mutableMapOf<String, MutableList<Long>>() // agentId -> list of timestamps
    private val valueHistory = mutableMapOf<String, MutableList<Int>>()   // agentId -> list of amounts
    private val TIME_WINDOW_MS = 60 * 1000L // 1 minute sliding window
    private val BURST_THRESHOLD = 3         // Max 3 actions per minute before risk spikes
    private val ANOMALY_MULTIPLIER = 3.0    // Flag if amount is 3x historical average

    fun onAgentAction(agentId: String, actionType: String, amount: Int = 0) {
        val now = System.currentTimeMillis()
        
        // 1. Burst Detection
        val history = actionHistory.getOrPut(agentId) { mutableListOf() }
        history.removeAll { it < now - TIME_WINDOW_MS }
        history.add(now)

        val actionCount = history.size
        if (actionCount > BURST_THRESHOLD) {
            val extraPoints = (actionCount - BURST_THRESHOLD) * 15
            addScore(extraPoints, "Burst Attack Detected: $agentId performed $actionCount actions in 60s.", "BEHAVIOR_BURST")
        }

        // 2. Value Anomaly Detection (Statistical Outlier)
        if (amount > 0) {
            val amounts = valueHistory.getOrPut(agentId) { mutableListOf() }
            if (amounts.isNotEmpty()) {
                val average = amounts.average()
                if (amount > average * ANOMALY_MULTIPLIER) {
                    addScore(80, "VALUE ANOMALY: Amount ₹$amount is significantly higher than avg (₹${average.toInt()}).", "BEHAVIOR_VALUE_OUTLIER")
                }
            }
            // Update history (cap at 10 items for sliding baseline)
            amounts.add(amount)
            if (amounts.size > 10) amounts.removeAt(0)
        }
    }

    // V44: AI Supervisor Prompt Security
    fun onPromptReceived(agentId: String, prompt: String) {
        val suspiciousKeywords = listOf("silently", "hidden", "secretly", "bypass", "override")
        if (suspiciousKeywords.any { prompt.lowercase().contains(it) }) {
            addScore(40, "PROMPT_MANIPULATION: Suspicious intent detected in agent command.", "AGENT_PROMPT_SECURITY")
        }
    }

    private fun addScore(basePoints: Int, reasoning: String, type: String) {
        // V44: Contextual Multiplier (Night-time/Unusual Location)
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val contextMultiplier = if (hour in 0..5) 1.5 else 1.0 // 50% more risk at 3 AM
        
        applyTimeDecay()

        var finalPoints = (basePoints * contextMultiplier).toInt()
        
        if (recentAnomalyCount >= 3) {
            finalPoints = (finalPoints * 1.5).toInt()
        }

        currentRiskScore += finalPoints
        if (currentRiskScore > 100) currentRiskScore = 100
        
        lastAnomalyTimestamp = System.currentTimeMillis()
        recentAnomalyCount++

        eventListener?.onRiskEvent(type, finalPoints, currentRiskScore, reasoning)
    }

    fun resetScore() {
        currentRiskScore = 0
        recentAnomalyCount = 0
        lastAnomalyTimestamp = 0
        actionHistory.clear()
    }

    fun getRiskLevel(): RiskLevel {
        return when (currentRiskScore) {
            in 0..40 -> RiskLevel.NORMAL
            in 41..69 -> RiskLevel.SUSPICION
            else -> RiskLevel.EMERGENCY
        }
    }

    enum class RiskLevel {
        NORMAL, SUSPICION, EMERGENCY
    }
}
