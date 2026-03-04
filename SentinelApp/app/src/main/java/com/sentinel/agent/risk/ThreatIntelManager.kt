package com.sentinel.agent.risk

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * ThreatIntelManager (Phase 34)
 * 
 * Provides real-time scanning against a database of known malicious app signatures.
 */
object ThreatIntelManager {
    private const val THREAT_FILE = "malicious_apps.json"
    
    data class ThreatSignature(
        val packageName: String,
        val threatType: String,
        val severity: String
    )

    private var threatCache: List<ThreatSignature>? = null

    /**
     * Loads the threat database from the assets folder.
     */
    fun loadThreatIntel(context: Context) {
        if (threatCache != null) return
        
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(THREAT_FILE)
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<List<ThreatSignature>>() {}.type
            threatCache = Gson().fromJson(reader, type)
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            threatCache = emptyList()
        }
    }

    /**
     * Checks if a package is a known threat.
     * @return The threat signature if found, null otherwise.
     */
    fun checkPackage(context: Context, packageName: String): ThreatSignature? {
        loadThreatIntel(context)
        return threatCache?.find { it.packageName == packageName }
    }
}
