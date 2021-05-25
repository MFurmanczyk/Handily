package com.handily.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.handily.databinding.FixRequestHomeCardBinding

class FixRequestCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: FixRequestHomeCardBinding by lazy { FixRequestHomeCardBinding.bind(itemView) }
}
