package com.handily.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.handily.databinding.FixOffersAcceptanceElementBinding

class FixOfferAcceptanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding : FixOffersAcceptanceElementBinding by lazy { FixOffersAcceptanceElementBinding.bind(itemView) }
}