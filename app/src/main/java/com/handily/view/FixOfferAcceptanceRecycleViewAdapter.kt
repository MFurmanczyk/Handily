package com.handily.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handily.R
import com.handily.model.FixOffer
import com.handily.repository.FirestoreProvider

class FixOfferAcceptanceRecycleViewAdapter(private val fixOfferList: ArrayList<FixOffer>): RecyclerView.Adapter<FixOfferAcceptanceViewHolder>() {


    fun updateFixRequestList(newFixOfferList: List<FixOffer>) {
        fixOfferList.clear()
        fixOfferList.addAll(newFixOfferList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FixOfferAcceptanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fix_offers_acceptance_element, parent, false)
        return FixOfferAcceptanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixOfferAcceptanceViewHolder, position: Int) {
        val priceText = fixOfferList[position].price.toString() + fixOfferList[position].currency
        holder.binding.fixOfferAcceptancePrice.text = priceText
        FirestoreProvider.instance.getUser(fixOfferList[position].workerUuid!!) {
            if (it != null) {
                val userDetails = it.givenName + " " + it.familyName
                holder.binding.fixOfferAcceptanceUser.text = userDetails
            }
        }

    }

    override fun getItemCount(): Int = fixOfferList.size
}