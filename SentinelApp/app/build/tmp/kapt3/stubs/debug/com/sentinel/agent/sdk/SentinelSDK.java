package com.sentinel.agent.sdk;

/**
 * SentinelSDK (Phase 34)
 *
 * Formal interface for AI Agents to integrate with the Sentinel Security Layer.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\n\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\rB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/sentinel/agent/sdk/SentinelSDK;", "", "()V", "ACTION_AGENT_EXECUTE", "", "ACTION_POLICY_VIOLATION", "EXTRA_ACTION_TYPE", "EXTRA_AGENT_ID", "EXTRA_AGENT_NAME", "EXTRA_AMOUNT", "EXTRA_SIGNATURE", "EXTRA_THRESHOLD", "EXTRA_TIMESTAMP", "ResponseTier", "app_debug"})
public final class SentinelSDK {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_AGENT_EXECUTE = "com.sentinel.agent.ACTION_AGENT_EXECUTE";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String ACTION_POLICY_VIOLATION = "com.sentinel.agent.POLICY_VIOLATION";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_AGENT_NAME = "AGENT_NAME";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_ACTION_TYPE = "ACTION_TYPE";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_AMOUNT = "AMOUNT";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_THRESHOLD = "THRESHOLD";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_AGENT_ID = "com.sentinel.agent.EXTRA_AGENT_ID";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_SIGNATURE = "com.sentinel.agent.EXTRA_SIGNATURE";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String EXTRA_TIMESTAMP = "com.sentinel.agent.EXTRA_TIMESTAMP";
    @org.jetbrains.annotations.NotNull
    public static final com.sentinel.agent.sdk.SentinelSDK INSTANCE = null;
    
    private SentinelSDK() {
        super();
    }
    
    /**
     * Tiered Response Levels
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/sentinel/agent/sdk/SentinelSDK$ResponseTier;", "", "(Ljava/lang/String;I)V", "ALERT", "BLOCK", "LOCKDOWN", "app_debug"})
    public static enum ResponseTier {
        /*public static final*/ ALERT /* = new ALERT() */,
        /*public static final*/ BLOCK /* = new BLOCK() */,
        /*public static final*/ LOCKDOWN /* = new LOCKDOWN() */;
        
        ResponseTier() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.sentinel.agent.sdk.SentinelSDK.ResponseTier> getEntries() {
            return null;
        }
    }
}