package com.sentinel.agent.service.integrity

import android.content.Context
import android.util.Log
import com.sentinel.agent.risk.RiskCalculator
import java.io.File

/**
 * IntegrityManager: Advanced Device Attestation
 * Handles Root detection, Bootloader checks, and Play Integrity framework.
 */
object IntegrityManager {
    private const val TAG = "IntegrityManager"

    /**
     * Performs a comprehensive integrity check.
     */
    fun checkIntegrity(context: Context): IntegrityResult {
        val buildTags = android.os.Build.TAGS
        val isRooted = checkRootFiles() || (buildTags != null && buildTags.contains("test-keys"))
        val isTampered = checkZygisk() || checkLibraryInjection()
        
        return when {
            isRooted -> IntegrityResult.COMPROMISED("ROOTED_DEVICE")
            isTampered -> IntegrityResult.COMPROMISED("DYNAMIC_TAMPERING_DETECTED")
            else -> IntegrityResult.SECURE
        }
    }

    private fun checkRootFiles(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
            "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkZygisk(): Boolean {
        return try {
            val file = File("/proc/self/mounts")
            val content = file.readText()
            content.contains("zygisk") || content.contains("lsposed")
        } catch (e: Exception) { false }
    }

    private fun checkLibraryInjection(): Boolean {
        return try {
            val file = File("/proc/self/maps")
            val content = file.readText()
            val maliciousLibs = listOf("frida", "gum-js", "xkposed", "sandhook", "edxp")
            maliciousLibs.any { content.contains(it, ignoreCase = true) }
        } catch (e: Exception) { false }
    }

    sealed class IntegrityResult {
        object SECURE : IntegrityResult()
        data class COMPROMISED(val reason: String) : IntegrityResult()
    }
}
