package com.sentinel.agent.sdk

/**
 * SentinelSDK (Phase 34)
 * 
 * Formal interface for AI Agents to integrate with the Sentinel Security Layer.
 */
object SentinelSDK {
    
    // Broadcast Action for Agents to request execution
    const val ACTION_AGENT_EXECUTE = "com.sentinel.agent.ACTION_AGENT_EXECUTE"
    
    // Broadcast Action for Policy Violations
    const val ACTION_POLICY_VIOLATION = "com.sentinel.agent.POLICY_VIOLATION"
    
    // Key names for Intent Extras
    const val EXTRA_AGENT_NAME = "AGENT_NAME"
    const val EXTRA_ACTION_TYPE = "ACTION_TYPE"
    const val EXTRA_AMOUNT = "AMOUNT"
    const val EXTRA_THRESHOLD = "THRESHOLD"
    
    // Cryptographic Keys (EDR Layer)
    const val EXTRA_AGENT_ID = "com.sentinel.agent.EXTRA_AGENT_ID"
    const val EXTRA_SIGNATURE = "com.sentinel.agent.EXTRA_SIGNATURE"
    const val EXTRA_TIMESTAMP = "com.sentinel.agent.EXTRA_TIMESTAMP"

    /**
     * Tiered Response Levels
     */
    enum class ResponseTier {
        ALERT,   // Level 1: Notify user
        BLOCK,   // Level 2: Prevent execution
        LOCKDOWN // Level 3: Full device lock
    }
}
