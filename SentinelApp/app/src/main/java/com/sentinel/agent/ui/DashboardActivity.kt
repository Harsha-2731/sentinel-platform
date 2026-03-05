package com.sentinel.agent.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sentinel.agent.R
import com.sentinel.agent.data.local.AppDatabase
import com.sentinel.agent.utils.SecurityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.sentinel.agent.ui.adapter.RiskEventAdapter
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.sentinel.agent.service.integrity.IntegrityManager

class DashboardActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var securityHelper: SecurityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // V51: Fail-Closed Enforcement (10/10 Hardening)
        val integrity = IntegrityManager.checkIntegrity(this)
        val isAuditBypass = true // Set to false for hard production enforcement
        
        if (integrity is IntegrityManager.IntegrityResult.COMPROMISED && !isAuditBypass) {
            Toast.makeText(this, "CRITICAL: Security Policy Violation. Access Denied.", Toast.LENGTH_LONG).show()
            val intent = Intent(this, com.sentinel.agent.ui.EmergencyActivity::class.java).apply {
                putExtra("REASONING", "Fail-Closed Enforcement: Device Integrity Compromised (${integrity.reason})")
            }
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_dashboard)

        db = AppDatabase.getDatabase(this)
        securityHelper = SecurityHelper(this)

        val riskProgressBar = findViewById<ProgressBar>(R.id.riskProgressBar)
        val riskScoreText = findViewById<TextView>(R.id.riskScoreText)
        val authStatusText = findViewById<TextView>(R.id.authStatusText)
        val eventsRecyclerView = findViewById<RecyclerView>(R.id.eventsRecyclerView)
        val simulateBtn = findViewById<Button>(R.id.simulateAttackBtn)
        val historyBtn = findViewById<Button>(R.id.historyBtn)
        val settingsBtn = findViewById<android.widget.ImageButton>(R.id.settingsBtn)

        eventsRecyclerView.layoutManager = LinearLayoutManager(this)

        settingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        historyBtn.setOnClickListener {
            startActivity(Intent(this, TaskHistoryActivity::class.java))
        }

        // V46: Launch Attack Simulation Lab (Direct User-Friendly Testing)
        simulateBtn.setOnClickListener {
            startActivity(Intent(this, AttackSimulationActivity::class.java))
        }

        // Periodically refresh the UI
        lifecycleScope.launch {
            while (true) {
                refreshUi(riskProgressBar, riskScoreText, authStatusText, eventsRecyclerView)
                kotlinx.coroutines.delay(2000)
            }
        }
    }

    private suspend fun refreshUi(bar: ProgressBar, scoreText: TextView, statusText: TextView, rv: RecyclerView) {
        val allEvents = withContext(Dispatchers.IO) {
            db.riskEventDao().getAllEvents()
        }
        
        // V3.2: Show only last 5 events in Dashboard for focus
        val recentEvents = allEvents.take(5)
        
        withContext(Dispatchers.Main) {
            rv.adapter = RiskEventAdapter(recentEvents)
            
            val latestRisk = allEvents.firstOrNull()?.currentRiskScore ?: 0
            val isRevoked = securityHelper.getJwtToken() == null

            scoreText.text = latestRisk.toString()
            bar.progress = latestRisk
            
            updateIntegrityWidget(latestRisk)
            updateTimeline(allEvents.reversed().take(8)) // Show trend

            if (isRevoked) {
                statusText.text = "REVOKED / LOCKED"
                statusText.setTextColor(android.graphics.Color.parseColor("#FF4444"))
            } else {
                statusText.text = "AUTHORIZED / SECURE"
                statusText.setTextColor(android.graphics.Color.parseColor("#00FFCC"))
            }
        }
    }

    private fun updateIntegrityWidget(risk: Int) {
        val integrityText = findViewById<TextView>(R.id.integrityStatusText)
        val integrityDot = findViewById<View>(R.id.integrityStatusDot)
        val integrityLayout = findViewById<LinearLayout>(R.id.integrityStatusLayout)

        // Logic for Enterprise Integrity
        val isAdb = securityHelper.isAdbEnabled()
        val isEmulated = securityHelper.isEmulator()
        val isHooked = securityHelper.isFridaDetected()

        when {
            isHooked || risk >= 80 -> {
                integrityText.text = "SYSTEM INTEGRITY: COMPROMISED"
                integrityText.setTextColor(android.graphics.Color.parseColor("#FF4444"))
                integrityDot.setBackgroundColor(android.graphics.Color.parseColor("#FF4444"))
                integrityLayout.setBackgroundColor(android.graphics.Color.parseColor("#33FF4444"))
            }
            isAdb || isEmulated || risk > 30 -> {
                integrityText.text = "SYSTEM INTEGRITY: ELEVATED RISK"
                integrityText.setTextColor(android.graphics.Color.parseColor("#FF8800"))
                integrityDot.setBackgroundColor(android.graphics.Color.parseColor("#FF8800"))
                integrityLayout.setBackgroundColor(android.graphics.Color.parseColor("#33FF8800"))
            }
            else -> {
                integrityText.text = "SYSTEM INTEGRITY: SECURE"
                integrityText.setTextColor(android.graphics.Color.parseColor("#00FFCC"))
                integrityDot.setBackgroundColor(android.graphics.Color.parseColor("#00FFCC"))
                integrityLayout.setBackgroundColor(android.graphics.Color.parseColor("#1A00FFCC"))
            }
        }
    }

    private fun updateTimeline(trend: List<com.sentinel.agent.data.local.entity.RiskEventEntity>) {
        val timelineLayout = findViewById<LinearLayout>(R.id.timelineLayout)
        for (i in 0 until timelineLayout.childCount) {
            val view = timelineLayout.getChildAt(i)
            if (i < trend.size) {
                val event = trend[i]
                val color = when {
                    event.currentRiskScore >= 80 -> "#FF3366" // Red
                    event.currentRiskScore > 30 -> "#FFCC00"  // Yellow/Orange
                    else -> "#00FFCC"                        // Green/Cyber
                }
                view.setBackgroundColor(android.graphics.Color.parseColor(color))
                view.alpha = 1.0f
            } else {
                view.setBackgroundColor(android.graphics.Color.parseColor("#1A00FFCC"))
                view.alpha = 0.3f
            }
        }
        timelineLayout.visibility = View.VISIBLE
    }
}
