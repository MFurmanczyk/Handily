package com.handily.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.handily.databinding.FixRequestPhotoBinding

class FixPhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: FixRequestPhotoBinding by lazy { FixRequestPhotoBinding.bind(itemView) }
}