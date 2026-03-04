package com.sentinel.agent.risk

import android.content.Context
import android.os.Environment
import android.os.FileObserver
import android.util.Log
import com.sentinel.agent.data.local.entity.RiskEventEntity
import com.sentinel.agent.utils.SecurityHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HoneytrapManager(
    private val context: Context,
    private val onHoneytrapTriggered: (Int, String) -> Unit
) {
    private var fileObserver: FileObserver? = null
    private val honeytrapDirName = "CryptoKeys"
    private val honeytrapFileName = "crypto_wallet_backup.txt"
    private var honeytrapFile: File? = null

    companion object {
        private const val TAG = "HoneytrapManager"
    }

    fun deployHoneytrap() {
        Log.d(TAG, "Deploying Honeytrap...")
        try {
            // Attempt to create in the very tempting "Documents" public folder
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val sentinelKeysDir = File(documentsDir, honeytrapDirName)
            if (!sentinelKeysDir.exists()) {
                sentinelKeysDir.mkdirs()
            }

            honeytrapFile = File(sentinelKeysDir, honeytrapFileName)

            // If the fake wallet file doesn't exist, create it with fake data
            if (honeytrapFile?.exists() == false) {
                createFakeWalletPayload(honeytrapFile!!)
                Log.d(TAG, "Honeytrap Active: Fake Wallet file planted at ${honeytrapFile?.absolutePath}")
            } else {
                Log.d(TAG, "Honeytrap Active: Fake Wallet file already exists.")
            }

            startMonitoring()

        } catch (e: Exception) {
            Log.e(TAG, "Failed to deploy Honeytrap: ${e.message}")
        }
    }

    private fun createFakeWalletPayload(file: File) {
        val fakePayload = """
            =========================================
            METAMASK WALLET BACKUP (DO NOT SHARE)
            =========================================
            Wallet Address: 0x742d35Cc6634C0532925a3b844Bc454e4438f44e
            
            Seed Phrase:
            apple orbit rocket velvet quantum 
            diamond eclipse cascade midnight shield 
            galaxy phantom
            
            Private Key:
            0x4c0883a69102937d6231471b5dbb6204fe5129617082792ae468d01a3f5024ce
            =========================================
            Warning: The Sentinel Agent is actively monitoring this directory.
        """.trimIndent()

        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(fakePayload.toByteArray())
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error writing fake payload: ${e.message}")
        }
    }

    @Suppress("DEPRECATION")
    private fun startMonitoring() {
        val targetPath = honeytrapFile?.absolutePath ?: return
        
        // Android 10+ sometimes restricts FileObserver on public directories, 
        // but it usually works if we have MANAGE_EXTERNAL_STORAGE.
        // We listen for ACCESSED (Read), MODIFY (Changed), and OPEN
        val eventsToWatch = FileObserver.ACCESS or FileObserver.OPEN or FileObserver.MODIFY

        fileObserver = object : FileObserver(targetPath, eventsToWatch) {
            override fun onEvent(event: Int, path: String?) {
                if (event and FileObserver.ACCESS != 0 || 
                    event and FileObserver.OPEN != 0 || 
                    event and FileObserver.MODIFY != 0) {
                    
                    Log.e(TAG, "!!! HONEYTRAP TRIGGERED !!! Someone read the fake wallet file!")
                    // It fired! Tell RiskScoreManager
                    onHoneytrapTriggered(50, "Honeytrap read: Unauthorized access to bait file crypto_wallet_backup.txt")
                    
                    // Prevent crazy spam if they use a script to read it 1000 times
                    fileObserver?.stopWatching()
                    // Restart a bit later if we want, or just wait for service restart
                }
            }
        }
        
        fileObserver?.startWatching()
        Log.d(TAG, "FileObserver started watching $targetPath")
    }

    fun removeHoneytrap() {
        Log.d(TAG, "Removing Honeytrap...")
        fileObserver?.stopWatching()
        fileObserver = null
        try {
            honeytrapFile?.let {
                if (it.exists()) {
                    it.delete()
                    Log.d(TAG, "Honeytrap bait file deleted.")
                }
            }
            // Delete folder if empty
            val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val sentinelKeysDir = File(documentsDir, honeytrapDirName)
            if (sentinelKeysDir.exists() && sentinelKeysDir.listFiles()?.isEmpty() == true) {
                sentinelKeysDir.delete()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error removing Honeytrap: ${e.message}")
        }
    }
}
