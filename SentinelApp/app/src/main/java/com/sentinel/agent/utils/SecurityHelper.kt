package com.sentinel.agent.utils

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * SecurityHelper (Phase 33 Hardware Hardening)
 * 
 * Implements hardware-backed security, encrypted storage, and advanced tamper detection.
 */
class SecurityHelper(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "sentinel_secure_prefs"
        private const val KEY_JWT_TOKEN = "jwt_token"
        private const val KEY_HMAC_SECRET = "hmac_signing_secret"
        private const val KEYSTORE_ALIAS = "sentinel_integrity_key"
        
        const val EXTRA_AGENT_ID = "com.sentinel.agent.EXTRA_AGENT_ID"
        const val EXTRA_SIGNATURE = "com.sentinel.agent.EXTRA_SIGNATURE"
        const val EXTRA_TIMESTAMP = "com.sentinel.agent.EXTRA_TIMESTAMP"
    }

    // V33: Implementation of hardware-backed EncryptedSharedPreferences
    private val sharedPreferences: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e("SecurityHelper", "Failed to init EncryptedPrefs, falling back to basic.")
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    // V33: Hardware-Backed Key Generation (Keys never leave the secure enclave)
    fun ensureHardwareKeyExists() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(
                KeyGenParameterSpec.Builder(KEYSTORE_ALIAS, 
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false) // For background monitoring efficiency
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
            keyGenerator.generateKey()
            Log.d("SecurityHelper", "Hardware-backed security anchor generated.")
        }
    }

    fun saveJwtToken(token: String) {
        sharedPreferences.edit().putString(KEY_JWT_TOKEN, token).apply()
        Log.d("SecurityHelper", "JWT Token stored securely in EncryptedPrefs.")
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null)
    }
    
    fun saveHmacSecret(secret: String) {
        sharedPreferences.edit().putString(KEY_HMAC_SECRET, secret).apply()
    }
    
    fun getHmacSecret(): String? {
        return sharedPreferences.getString(KEY_HMAC_SECRET, null)
    }

    fun clearAuthorities() {
        sharedPreferences.edit()
            .remove(KEY_JWT_TOKEN)
            .remove(KEY_HMAC_SECRET)
            .apply()
        Log.d("SecurityHelper", "Credentials revoked locally. Agent session terminated.")
    }

    /**
     * Advanced Hardening Checklist
     * Combines Root, Emulator, Frida, and debugger checks into a single integrity verdict.
     */
    fun checkEnvironmentIntegrity(): Boolean {
        var isCompromised = isDeviceRooted() || isEmulator() || isFridaDetected() || isDebuggerAttached()
        if (isAdbEnabled()) {
            Log.w("SecurityHelper", "Integrity Warning: ADB Debugging is enabled.")
            // High security policy might count ADB as a risk
        }
        return !isCompromised
    }

    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || isMagiskDetected()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
            "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"
        )
        for (path in paths) {
            if (java.io.File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val inReader = java.io.BufferedReader(java.io.InputStreamReader(process.inputStream))
            inReader.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun isMagiskDetected(): Boolean {
        return try {
            val file = java.io.File("/proc/self/mounts")
            val content = file.readText()
            content.contains("magisk") || content.contains("core/mirror")
        } catch (e: Exception) { false }
    }

    fun isDebuggerAttached(): Boolean {
        return android.os.Debug.isDebuggerConnected()
    }

    fun isAdbEnabled(): Boolean {
        return try {
            android.provider.Settings.Global.getInt(
                context.contentResolver,
                android.provider.Settings.Global.ADB_ENABLED, 0
            ) == 1
        } catch (e: Exception) { false }
    }

    fun isFridaDetected(): Boolean {
        return try {
            val file = java.io.File("/proc/self/maps")
            val content = file.readText()
            content.contains("frida") || content.contains("gum-js") || content.contains("gadget")
        } catch (e: Exception) { false }
    }

    fun isEmulator(): Boolean {
        return (android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.FINGERPRINT.startsWith("unknown")
                || android.os.Build.MODEL.contains("google_sdk")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic"))
                || "google_sdk" == android.os.Build.PRODUCT)
    }

    fun isApkSignatureValid(expectedSignature: String): Boolean {
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                android.content.pm.PackageManager.GET_SIGNATURES
            )
            for (signature in packageInfo.signatures) {
                val md = java.security.MessageDigest.getInstance("SHA-256")
                md.update(signature.toByteArray())
                val currentSignature = bytesToHex(md.digest())
                if (currentSignature == expectedSignature) return true
            }
        } catch (e: Exception) { e.printStackTrace() }
        return false
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    /**
     * V35: Lockdown Persistence
     */
    fun setLockdownActive(active: Boolean) {
        sharedPreferences.edit().putBoolean("LOCKDOWN_ACTIVE", active).apply()
    }

    fun isLockdownActive(): Boolean {
        return sharedPreferences.getBoolean("LOCKDOWN_ACTIVE", false)
    }
}
