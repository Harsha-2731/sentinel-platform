package com.sentinel.agent.utils;

/**
 * SecurityHelper (Phase 33 Hardware Hardening)
 *
 * Implements hardware-backed security, encrypted storage, and advanced tamper detection.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0014\u0018\u0000 (2\u00020\u0001:\u0001(B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0006\u0010\u000f\u001a\u00020\u0010J\b\u0010\u0011\u001a\u00020\u0010H\u0002J\b\u0010\u0012\u001a\u00020\u0010H\u0002J\b\u0010\u0013\u001a\u00020\u0010H\u0002J\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0015J\b\u0010\u0017\u001a\u0004\u0018\u00010\fJ\b\u0010\u0018\u001a\u0004\u0018\u00010\fJ\u0006\u0010\u0019\u001a\u00020\u0010J\u000e\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\fJ\u0006\u0010\u001c\u001a\u00020\u0010J\u0006\u0010\u001d\u001a\u00020\u0010J\u0006\u0010\u001e\u001a\u00020\u0010J\u0006\u0010\u001f\u001a\u00020\u0010J\u0006\u0010 \u001a\u00020\u0010J\u0006\u0010!\u001a\u00020\u0010J\u000e\u0010\"\u001a\u00020\u00152\u0006\u0010#\u001a\u00020\fJ\u000e\u0010$\u001a\u00020\u00152\u0006\u0010%\u001a\u00020\fJ\u000e\u0010&\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b\u00a8\u0006)"}, d2 = {"Lcom/sentinel/agent/utils/SecurityHelper;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "sharedPreferences", "Landroid/content/SharedPreferences;", "getSharedPreferences", "()Landroid/content/SharedPreferences;", "sharedPreferences$delegate", "Lkotlin/Lazy;", "bytesToHex", "", "bytes", "", "checkEnvironmentIntegrity", "", "checkRootMethod1", "checkRootMethod2", "checkRootMethod3", "clearAuthorities", "", "ensureHardwareKeyExists", "getHmacSecret", "getJwtToken", "isAdbEnabled", "isApkSignatureValid", "expectedSignature", "isDebuggerAttached", "isDeviceRooted", "isEmulator", "isFridaDetected", "isLockdownActive", "isMagiskDetected", "saveHmacSecret", "secret", "saveJwtToken", "token", "setLockdownActive", "active", "Companion", "app_debug"})
public final class SecurityHelper {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREFS_NAME = "sentinel_secure_prefs";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_JWT_TOKEN = "jwt_token";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_HMAC_SECRET = "hmac_signing_secret";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEYSTORE_ALIAS = "sentinel_integrity_key";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_AGENT_ID = "com.sentinel.agent.EXTRA_AGENT_ID";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_SIGNATURE = "com.sentinel.agent.EXTRA_SIGNATURE";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_TIMESTAMP = "com.sentinel.agent.EXTRA_TIMESTAMP";
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy sharedPreferences$delegate = null;
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.utils.SecurityHelper.Companion Companion = null;
    
    public SecurityHelper(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    private final android.content.SharedPreferences getSharedPreferences() {
        return null;
    }
    
    public final void ensureHardwareKeyExists() {
    }
    
    public final void saveJwtToken(@org.jetbrains.annotations.NotNull
    java.lang.String token) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getJwtToken() {
        return null;
    }
    
    public final void saveHmacSecret(@org.jetbrains.annotations.NotNull
    java.lang.String secret) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getHmacSecret() {
        return null;
    }
    
    public final void clearAuthorities() {
    }
    
    /**
     * Advanced Hardening Checklist
     * Combines Root, Emulator, Frida, and debugger checks into a single integrity verdict.
     */
    public final boolean checkEnvironmentIntegrity() {
        return false;
    }
    
    public final boolean isDeviceRooted() {
        return false;
    }
    
    private final boolean checkRootMethod1() {
        return false;
    }
    
    private final boolean checkRootMethod2() {
        return false;
    }
    
    private final boolean checkRootMethod3() {
        return false;
    }
    
    public final boolean isMagiskDetected() {
        return false;
    }
    
    public final boolean isDebuggerAttached() {
        return false;
    }
    
    public final boolean isAdbEnabled() {
        return false;
    }
    
    public final boolean isFridaDetected() {
        return false;
    }
    
    public final boolean isEmulator() {
        return false;
    }
    
    public final boolean isApkSignatureValid(@org.jetbrains.annotations.NotNull
    java.lang.String expectedSignature) {
        return false;
    }
    
    private final java.lang.String bytesToHex(byte[] bytes) {
        return null;
    }
    
    /**
     * V35: Lockdown Persistence
     */
    public final void setLockdownActive(boolean active) {
    }
    
    public final boolean isLockdownActive() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/sentinel/agent/utils/SecurityHelper$Companion;", "", "()V", "EXTRA_AGENT_ID", "", "EXTRA_SIGNATURE", "EXTRA_TIMESTAMP", "KEYSTORE_ALIAS", "KEY_HMAC_SECRET", "KEY_JWT_TOKEN", "PREFS_NAME", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}