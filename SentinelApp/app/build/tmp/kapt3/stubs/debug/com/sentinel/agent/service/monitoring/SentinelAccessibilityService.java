package com.sentinel.agent.service.monitoring;

/**
 * V38: Sentinel Accessibility Service (The "Eyes" of the Guardian)
 *
 * Provides deep UI inspection to detect:
 * 1. Phishing URLs in browsers.
 * 2. Clipboard hijacking.
 * 3. Suspicious UI overlays/scraping.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u0004H\u0016J\b\u0010\r\u001a\u00020\u0004H\u0014J\u0012\u0010\u000e\u001a\u00020\u00042\b\u0010\u000f\u001a\u0004\u0018\u00010\u0006H\u0002\u00a8\u0006\u0010"}, d2 = {"Lcom/sentinel/agent/service/monitoring/SentinelAccessibilityService;", "Landroid/accessibilityservice/AccessibilityService;", "()V", "inspectWindow", "", "rootNode", "Landroid/view/accessibility/AccessibilityNodeInfo;", "packageName", "", "onAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onInterrupt", "onServiceConnected", "scrapePrices", "node", "app_debug"})
public final class SentinelAccessibilityService extends android.accessibilityservice.AccessibilityService {
    
    public SentinelAccessibilityService() {
        super();
    }
    
    @java.lang.Override
    public void onAccessibilityEvent(@org.jetbrains.annotations.NotNull
    android.view.accessibility.AccessibilityEvent event) {
    }
    
    private final void inspectWindow(android.view.accessibility.AccessibilityNodeInfo rootNode, java.lang.String packageName) {
    }
    
    private final void scrapePrices(android.view.accessibility.AccessibilityNodeInfo node) {
    }
    
    @java.lang.Override
    public void onInterrupt() {
    }
    
    @java.lang.Override
    protected void onServiceConnected() {
    }
}