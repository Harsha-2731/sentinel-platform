package com.sentinel.agent.risk

import android.util.Log

/**
 * Sentinel V2.0: Probabilistic Risk Engine (EDR Logic)
 * 
 * Moves from simple additive scoring to a state-aware, correlated risk model.
 */
object RiskCalculator {

    enum class RiskState { 
        SECURE,     // Normal operations
        VIGILANT,   // High-risk state (Halved limits, UI confirmation required)
        LOCKDOWN    // Critical breach (Fail-Closed triggered)
    }

    private var currentRiskScore: Int = 0
    private var currentState: RiskState = RiskState.SECURE
    private var lastAnomalyTimestamp: Long = 0
    private var recentSignificantEvents = mutableListOf<String>()

    // V2.0 Correlation Maps
    private val eventWeights = mapOf(
        "TAMPER_APK" to 100,
        "TAMPER_MEMORY" to 100,
        "TAMPER_ADB" to 60,
        "TAMPER_GPS" to 100,
        "BEHAVIOR_VALUE_OUTLIER" to 80,
        "BEHAVIOR_BURST" to 50,
        "AGENT_PROMPT_SECURITY" to 40,
        "SENSOR_MINOR" to 10,
        "FALL_DETECTED" to 50
    )

    fun getCurrentScore(): Int = currentRiskScore
    fun getCurrentState(): RiskState = currentState

    fun applyTimeDecay() {
        if (currentRiskScore <= 0 || lastAnomalyTimestamp == 0L) return
        
        val now = System.currentTimeMillis()
        val elapsedMinutes = (now - lastAnomalyTimestamp) / (1000 * 60)
        
        if (elapsedMinutes > 5) {
            // Exponential Decay: Faster recovery if no new events occur
            val decay = (elapsedMinutes / 2).toInt()
            currentRiskScore = (currentRiskScore - decay).coerceAtLeast(0)
            updateState()
        }
    }

    fun onEvent(type: String, reasoning: String = "", basePoints: Int? = null) {
        val now = System.currentTimeMillis()
        applyTimeDecay()

        var points = basePoints ?: eventWeights[type] ?: 30
        
        // 1. Context Multiplier (Night-time 12AM-5AM)
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        if (hour in 0..5) {
            points = (points * 1.5).toInt()
            Log.d("RiskEngine", "CONTEXT_ALARM: Applying 1.5x Night-Shift Multiplier")
        }

        // 2. Correlation Multiplier
        // If we recently saw a Root/Debugger event, subsequent events are far more dangerous
        if (recentSignificantEvents.any { it.startsWith("TAMPER") }) {
            points = (points * 2.0).toInt()
            Log.d("RiskEngine", "CORRELATION_ALARM: High-risk chain detected. Doubling impact.")
        }

        currentRiskScore = (currentRiskScore + points).coerceAtMost(100)
        lastAnomalyTimestamp = now
        
        if (points > 30) {
            recentSignificantEvents.add(type)
            if (recentSignificantEvents.size > 5) recentSignificantEvents.removeAt(0)
        }

        updateState()
        eventListener?.onRiskEvent(type, points, currentRiskScore, reasoning)
    }

    private fun updateState() {
        currentState = when {
            currentRiskScore >= 75 -> RiskState.LOCKDOWN
            currentRiskScore >= 40 -> RiskState.VIGILANT
            else -> RiskState.SECURE
        }
    }

    // Legacy Wrappers (Phase 1 Refactor)
    fun onMinorAnomaly() = onEvent("SENSOR_MINOR", "Minor instability detected.")
    fun onSuddenFall() = onEvent("FALL_DETECTED", "Sudden impact detected.")
    fun onSensorSpoofingDetected() = onEvent("TAMPER_GPS", "Mock GPS usage detected.")
    fun onAdbDetected() = onEvent("TAMPER_ADB", "ADB/Debugging bridge active.")
    fun onTamperDetected() = onEvent("TAMPER_APK", "APK Signature mismatch.")
    fun onHookingDetected() = onEvent("TAMPER_MEMORY", "Runtime instrumentation (Frida) found.")
    fun onPolicyViolation(reason: String) = onEvent("AGENT_POLICY", reason, 100)
    fun onNoMovementAfterFall() = onEvent("SENSOR_CRITICAL", "Critical inactivity detected after impact.", 50)

    // Compatibility Wrappers (for Simulations & Dynamic Events)
    fun onAnomalyDetected(type: String, reasoning: String) = onEvent(type, reasoning)
    
    fun onPromptReceived(agentId: String, prompt: String) {
        val suspiciousKeywords = listOf("silently", "hidden", "secretly", "bypass", "override")
        if (suspiciousKeywords.any { prompt.lowercase().contains(it) }) {
            onEvent("AGENT_PROMPT_SECURITY", "Suspicious intent in agent prompt: $agentId")
        }
    }

    fun onHoneytrapTriggered(points: Int, reasoning: String) = onEvent("HONEYTRAP_TRIPPED", reasoning, points)

    private var eventListener: RiskEventListener? = null
    interface RiskEventListener {
        fun onRiskEvent(eventType: String, delta: Int, currentRisk: Int, reasoning: String)
    }
    fun setRiskEventListener(listener: RiskEventListener) { this.eventListener = listener }

    fun resetScore() {
        currentRiskScore = 0
        currentState = RiskState.SECURE
        recentSignificantEvents.clear()
        lastAnomalyTimestamp = 0
    }
}
