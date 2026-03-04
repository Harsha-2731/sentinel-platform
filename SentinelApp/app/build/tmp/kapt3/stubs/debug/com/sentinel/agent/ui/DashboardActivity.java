package com.sentinel.agent.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0014J1\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0016\u0010\u0017\u001a\u00020\b2\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001b"}, d2 = {"Lcom/sentinel/agent/ui/DashboardActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "db", "Lcom/sentinel/agent/data/local/AppDatabase;", "securityHelper", "Lcom/sentinel/agent/utils/SecurityHelper;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "refreshUi", "bar", "Landroid/widget/ProgressBar;", "scoreText", "Landroid/widget/TextView;", "statusText", "rv", "Landroidx/recyclerview/widget/RecyclerView;", "(Landroid/widget/ProgressBar;Landroid/widget/TextView;Landroid/widget/TextView;Landroidx/recyclerview/widget/RecyclerView;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateIntegrityWidget", "risk", "", "updateTimeline", "trend", "", "Lcom/sentinel/agent/data/local/entity/RiskEventEntity;", "app_debug"})
public final class DashboardActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.sentinel.agent.data.local.AppDatabase db;
    private com.sentinel.agent.utils.SecurityHelper securityHelper;
    
    public DashboardActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final java.lang.Object refreshUi(android.widget.ProgressBar bar, android.widget.TextView scoreText, android.widget.TextView statusText, androidx.recyclerview.widget.RecyclerView rv, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void updateIntegrityWidget(int risk) {
    }
    
    private final void updateTimeline(java.util.List<com.sentinel.agent.data.local.entity.RiskEventEntity> trend) {
    }
}