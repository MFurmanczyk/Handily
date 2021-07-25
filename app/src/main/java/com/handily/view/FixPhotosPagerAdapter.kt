package com.handily.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FixPhotosPagerAdapter(fragment: Fragment, private val bitmapUrls: ArrayList<String>) : FragmentStateAdapter(fragment) {

    fun setBitmapList(newBitmapList: List<String>) {
        bitmapUrls.clear()
        bitmapUrls.addAll(newBitmapList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bitmapUrls.size

    override fun createFragment(position: Int): Fragment {
       return FixPhotoFragment(bitmapUrls[position])
    }
}