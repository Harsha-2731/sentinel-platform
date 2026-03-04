package com.sentinel.agent.ui;

/**
 * V3.1: Task History
 * Displays all tasks stored in the Sentinel Agent (Local Storer).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\t\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/sentinel/agent/ui/TaskHistoryActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "db", "Lcom/sentinel/agent/data/local/AppDatabase;", "fetchLocalTasks", "", "rv", "Landroidx/recyclerview/widget/RecyclerView;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class TaskHistoryActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.sentinel.agent.data.local.AppDatabase db;
    
    public TaskHistoryActivity() {
        super();
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void fetchLocalTasks(androidx.recyclerview.widget.RecyclerView rv) {
    }
}