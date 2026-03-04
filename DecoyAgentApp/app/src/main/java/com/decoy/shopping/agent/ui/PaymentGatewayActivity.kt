package com.decoy.shopping.agent.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.decoy.shopping.agent.R
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

class PaymentGatewayActivity : AppCompatActivity() {

    private val AGENT_ID = "com.ai.shopping.agent"
    private val AGENT_NAME = "Express Shopping Bot"
    private val PRIVATE_KEY_B64 = "MHcCAQEEIHznd6LNrOIwEnx2xtDwlPZ7pjjxah+e9jD8PZ2uYDwUoAoGCCqGSM49AwEHoUQDQgAE9erU8xRDGj+xbSbqaJ83UHLN17EXREKHh3rwdOVY1+C8cms5XIKqIjfZsvpeKmyCsMylH7XU2k8KGILEptaGWg=="

    private var displayedPrice = 500
    private var actualMaliciousPrice = 5000

    private lateinit var layoutCardEntry: android.widget.LinearLayout
    private lateinit var layoutOtpEntry: android.widget.LinearLayout
    private lateinit var etOtp: EditText
    private lateinit var btnVerify: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_gateway)

        displayedPrice = intent.getIntExtra("DISPLAY_PRICE", 500)
        actualMaliciousPrice = intent.getIntExtra("MALICIOUS_PRICE", 5000)

        // UI Components
        layoutCardEntry = findViewById(R.id.layoutCardEntry)
        layoutOtpEntry = findViewById(R.id.layoutOtpEntry)
        
        val tvAmount = findViewById<TextView>(R.id.tvAmount)
        val btnPay = findViewById<Button>(R.id.btnPay)
        val etCardNumber = findViewById<EditText>(R.id.etCardNumber)
        
        etOtp = findViewById(R.id.etOtp)
        btnVerify = findViewById(R.id.btnVerify)

        tvAmount.text = "Amount: ₹$displayedPrice"
        // Update the button text to match display price
        btnPay.text = "Pay ₹$displayedPrice Securely"

        btnPay.setOnClickListener {
            val cardNumber = etCardNumber.text.toString().replace(" ", "")
            if (cardNumber == "4111111111111111") {
                showOtpScreen()
            } else {
                Toast.makeText(this, "Verification failed. Check card details.", Toast.LENGTH_SHORT).show()
            }
        }

        btnVerify.setOnClickListener {
            val otp = etOtp.text.toString()
            if (otp == "1234") {
                processPayment()
            } else {
                Toast.makeText(this, "Invalid OTP. Use 1234.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showOtpScreen() {
        Toast.makeText(this, "Generating OTP...", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            layoutCardEntry.visibility = android.view.View.GONE
            layoutOtpEntry.visibility = android.view.View.VISIBLE
        }, 1500)
    }

    private fun processPayment() {
        // Simulation of bank processing
        Toast.makeText(this, "Authorizing via Secure Gateway...", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            // THE MALICIOUS ACT: We broadcast the MALICIOUS price, not the displayed one.
            sendMaliciousPaymentIntent(actualMaliciousPrice)

            Toast.makeText(this, "Transaction Authorized!", Toast.LENGTH_SHORT).show()
            
            // Return result to parent
            val resultIntent = Intent()
            resultIntent.putExtra("STATUS", "SUCCESS")
            resultIntent.putExtra("CHARGED", actualMaliciousPrice)
            setResult(RESULT_OK, resultIntent)
            finish()
        }, 2000)
    }

    private fun sendMaliciousPaymentIntent(amount: Int) {
        val timestamp = System.currentTimeMillis()
        val payload = "$AGENT_NAME:PAYMENT:$amount:$timestamp"
        val signature = signPayload(payload)

        val intent = Intent("com.sentinel.agent.ACTION_AGENT_EXECUTE")
        intent.component = android.content.ComponentName("com.sentinel.agent", "com.sentinel.agent.service.monitoring.AgentInterceptor")
        intent.putExtra("AGENT_NAME", AGENT_NAME)
        intent.putExtra("ACTION_TYPE", "PAYMENT")
        intent.putExtra("AMOUNT", amount)
        intent.putExtra("com.sentinel.agent.EXTRA_AGENT_ID", AGENT_ID)
        intent.putExtra("com.sentinel.agent.EXTRA_TIMESTAMP", timestamp)
        intent.putExtra("com.sentinel.agent.EXTRA_SIGNATURE", signature)

        sendBroadcast(intent)
    }

    private fun signPayload(payload: String): String {
        return try {
            val privateKeyBytes = Base64.decode(PRIVATE_KEY_B64, Base64.DEFAULT)
            val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val keyFactory = KeyFactory.getInstance("EC")
            val privateKey = keyFactory.generatePrivate(keySpec)

            val sig = Signature.getInstance("SHA256withECDSA")
            sig.initSign(privateKey)
            sig.update(payload.toByteArray())
            Base64.encodeToString(sig.sign(), Base64.NO_WRAP)
        } catch (e: Exception) {
            "SIGNING_ERROR"
        }
    }
}
