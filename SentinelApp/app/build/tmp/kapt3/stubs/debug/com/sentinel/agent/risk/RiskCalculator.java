package com.sentinel.agent.risk;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010!\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u000256B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u000bH\u0002J\u0006\u0010\u001d\u001a\u00020\u0019J\u0006\u0010\u001e\u001a\u00020\u0006J\u0006\u0010\u001f\u001a\u00020 J\u0006\u0010!\u001a\u00020\u0019J \u0010\"\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u000b2\b\b\u0002\u0010%\u001a\u00020\u0006J\u0016\u0010&\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u000bJ\u0016\u0010\'\u001a\u00020\u00192\u0006\u0010(\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u000bJ\u0006\u0010)\u001a\u00020\u0019J\u0006\u0010*\u001a\u00020\u0019J\u0006\u0010+\u001a\u00020\u0019J\u000e\u0010,\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u000bJ\u0016\u0010-\u001a\u00020\u00192\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010.\u001a\u00020\u000bJ\u0006\u0010/\u001a\u00020\u0019J\u0006\u00100\u001a\u00020\u0019J\u0006\u00101\u001a\u00020\u0019J\u0006\u00102\u001a\u00020\u0019J\u000e\u00103\u001a\u00020\u00192\u0006\u00104\u001a\u00020\u0013R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R \u0010\t\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00060\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u0006@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R \u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/sentinel/agent/risk/RiskCalculator;", "", "()V", "ANOMALY_MULTIPLIER", "", "BURST_THRESHOLD", "", "TIME_WINDOW_MS", "", "actionHistory", "", "", "", "agentReputation", "<set-?>", "currentRiskScore", "getCurrentRiskScore", "()I", "eventListener", "Lcom/sentinel/agent/risk/RiskCalculator$RiskEventListener;", "lastAnomalyTimestamp", "lastObservedLocation", "recentAnomalyCount", "valueHistory", "addScore", "", "basePoints", "reasoning", "type", "applyTimeDecay", "getCurrentScore", "getRiskLevel", "Lcom/sentinel/agent/risk/RiskCalculator$RiskLevel;", "onAdbDetected", "onAgentAction", "agentId", "actionType", "amount", "onAnomalyDetected", "onHoneytrapTriggered", "points", "onHookingDetected", "onMinorAnomaly", "onNoMovementAfterFall", "onPolicyViolation", "onPromptReceived", "prompt", "onSensorSpoofingDetected", "onSuddenFall", "onTamperDetected", "resetScore", "setRiskEventListener", "listener", "RiskEventListener", "RiskLevel", "app_debug"})
public final class RiskCalculator {
    private static int currentRiskScore = 0;
    private static long lastAnomalyTimestamp = 0L;
    private static int recentAnomalyCount = 0;
    @org.jetbrains.annotations.NotNull
    private static final java.util.Map<java.lang.String, java.lang.Integer> agentReputation = null;
    @org.jetbrains.annotations.Nullable
    private static java.lang.String lastObservedLocation;
    @org.jetbrains.annotations.Nullable
    private static com.sentinel.agent.risk.RiskCalculator.RiskEventListener eventListener;
    @org.jetbrains.annotations.NotNull
    private static final java.util.Map<java.lang.String, java.util.List<java.lang.Long>> actionHistory = null;
    @org.jetbrains.annotations.NotNull
    private static final java.util.Map<java.lang.String, java.util.List<java.lang.Integer>> valueHistory = null;
    private static final long TIME_WINDOW_MS = 60000L;
    private static final int BURST_THRESHOLD = 3;
    private static final double ANOMALY_MULTIPLIER = 3.0;
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.risk.RiskCalculator INSTANCE = null;
    
    private RiskCalculator() {
        super();
    }
    
    public final int getCurrentRiskScore() {
        return 0;
    }
    
    public final int getCurrentScore() {
        return 0;
    }
    
    public final void applyTimeDecay() {
    }
    
    public final void setRiskEventListener(@org.jetbrains.annotations.NotNull
    com.sentinel.agent.risk.RiskCalculator.RiskEventListener listener) {
    }
    
    public final void onMinorAnomaly() {
    }
    
    public final void onSuddenFall() {
    }
    
    public final void onNoMovementAfterFall() {
    }
    
    public final void onSensorSpoofingDetected() {
    }
    
    public final void onAdbDetected() {
    }
    
    public final void onPolicyViolation(@org.jetbrains.annotations.NotNull
    java.lang.String reasoning) {
    }
    
    public final void onTamperDetected() {
    }
    
    public final void onHookingDetected() {
    }
    
    public final void onHoneytrapTriggered(int points, @org.jetbrains.annotations.NotNull
    java.lang.String reasoning) {
    }
    
    public final void onAnomalyDetected(@org.jetbrains.annotations.NotNull
    java.lang.String type, @org.jetbrains.annotations.NotNull
    java.lang.String reasoning) {
    }
    
    public final void onAgentAction(@org.jetbrains.annotations.NotNull
    java.lang.String agentId, @org.jetbrains.annotations.NotNull
    java.lang.String actionType, int amount) {
    }
    
    public final void onPromptReceived(@org.jetbrains.annotations.NotNull
    java.lang.String agentId, @org.jetbrains.annotations.NotNull
    java.lang.String prompt) {
    }
    
    private final void addScore(int basePoints, java.lang.String reasoning, java.lang.String type) {
    }
    
    public final void resetScore() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.sentinel.agent.risk.RiskCalculator.RiskLevel getRiskLevel() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0005H&\u00a8\u0006\n"}, d2 = {"Lcom/sentinel/agent/risk/RiskCalculator$RiskEventListener;", "", "onRiskEvent", "", "eventType", "", "delta", "", "currentRisk", "reasoning", "app_debug"})
    public static abstract interface RiskEventListener {
        
        public abstract void onRiskEvent(@org.jetbrains.annotations.NotNull
        java.lang.String eventType, int delta, int currentRisk, @org.jetbrains.annotations.NotNull
        java.lang.String reasoning);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/sentinel/agent/risk/RiskCalculator$RiskLevel;", "", "(Ljava/lang/String;I)V", "NORMAL", "SUSPICION", "EMERGENCY", "app_debug"})
    public static enum RiskLevel {
        /*public static final*/ NORMAL /* = new NORMAL() */,
        /*public static final*/ SUSPICION /* = new SUSPICION() */,
        /*public static final*/ EMERGENCY /* = new EMERGENCY() */;
        
        RiskLevel() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.sentinel.agent.risk.RiskCalculator.RiskLevel> getEntries() {
            return null;
        }
    }
}