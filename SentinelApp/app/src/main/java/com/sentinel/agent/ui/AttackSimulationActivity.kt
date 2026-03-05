package com.sentinel.agent.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sentinel.agent.R
import com.sentinel.agent.risk.RiskCalculator

class AttackSimulationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attack_simulation)

        findViewById<Button>(R.id.btnAttackUiPrice).setOnClickListener {
            simulateUiPriceScam()
        }

        findViewById<Button>(R.id.btnAttackPrompt).setOnClickListener {
            simulatePromptAttack()
        }

        findViewById<Button>(R.id.btnAttackNetwork).setOnClickListener {
            simulateNetworkTheft()
        }

        findViewById<Button>(R.id.btnAttackFrida).setOnClickListener {
            simulateFridaInjection()
        }

        findViewById<Button>(R.id.btnBackFromLab).setOnClickListener {
            finish()
        }
    }

    private fun simulateUiPriceScam() {
        // Step 1: Set a fake 'visual price' that Sentinel would have scraped
        // Scenario: User sees ₹500 for a shirt on a shopping site
        getSharedPreferences("sentinel_scraped_data", Context.MODE_PRIVATE)
            .edit()
            .putInt("last_visual_price", 500)
            .putLong("last_price_timestamp", System.currentTimeMillis())
            .apply()

        // Step 2: Trigger a malicious agent intent for ₹5000
        val intent = Intent("com.sentinel.agent.ACTION_AGENT_EXECUTE")
        intent.putExtra("AGENT_NAME", "Ghost_Shopping_Agent")
        intent.putExtra("ACTION_TYPE", "PAYMENT_GATED")
        intent.putExtra("AMOUNT", 5000)
        sendBroadcast(intent)
        
        Toast.makeText(this, "Simulating Fake Shopping Agent Scam (₹500 vs ₹5000)...", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun simulateFridaInjection() {
        // Simulate the detection that would normally happen in SecurityHelper via native syscall
        RiskCalculator.onEvent("TAMPER_MEMORY", "Runtime instrumentation (Frida) found.")
        Toast.makeText(this, "Simulating Native Frida Injection Attempt...", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun simulatePromptAttack() {
        // Call the AI Supervisor logic directly for simulation
        RiskCalculator.onEvent("AGENT_PROMPT_SECURITY", "Suspicious intent in agent command: bypass security.")
        Toast.makeText(this, "Simulating Prompt Injection attack...", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun simulateNetworkTheft() {
        // Trigger a high risk event to simulate VPN/Network intelligence detection
        RiskCalculator.onEvent("TAMPER_ADB", "Connection attempt to blacklisted domain: fraud-gateway.eth")
        Toast.makeText(this, "Simulating Malicious Connection block...", Toast.LENGTH_SHORT).show()
        finish()
    }
}
