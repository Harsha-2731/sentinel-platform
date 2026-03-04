package com.sentinel.agent.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sentinel.agent.R
import com.sentinel.agent.network.client.RetrofitClient
import com.sentinel.agent.utils.SecurityHelper
import kotlinx.coroutines.launch

import com.sentinel.agent.data.local.AppDatabase
import com.sentinel.agent.ui.adapter.TaskAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * V3.1: Task History
 * Displays all tasks stored in the Sentinel Agent (Local Storer).
 */
class TaskHistoryActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_history)

        db = AppDatabase.getDatabase(this)
        val recyclerView = findViewById<RecyclerView>(R.id.tasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchLocalTasks(recyclerView)
    }

    private fun fetchLocalTasks(rv: RecyclerView) {
        lifecycleScope.launch {
            val tasks = withContext(Dispatchers.IO) {
                db.taskDao().getAllTasks()
            }
            
            if (tasks.isNotEmpty()) {
                rv.adapter = TaskAdapter(tasks)
                Log.d("TaskHistory", "Displayed ${tasks.size} local agent tasks.")
            } else {
                Toast.makeText(this@TaskHistoryActivity, "No stored tasks found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
