package com.sentinel.agent.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sentinel.agent.R
import com.sentinel.agent.data.local.entity.RiskEventEntity
import java.text.SimpleDateFormat
import java.util.*

class RiskEventAdapter(private val events: List<RiskEventEntity>) : RecyclerView.Adapter<RiskEventAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: TextView = view.findViewById(R.id.itemType)
        val reasoning: TextView = view.findViewById(R.id.itemReasoning)
        val score: TextView = view.findViewById(R.id.itemScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_risk_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.type.text = event.eventType
        holder.reasoning.text = event.reasoning
        holder.score.text = "Risk: ${event.currentRiskScore}"
        
        if (event.riskDelta > 0) {
            holder.score.setTextColor(android.graphics.Color.parseColor("#FF4444"))
        } else {
            holder.score.setTextColor(android.graphics.Color.parseColor("#00FFCC"))
        }
    }

    override fun getItemCount() = events.size
}
