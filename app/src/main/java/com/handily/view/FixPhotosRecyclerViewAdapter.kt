package com.handily.view

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.handily.R

class FixPhotosRecyclerViewAdapter(private val fixPhotosList: ArrayList<Bitmap>)
    : RecyclerView.Adapter<FixPhotosViewHolder>(){


    fun updatePhotosList(newPhotosList: ArrayList<Bitmap>) {
        fixPhotosList.clear()
        fixPhotosList.addAll(newPhotosList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixPhotosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fix_request_photo, parent, false)
        return FixPhotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixPhotosViewHolder, position: Int) {
        holder.binding.fixImage.setImageBitmap(fixPhotosList[position])
    }

    override fun getItemCount(): Int = fixPhotosList.size
}