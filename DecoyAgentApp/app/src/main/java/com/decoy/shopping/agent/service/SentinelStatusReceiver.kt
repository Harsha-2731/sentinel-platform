package com.decoy.shopping.agent.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SentinelStatusReceiver(private val onViolation: (String) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.sentinel.agent.POLICY_VIOLATION") {
            val status = intent.getStringExtra("STATUS") ?: "blocked"
            val reason = intent.getStringExtra("REASON") ?: "Unknown Policy Violation"
            Log.e("SentinelStatusReceiver", "POLCIY VIOLATION RECEIVED: $status ($reason)")
            onViolation(reason)
        }
    }
}
