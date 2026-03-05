package com.sentinel.agent.ui

import android.os.Bundle
import android.os.Handler
import android.content.Intent
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.sentinel.agent.R
import com.sentinel.agent.service.emergency.EmergencyManager

class EmergencyActivity : AppCompatActivity() {

    private var countdown = 60 // V60 Fix: 60-second auto-expiration
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var countdownText: TextView
    private lateinit var statusText: TextView
    private lateinit var reasoning: String
    private val emergencyManager = EmergencyManager()

    private var isCountdownPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        countdownText = findViewById(R.id.countdownText)
        statusText = findViewById(R.id.biometricStatusText)
        val cancelBtn = findViewById<Button>(R.id.cancelEmergencyBtn)

        reasoning = intent.getStringExtra("REASONING") ?: "Critical Security Anomaly detected. Revoking Agent authorities."
        findViewById<TextView>(R.id.emergencyReasoningText).text = reasoning

        // V38: Enable App Pinning (Lock Task Mode) for Inescapable Lockdown
        try {
            startLockTask()
            Toast.makeText(this, "Sentinel Lockdown: System Pinned.", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Lockdown Warning: Enhanced Pinning Restricted.", Toast.LENGTH_SHORT).show()
        }

        startCountdown()

        cancelBtn.setOnClickListener {
            checkAndPromptBiometric()
        }
    }

    private fun checkAndPromptBiometric() {
        val biometricManager = BiometricManager.from(this)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        
        // V51: Robust Fallback - If strict biometrics fail (missing hardware, no fingers enrolled), 
        // ALWAYS prompt with DEVICE_CREDENTIAL (PIN/Password) instead of failing silently.
        isCountdownPaused = true
        showBiometricPrompt() 
    }

    private fun showBiometricPrompt() {
        val executor = androidx.core.content.ContextCompat.getMainExecutor(this)
        val biometricPrompt = androidx.biometric.BiometricPrompt(this, executor,
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    isCountdownPaused = false
                    statusText.text = "ERROR: \$errString"
                    statusText.setTextColor(android.graphics.Color.RED)
                    statusText.visibility = android.view.View.VISIBLE
                    Toast.makeText(applicationContext, "Auth Error: \$errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    handler.removeCallbacksAndMessages(null)
                    statusText.text = "VERIFIED"
                    statusText.setTextColor(android.graphics.Color.GREEN)
                    statusText.visibility = android.view.View.VISIBLE
                    sendBroadcast(Intent("com.sentinel.agent.RESET_EMERGENCY"))
                    Toast.makeText(applicationContext, "Identity Verified. Lockdown Overridden.", Toast.LENGTH_SHORT).show()
                    try {
                        stopLockTask()
                    } catch (e: Exception) {}
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    isCountdownPaused = false
                    statusText.text = "VERIFICATION FAILED"
                    statusText.setTextColor(android.graphics.Color.RED)
                    statusText.visibility = android.view.View.VISIBLE
                    Toast.makeText(applicationContext, "Identity Verification Failed.", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Sentinel Identity Verification")
            .setSubtitle("Authenticate to override security block")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    }

    private fun startCountdown() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isCountdownPaused) {
                    handler.postDelayed(this, 1000)
                    return
                }

                if (countdown > 0) {
                    countdown--
                    countdownText.text = countdown.toString()
                    handler.postDelayed(this, 1000)
                } else {
                    // V60 Fix: Watchdog auto-release to prevent infinite permanent lock
                    countdownText.text = "AUTO-RELEASE"
                    countdownText.setTextColor(android.graphics.Color.GREEN)
                    // Prepend the status instead of overwriting the detailed reason
                    findViewById<TextView>(R.id.emergencyReasoningText).text = "EXPIRED: \$reasoning"
                    
                    Toast.makeText(applicationContext, "Lockdown Auto-Expired (Watchdog Release).", Toast.LENGTH_LONG).show()
                    
                    try {
                        stopLockTask()
                    } catch (e: Exception) {}
                    
                    // Reset emergency state so user isn't permanently locked out of phone
                    sendBroadcast(Intent("com.sentinel.agent.RESET_EMERGENCY"))
                    finish()
                }
            }
        }, 1000)
    }

    override fun onBackPressed() {
        // Disable back button to prevent trivial bypass of lockdown
    }
}
