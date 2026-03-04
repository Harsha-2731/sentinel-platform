package com.sentinel.agent.service.monitoring

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * V38: Sentinel Device Admin Receiver
 * 
 * Prevents unauthorized uninstallation and provides system-level management.
 */
class SentinelAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.i("SentinelAdmin", "Device Admin Privileges Granted.")
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.w("SentinelAdmin", "Device Admin Privileges Revoked! Security integrity lowered.")
    }
    
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        // Warning message when user tries to deactivate admin
        return "Disabling Sentinel Admin will expose your AI Agents to external threats and disable deep monitoring."
    }
}
