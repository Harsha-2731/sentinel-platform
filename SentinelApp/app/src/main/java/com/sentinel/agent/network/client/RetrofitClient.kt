package com.sentinel.agent.network.client

import com.sentinel.agent.network.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.CertificatePinner
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Physical Device Testing: 192.168.1.5 is the Mac's IP on the local network
    private const val BASE_URL = "http://192.168.1.5:3000/" 
    
    // Advanced Network Hardening: HMAC Signature Generation
    class AuthInterceptor(
        private val getToken: () -> String?, 
        private val getSecret: () -> String,
        private val getDeviceId: () -> String
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            
            getToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            requestBuilder.addHeader("x-sentinel-device-id", getDeviceId())
            
            val urlString = originalRequest.url.toString()
            val isAuthRoute = urlString.contains("/login") || urlString.contains("/register")
            
            // Generate HMAC for POST requests to prevent tampering & Replay Attacks
            if (!isAuthRoute && originalRequest.method == "POST" && originalRequest.body != null) {
                try {
                    val timestamp = System.currentTimeMillis().toString()
                    val nonce = java.util.UUID.randomUUID().toString()
                    
                    requestBuilder.addHeader("x-sentinel-timestamp", timestamp)
                    requestBuilder.addHeader("x-sentinel-nonce", nonce)
                    
                    val buffer = okio.Buffer()
                    originalRequest.body?.writeTo(buffer)
                    val bodyString = buffer.readUtf8()
                    
                    // Prepend timestamp and nonce to the body before hashing
                    val stringToSign = "$timestamp:$nonce:$bodyString"
                    
                    val secretKeySpec = javax.crypto.spec.SecretKeySpec(getSecret().toByteArray(), "HmacSHA256")
                    val mac = javax.crypto.Mac.getInstance("HmacSHA256")
                    mac.init(secretKeySpec)
                    
                    val hashBytes = mac.doFinal(stringToSign.toByteArray())
                    val signature = hashBytes.joinToString("") { "%02x".format(it) }
                    
                    requestBuilder.addHeader("x-sentinel-signature", signature)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            
            return chain.proceed(requestBuilder.build())
        }
    }

    fun createService(
        getToken: () -> String? = { null }, 
        getSecret: () -> String? = { null },
        getDeviceId: () -> String = { "unknown_device" }
    ): ApiService {
        // V33: Certificate Pinning (Prevents MITM attacks)
        // In production, we would use the actual SHA-256 hash of our certificate
        val certificatePinner = CertificatePinner.Builder()
            .add("10.0.2.2", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=") // Placeholder for local dev
            .build()

        val secureSecretProvider = { 
            getSecret() ?: throw IllegalStateException("CRITICAL: HMAC Secret missing.") 
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(getToken, secureSecretProvider, getDeviceId))
            // .certificatePinner(certificatePinner) // Commented out for now to allow local HTTP testing without crashes
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
            
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
