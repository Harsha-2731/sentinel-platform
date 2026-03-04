package com.sentinel.agent.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sentinel.agent.R
import com.sentinel.agent.network.client.RetrofitClient
import com.sentinel.agent.utils.SecurityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var securityHelper: SecurityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        securityHelper = SecurityHelper(this)

        val nameInput = findViewById<EditText>(R.id.regNameInput)
        val emailInput = findViewById<EditText>(R.id.regEmailInput)
        val passwordInput = findViewById<EditText>(R.id.regPasswordInput)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val backToLoginText = findViewById<TextView>(R.id.backToLoginText)

        backToLoginText.setOnClickListener { finish() }

        registerBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString().trim()
            val pass = passwordInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        registerBtn.isEnabled = false // Disable at start
                        
                        val (deviceFingerprint, _) = withContext(Dispatchers.IO) {
                            // V33: Transition from plain ANDROID_ID to Multi-factor Fingerprint
                            val fingerprint = com.sentinel.agent.utils.IdentityVerifier.calculateDeviceFingerprint(this@RegisterActivity)
                            
                            // V33: Initialize hardware security anchor
                            securityHelper.ensureHardwareKeyExists()
                            Pair(fingerprint, true)
                        }
                        
                        val request = com.sentinel.agent.data.model.RegistrationRequest(
                            name = name,
                            email = email,
                            password = pass,
                            deviceId = deviceFingerprint,
                            riskPreference = "high"
                        )
                        
                        val apiService = RetrofitClient.createService()
                        val response = withContext(Dispatchers.IO) {
                            apiService.registerUser(request)
                        }
                        
                        if (response.isSuccessful && response.body() != null) {
                            val user = response.body()!!
                            
                            // Save JWT and HMAC dynamically 
                            user.token?.let { securityHelper.saveJwtToken(it) }
                            user.hmacSecret?.let { securityHelper.saveHmacSecret(it) }

                            Toast.makeText(this@RegisterActivity, "Device Provisioned!", Toast.LENGTH_SHORT).show()
                            
                            val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Registration Failed: \${response.message()}", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        val errorDetail = e.message ?: "Unknown Registration Error"
                        Log.e("RegisterActivity", "Network error: $errorDetail", e)
                        Toast.makeText(this@RegisterActivity, "Network Error: $errorDetail", Toast.LENGTH_LONG).show()
                    } finally {
                        registerBtn.isEnabled = true // Always re-enable
                    }
                }
            } else {
                Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
