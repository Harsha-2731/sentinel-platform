package com.sentinel.agent.ui

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.sentinel.agent.R
import java.util.concurrent.Executor

class SettingsActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var authPassed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        executor = ContextCompat.getMainExecutor(this)

        setupButtonListeners()

        // Check if biometrics/PIN is available before forcing the prompt
        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                // Hardware & enrollment confirmed — show the prompt
                setupBiometricPrompt()
                biometricPrompt.authenticate(promptInfo)
            }
            else -> {
                // Emulator / No enrollment fallback — allow access with a warning
                authPassed = true
                Toast.makeText(
                    this,
                    "⚠ Debug Mode: No lock screen enrolled. Settings unlocked for audit.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupButtonListeners() {
        val saveBtn = findViewById<Button>(R.id.saveSettingsBtn)
        val maxSpend = findViewById<EditText>(R.id.maxSpendInput)
        val hcmCheck = findViewById<android.widget.CheckBox>(R.id.darkModeCheck)

        saveBtn.setOnClickListener {
            val limit = maxSpend.text.toString()
            val isHighContrast = hcmCheck.isChecked

            val prefs = getSharedPreferences("sentinel_prefs", MODE_PRIVATE)
            prefs.edit()
                .putString("max_agent_spend", limit)
                .putBoolean("high_contrast_mode", isHighContrast)
                .apply()

            Toast.makeText(this, "Policy Updated: Spending Limit ₹$limit", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.activateAccessibilityBtn).setOnClickListener {
            val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            Toast.makeText(this, "Please enable 'Sentinel Accessibility Service'", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.activateAdminBtn).setOnClickListener {
            val componentName = android.content.ComponentName(
                this,
                com.sentinel.agent.service.monitoring.SentinelAdminReceiver::class.java
            )
            val intent = Intent(android.app.admin.DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(android.app.admin.DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                putExtra(
                    android.app.admin.DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "Sentinel Elite requires Admin privileges to prevent unauthorized uninstallation."
                )
            }
            startActivity(intent)
        }
    }

    private fun setupBiometricPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Fixed: use actual errString value (not escaped template)
                    Toast.makeText(
                        applicationContext,
                        "Auth Error ($errorCode): $errString",
                        Toast.LENGTH_LONG
                    ).show()
                    // Only close if it's a real failure, not just "cancelled"
                    // Error code 13 = BIOMETRIC_ERROR_CANCELED, 10 = user cancel
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        finish()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    authPassed = true
                    Toast.makeText(applicationContext, "✅ Identity Verified. Access Granted.", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Agent Authority Verification")
            .setSubtitle("Authenticate to modify Sentinel policies")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
    }
}
