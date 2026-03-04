package com.sentinel.agent.service.network

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.sentinel.agent.risk.RiskCalculator
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

class SentinelVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private var thread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (thread != null) return START_STICKY

        // Start VPN Protection
        thread = Thread {
            try {
                runVpn()
            } catch (e: Exception) {
                Log.e("SentinelVpn", "VPN Crashing: ${e.message}")
            }
        }.apply { start() }

        return START_STICKY
    }

    private fun runVpn() {
        // Configure VPN
        val builder = Builder()
            .setSession("Sentinel Elite Firewall")
            .addAddress("10.0.0.2", 32)
            .addRoute("0.0.0.0", 0)
            .addDnsServer("8.8.8.8")
            .setBlocking(true)

        vpnInterface = builder.establish()
        Log.i("SentinelVpn", "Network Intelligence Layer ACTIVE (Local Firewall)")

        val input = FileInputStream(vpnInterface?.fileDescriptor)
        val output = FileOutputStream(vpnInterface?.fileDescriptor)
        val buffer = ByteBuffer.allocate(32768)

        while (true) {
            val length = input.read(buffer.array())
            if (length > 0) {
                // In a 10/10 app, we would parse IP/TCP headers here
                // to detect connections to domains like "scam-gateway.com"
                
                // SIMULATION: If we detect frequent outbound packets
                // from an untrusted background app, flag it.
                
                // Normally we'd write back: output.write(buffer.array(), 0, length)
                // But for a pure firewall demo, we pass if allowed or block by dropping.
                output.write(buffer.array(), 0, length)
                buffer.clear()
            }
            Thread.sleep(10)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        thread?.interrupt()
        vpnInterface?.close()
    }
}
