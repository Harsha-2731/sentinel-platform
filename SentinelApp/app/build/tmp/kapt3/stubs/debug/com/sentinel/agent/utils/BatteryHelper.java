package com.sentinel.agent.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/sentinel/agent/utils/BatteryHelper;", "", "()V", "Companion", "app_debug"})
public final class BatteryHelper {
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.utils.BatteryHelper.Companion Companion = null;
    
    public BatteryHelper() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\t"}, d2 = {"Lcom/sentinel/agent/utils/BatteryHelper$Companion;", "", "()V", "isIgnoringBatteryOptimizations", "", "context", "Landroid/content/Context;", "requestBatteryExemptionIntent", "Landroid/content/Intent;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isIgnoringBatteryOptimizations(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return false;
        }
        
        @org.jetbrains.annotations.Nullable
        public final android.content.Intent requestBatteryExemptionIntent(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}