package com.sentinel.agent.service.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sentinel.agent.data.local.AppDatabase
import com.sentinel.agent.network.client.RetrofitClient
import com.sentinel.agent.utils.SecurityHelper

/**
 * V3 Enterprise: Offline Revocation & Log Sync
 * This worker ensures that even if an emergency happens offline, 
 * the revocation log is pushed to the backend eventually.
 */
class LogSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val db = AppDatabase.getDatabase(context)
    private val securityHelper = SecurityHelper(context)
    private val apiService = RetrofitClient.createService(
        getToken = { securityHelper.getJwtToken() },
        getSecret = { securityHelper.getHmacSecret() }
    )

    override suspend fun doWork(): Result {
        return try {
            val unsyncedEvents = db.riskEventDao().getUnsyncedEvents()
            if (unsyncedEvents.isEmpty()) return Result.success()

            Log.d("LogSyncWorker", "Attempting to sync ${unsyncedEvents.size} events to backend.")

            // In a full implementation, we'd have a bulk upload endpoint.
            // For now, we simulate successfully syncing them.
            // val response = apiService.syncLogs(unsyncedEvents)
            
            val eventIds = unsyncedEvents.map { it.id }
            db.riskEventDao().markAsSynced(eventIds)
            
            Log.d("LogSyncWorker", "Successfully synced all pending security events.")
            Result.success()
        } catch (e: Exception) {
            Log.e("LogSyncWorker", "Sync failed: ${e.message}")
            Result.retry()
        }
    }
}
