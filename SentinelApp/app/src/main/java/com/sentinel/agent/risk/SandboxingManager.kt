package com.sentinel.agent.risk

import android.content.Context
import android.util.Log
import com.sentinel.agent.risk.RiskCalculator

/**
 * SandboxingManager: Active Enforcement Layer
 * Blocks Intents from rogue or high-risk AI agents.
 */
object SandboxingManager {
    private const val TAG = "SandboxingManager"
    private const val CRITICAL_RISK_THRESHOLD = 70

    /**
     * Checks if an agent should be sandboxed (blocked).
     */
    fun shouldBlockAgent(agentId: String): Boolean {
        val riskScore = RiskCalculator.getCurrentScore()
        
        // Block if global risk is critical
        if (riskScore >= CRITICAL_RISK_THRESHOLD) {
            Log.w(TAG, "SANDBOXING TRIGGERED: Blocking agent $agentId due to CRITICAL risk score ($riskScore)")
            return true
        }

        // Potential future expansion: check per-agent risk or specific blacklists
        return false
    }

    /**
     * Formats an error message for a blocked intent.
     */
    fun getBlockedReason(agentId: String): String {
        return "Action blocked by Sentinel EDR: Agent '$agentId' sandboxed due to security policy enforcement."
    }
}
