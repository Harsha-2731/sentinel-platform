package com.sentinel.agent.risk;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0005\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u000eH\u0002J\u0006\u0010\u0012\u001a\u00020\bJ\u0006\u0010\u0013\u001a\u00020\bJ\b\u0010\u0014\u001a\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/sentinel/agent/risk/HoneytrapManager;", "", "context", "Landroid/content/Context;", "onHoneytrapTriggered", "Lkotlin/Function2;", "", "", "", "(Landroid/content/Context;Lkotlin/jvm/functions/Function2;)V", "fileObserver", "Landroid/os/FileObserver;", "honeytrapDirName", "honeytrapFile", "Ljava/io/File;", "honeytrapFileName", "createFakeWalletPayload", "file", "deployHoneytrap", "removeHoneytrap", "startMonitoring", "Companion", "app_debug"})
public final class HoneytrapManager {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<java.lang.Integer, java.lang.String, kotlin.Unit> onHoneytrapTriggered = null;
    @org.jetbrains.annotations.Nullable
    private android.os.FileObserver fileObserver;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String honeytrapDirName = "CryptoKeys";
    @org.jetbrains.annotations.NotNull
    private final java.lang.String honeytrapFileName = "crypto_wallet_backup.txt";
    @org.jetbrains.annotations.Nullable
    private java.io.File honeytrapFile;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "HoneytrapManager";
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.risk.HoneytrapManager.Companion Companion = null;
    
    public HoneytrapManager(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.String, kotlin.Unit> onHoneytrapTriggered) {
        super();
    }
    
    public final void deployHoneytrap() {
    }
    
    private final void createFakeWalletPayload(java.io.File file) {
    }
    
    @kotlin.Suppress(names = {"DEPRECATION"})
    private final void startMonitoring() {
    }
    
    public final void removeHoneytrap() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/sentinel/agent/risk/HoneytrapManager$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}