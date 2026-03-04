package com.sentinel.agent.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.util.Log
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Locale

/**
 * IdentityVerifier (EDR Part 1)
 * 
 * Provides cryptographic verification of AI Agent actions using ECDSA signatures.
 * This ensures that the intent received by Sentinel actually originated from a 
 * trusted and signed agent, preventing malicious intent spoofing.
 */
object IdentityVerifier {
    private const val TAG = "IdentityVerifier"
    
    // In Phase 26, these will be moved to the Backend Secure Agent Registry
    // For now, we whitelist a PoC Shopping Agent key
    private val trustedKeys = mapOf(
        "com.ai.shopping.agent" to "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE9erU8xRDGj+xbSbqaJ83UHLN17EXREKHh3rwdOVY1+C8cms5XIKqIjfZsvpeKmyCsMylH7XU2k8KGILEptaGWg=="
    )

    /**
     * V33 Hardening: Multi-factor Device Fingerprinting
     * Combines multiple hardware attributes into a SHA-256 hash to prevent spoofing.
     */
    fun calculateDeviceFingerprint(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
        val buildFingerprint = Build.FINGERPRINT ?: ""
        val buildModel = Build.MODEL ?: ""
        val buildManufacturer = Build.MANUFACTURER ?: ""
        
        val rawIdentity = "$androidId:$buildFingerprint:$buildModel:$buildManufacturer"
        return sha256(rawIdentity)
    }

    /**
     * V34 Hardening: Generic SHA-256 helper for Hash Chaining
     */
    fun sha256(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(input.toByteArray())
            hash.joinToString("") { "%02x".format(it) }.lowercase(Locale.ROOT)
        } catch (e: Exception) {
            Log.e(TAG, "Hash error: ${e.message}")
            ""
        }
    }

    /**
     * Verifies the authenticity of an agent action.
     * @param agentId The unique ID of the agent (e.g., package name)
     * @param payload The raw string data of the action (e.g., "PAYMENT:4500:1712345678")
     * @param signatureBase64 The ECDSA signature encoded in Base64
     */
    fun verifyAgentAction(
        agentId: String,
        payload: String,
        signatureBase64: String?
    ): Boolean {
        if (signatureBase64 == null) {
            Log.w(TAG, "Rejecting action from $agentId: Missing signature.")
            return false
        }

        return try {
            val publicKeyStr = trustedKeys[agentId] ?: run {
                Log.w(TAG, "Rejecting action from $agentId: Agent ID not in trusted registry.")
                return false
            }
            
            val publicKey = getPublicKeyFromBase64(publicKeyStr)
            
            val signatureBytes = Base64.decode(signatureBase64, Base64.DEFAULT)
            val sig = Signature.getInstance("SHA256withECDSA")
            sig.initVerify(publicKey)
            sig.update(payload.toByteArray())
            
            val result = sig.verify(signatureBytes)
            if (!result) {
                Log.e(TAG, "CRITICAL: Signature mismatch for agent $agentId!")
            }
            result
        } catch (e: Exception) {
            Log.e(TAG, "Signature verification error for $agentId: ${e.message}")
            false
        }
    }

    private fun getPublicKeyFromBase64(base64Key: String): PublicKey {
        val keyBytes = Base64.decode(base64Key, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(keyBytes)
        val kf = KeyFactory.getInstance("EC")
        return kf.generatePublic(spec)
    }
}
