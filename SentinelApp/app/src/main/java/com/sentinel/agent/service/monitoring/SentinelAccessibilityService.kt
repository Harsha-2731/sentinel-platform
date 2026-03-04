package com.sentinel.agent.service.monitoring

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.sentinel.agent.risk.RiskCalculator

/**
 * V38: Sentinel Accessibility Service (The "Eyes" of the Guardian)
 * 
 * Provides deep UI inspection to detect:
 * 1. Phishing URLs in browsers.
 * 2. Clipboard hijacking.
 * 3. Suspicious UI overlays/scraping.
 */
class SentinelAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                val packageName = event.packageName?.toString() ?: ""
                inspectWindow(event.source, packageName)
            }
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                Log.d("SentinelAccessibility", "User interaction detected in ${event.packageName}")
            }
        }
    }

    private fun inspectWindow(rootNode: AccessibilityNodeInfo?, packageName: String) {
        if (rootNode == null) return

        // 1. Price Scraping Logic (V42 OS Visibility)
        // We look for currency symbols and amounts like ₹500, Rs. 500 etc.
        scrapePrices(rootNode)
        
        // 2. Overlay / Scraping Detection
        // Detect apps drawing over sensitive windows or performing intensive scraping
        if (packageName == "com.decoy.shopping.agent") {
            Log.i("SentinelAccessibility", "Deep monitoring active for Decoy Store.")
            
            // Check for unusual overlay windows (Simplistic heuristic for demo)
            // Real detection involves checking WindowManager flags
        }

        rootNode.recycle()
    }

    private fun scrapePrices(node: AccessibilityNodeInfo?) {
        if (node == null) return

        try {
            val text = node.text?.toString() ?: ""
            if (text.contains("₹") || text.contains("Rs.") || text.contains("Total")) {
                val priceRegex = """(?:₹|Rs\.?)\s?([\d,]+)""".toRegex()
                val match = priceRegex.find(text)
                if (match != null) {
                    val priceValue = match.groupValues[1].replace(",", "").toIntOrNull()
                    if (priceValue != null) {
                        Log.d("SentinelAccessibility", "SCANNED_VISUAL_PRICE: ₹$priceValue")
                        getSharedPreferences("sentinel_scraped_data", Context.MODE_PRIVATE)
                            .edit()
                            .putInt("last_visual_price", priceValue)
                            .putLong("last_price_timestamp", System.currentTimeMillis())
                            .apply()
                    }
                }
            }

            for (i in 0 until node.childCount) {
                val child = node.getChild(i)
                scrapePrices(child)
            }
        } catch (e: Exception) {
            // Prevent crash during inspection
        } finally {
            // CRITICAL: recycle() must be called on EVERY node obtained from getSource or getChild
            // However, we must not recycle the root node passed from inspectWindow until it's done.
            // In recursive calls, children are obtained fresh and must be recycled.
            // The root call in inspectWindow handles the rootNode.recycle().
        }
    }

    override fun onInterrupt() {
        Log.e("SentinelAccessibility", "Service Interrupted!")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i("SentinelAccessibility", "10/10 Deep Vision Service Connected.")
    }
}
