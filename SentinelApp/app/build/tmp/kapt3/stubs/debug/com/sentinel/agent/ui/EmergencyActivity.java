package com.sentinel.agent.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0011H\u0016J\u0012\u0010\u0013\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0014J\b\u0010\u0016\u001a\u00020\u0011H\u0014J\b\u0010\u0017\u001a\u00020\u0011H\u0002J\b\u0010\u0018\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/sentinel/agent/ui/EmergencyActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "countdown", "", "countdownText", "Landroid/widget/TextView;", "emergencyManager", "Lcom/sentinel/agent/service/emergency/EmergencyManager;", "handler", "Landroid/os/Handler;", "isCountdownPaused", "", "reasoning", "", "statusText", "checkAndPromptBiometric", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onUserLeaveHint", "showBiometricPrompt", "startCountdown", "app_debug"})
public final class EmergencyActivity extends androidx.appcompat.app.AppCompatActivity {
    private int countdown = 60;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler handler = null;
    private android.widget.TextView countdownText;
    private android.widget.TextView statusText;
    private java.lang.String reasoning;
    @org.jetbrains.annotations.NotNull
    private final com.sentinel.agent.service.emergency.EmergencyManager emergencyManager = null;
    private boolean isCountdownPaused = false;
    
    public EmergencyActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void checkAndPromptBiometric() {
    }
    
    private final void showBiometricPrompt() {
    }
    
    @java.lang.Override
    protected void onUserLeaveHint() {
    }
    
    private final void startCountdown() {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
}