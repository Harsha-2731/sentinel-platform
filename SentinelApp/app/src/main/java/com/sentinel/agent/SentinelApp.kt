package com.sentinel.agent

import android.app.Application
import android.content.Intent
import com.sentinel.agent.service.monitoring.MonitoringService

class SentinelApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Phase 60: Global Watchdog for Threat Engine Stability
        // Catch fatal exceptions so the app does not freeze or permanently lock the user's phone
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            // Log the fatal crash internally
            android.util.Log.e("SentinelAppWatchdog", "FATAL CRASH CAUGHT! Thread: \${thread.name}", exception)
            
            // Attempt to unstick the UI by forcing the monitoring service to restart completely
            try {
                val serviceIntent = Intent(this, MonitoringService::class.java)
                stopService(serviceIntent)
                // In a true watchdog we might startService again here, but Android limits background starts.
                // We rely on the user reopening the app or the background alarm to revive it safely.
            } catch (e: Exception) {
                android.util.Log.e("SentinelAppWatchdog", "Failed to gracefully stop service during crash", e)
            }

            // Still let the OS handle the ultimate crash teardown to avoid a zombied UI state
            defaultUncaughtExceptionHandler?.uncaughtException(thread, exception)
        }
    }
}
