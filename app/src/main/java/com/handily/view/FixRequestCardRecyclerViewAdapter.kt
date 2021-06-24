package com.handily.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.handily.R
import com.handily.model.FixRequest

class FixRequestCardRecyclerViewAdapter(private val fixRequestList: ArrayList<FixRequest>)
    : RecyclerView.Adapter<FixRequestCardViewHolder>(), FixClickListener {

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
        holder.binding.listener = this
    }

    override fun getItemCount(): Int = fixRequestList.size

    override fun onFixClicked(v: View, fixUuid: String) {
        Log.d("onFixClicked", "clicked: $fixUuid")
        val action = FragmentHomeTabsDirections.fixOverviewFragmentDirection(fixUuid)
        Navigation.findNavController(v).navigate(action)
    }
}