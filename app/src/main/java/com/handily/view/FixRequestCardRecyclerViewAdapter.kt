package com.handily.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handily.R
import com.handily.model.FixRequest

class FixRequestCardRecyclerViewAdapter(private val fixRequestList: ArrayList<FixRequest>)
    : RecyclerView.Adapter<FixRequestCardViewHolder>(){

    fun updateFixRequestList(newFixRequestList: List<FixRequest>) {
        fixRequestList.clear()
        fixRequestList.addAll(newFixRequestList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixRequestCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fix_request_home_card, parent, false)
        return FixRequestCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixRequestCardViewHolder, position: Int) {
        holder.binding.fixRequest = fixRequestList[position]
    }

    override fun getItemCount(): Int = fixRequestList.size
}