package com.sentinel.agent.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sentinel.agent.R
import com.sentinel.agent.data.local.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(private val tasks: List<TaskEntity>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val agentName: TextView = view.findViewById(R.id.itemAgentName)
        val taskDesc: TextView = view.findViewById(R.id.itemTaskDesc)
        val status: TextView = view.findViewById(R.id.itemStatus)
        val timestamp: TextView = view.findViewById(R.id.itemTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.agentName.text = task.agentName
        holder.taskDesc.text = "${task.actionType}: ₹${task.amount}"
        holder.status.text = task.status.uppercase()
        
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        holder.timestamp.text = sdf.format(Date(task.timestamp))

        if (task.status == "blocked") {
            holder.status.setTextColor(android.graphics.Color.parseColor("#FF4444"))
        } else {
            holder.status.setTextColor(android.graphics.Color.parseColor("#00FFCC"))
        }
    }

    override fun getItemCount() = tasks.size
}
