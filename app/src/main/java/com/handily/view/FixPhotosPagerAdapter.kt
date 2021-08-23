package com.handily.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.handily.R
import com.handily.databinding.FragmentFixPhotoBinding
import com.handily.util.getProgressDrawable
import com.handily.util.loadImage

class FixPhotosPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: FragmentFixPhotoBinding by lazy {FragmentFixPhotoBinding.bind(itemView)}
}

class FixPhotosPagerAdapter(val fragment: Fragment, private val bitmapUrls: ArrayList<String>) : RecyclerView.Adapter<FixPhotosPagerViewHolder>() {

    fun setBitmapList(newBitmapList: List<String>) {
        bitmapUrls.clear()
        bitmapUrls.addAll(newBitmapList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bitmapUrls.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixPhotosPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_fix_photo, parent, false)
        return FixPhotosPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixPhotosPagerViewHolder, position: Int) {
        holder.binding.fixPhoto.loadImage(bitmapUrls[position], getProgressDrawable(fragment.requireContext()))
    }
}