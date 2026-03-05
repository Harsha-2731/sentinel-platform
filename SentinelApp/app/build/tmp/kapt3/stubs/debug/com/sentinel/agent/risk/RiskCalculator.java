package com.sentinel.agent.risk;

/**
 * Sentinel V2.0: Probabilistic Risk Engine (EDR Logic)
 *
 * Moves from simple additive scoring to a state-aware, correlated risk model.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u001d\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002,-B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0010\u001a\u00020\u0011J\u0006\u0010\u0012\u001a\u00020\u0004J\u0006\u0010\u0013\u001a\u00020\u0006J\u0006\u0010\u0014\u001a\u00020\u0011J\u0016\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u000bJ)\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u000b2\b\b\u0002\u0010\u0017\u001a\u00020\u000b2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0004\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u000bJ\u0006\u0010\u001d\u001a\u00020\u0011J\u0006\u0010\u001e\u001a\u00020\u0011J\u0006\u0010\u001f\u001a\u00020\u0011J\u000e\u0010 \u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u000bJ\u0016\u0010\"\u001a\u00020\u00112\u0006\u0010#\u001a\u00020\u000b2\u0006\u0010$\u001a\u00020\u000bJ\u0006\u0010%\u001a\u00020\u0011J\u0006\u0010&\u001a\u00020\u0011J\u0006\u0010\'\u001a\u00020\u0011J\u0006\u0010(\u001a\u00020\u0011J\u000e\u0010)\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\bJ\b\u0010+\u001a\u00020\u0011H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00040\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcom/sentinel/agent/risk/RiskCalculator;", "", "()V", "currentRiskScore", "", "currentState", "Lcom/sentinel/agent/risk/RiskCalculator$RiskState;", "eventListener", "Lcom/sentinel/agent/risk/RiskCalculator$RiskEventListener;", "eventWeights", "", "", "lastAnomalyTimestamp", "", "recentSignificantEvents", "", "applyTimeDecay", "", "getCurrentScore", "getCurrentState", "onAdbDetected", "onAnomalyDetected", "type", "reasoning", "onEvent", "basePoints", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)V", "onHoneytrapTriggered", "points", "onHookingDetected", "onMinorAnomaly", "onNoMovementAfterFall", "onPolicyViolation", "reason", "onPromptReceived", "agentId", "prompt", "onSensorSpoofingDetected", "onSuddenFall", "onTamperDetected", "resetScore", "setRiskEventListener", "listener", "updateState", "RiskEventListener", "RiskState", "app_debug"})
public final class RiskCalculator {
    private static int currentRiskScore = 0;
    @org.jetbrains.annotations.NotNull
    private static com.sentinel.agent.risk.RiskCalculator.RiskState currentState = com.sentinel.agent.risk.RiskCalculator.RiskState.SECURE;
    private static long lastAnomalyTimestamp = 0L;
    @org.jetbrains.annotations.NotNull
    private static java.util.List<java.lang.String> recentSignificantEvents;
    @org.jetbrains.annotations.NotNull
    private static final java.util.Map<java.lang.String, java.lang.Integer> eventWeights = null;
    @org.jetbrains.annotations.Nullable
    private static com.sentinel.agent.risk.RiskCalculator.RiskEventListener eventListener;
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.risk.RiskCalculator INSTANCE = null;
    
    private RiskCalculator() {
        super();
    }
    
    public final int getCurrentScore() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.sentinel.agent.risk.RiskCalculator.RiskState getCurrentState() {
        return null;
    }
    
    public final void applyTimeDecay() {
    }
    
    public final void onEvent(@org.jetbrains.annotations.NotNull
    java.lang.String type, @org.jetbrains.annotations.NotNull
    java.lang.String reasoning, @org.jetbrains.annotations.Nullable
    java.lang.Integer basePoints) {
    }
    
    private final void updateState() {
    }
    
    public final void onMinorAnomaly() {
    }
    
    public final void onSuddenFall() {
    }
    
    public final void onSensorSpoofingDetected() {
    }
    
    public final void onAdbDetected() {
    }
    
    public final void onTamperDetected() {
    }
    
    public final void onHookingDetected() {
    }
    
    public final void onPolicyViolation(@org.jetbrains.annotations.NotNull
    java.lang.String reason) {
    }
    
    public final void onNoMovementAfterFall() {
    }
    
    public final void onAnomalyDetected(@org.jetbrains.annotations.NotNull
    java.lang.String type, @org.jetbrains.annotations.NotNull
    java.lang.String reasoning) {
    }
    
    public final void onPromptReceived(@org.jetbrains.annotations.NotNull
    java.lang.String agentId, @org.jetbrains.annotations.NotNull
    java.lang.String prompt) {
    }
    
    public final void onHoneytrapTriggered(int points, @org.jetbrains.annotations.NotNull
    java.lang.String reasoning) {
    }
    
    public final void setRiskEventListener(@org.jetbrains.annotations.NotNull
    com.sentinel.agent.risk.RiskCalculator.RiskEventListener listener) {
    }
    
    public final void resetScore() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J(\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0005H&\u00a8\u0006\n"}, d2 = {"Lcom/sentinel/agent/risk/RiskCalculator$RiskEventListener;", "", "onRiskEvent", "", "eventType", "", "delta", "", "currentRisk", "reasoning", "app_debug"})
    public static abstract interface RiskEventListener {
        
        public abstract void onRiskEvent(@org.jetbrains.annotations.NotNull
        java.lang.String eventType, int delta, int currentRisk, @org.jetbrains.annotations.NotNull
        java.lang.String reasoning);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/sentinel/agent/risk/RiskCalculator$RiskState;", "", "(Ljava/lang/String;I)V", "SECURE", "VIGILANT", "LOCKDOWN", "app_debug"})
    public static enum RiskState {
        /*public static final*/ SECURE /* = new SECURE() */,
        /*public static final*/ VIGILANT /* = new VIGILANT() */,
        /*public static final*/ LOCKDOWN /* = new LOCKDOWN() */;
        
        RiskState() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.sentinel.agent.risk.RiskCalculator.RiskState> getEntries() {
            return null;
        }
    }
}