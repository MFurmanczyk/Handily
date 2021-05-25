package com.handily.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handily.R
import com.handily.model.FixRequest

class FixRequestCardRecyclerViewAdapter(private val fixRequestList: List<FixRequest>)
    : RecyclerView.Adapter<FixRequestCardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixRequestCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fix_request_home_card, parent, false)
        return FixRequestCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixRequestCardViewHolder, position: Int) {
        holder.binding.fixRequest = fixRequestList[position]
    }

    override fun getItemCount(): Int = fixRequestList.size
}