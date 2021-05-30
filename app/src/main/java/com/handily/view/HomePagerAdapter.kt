package com.handily.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter (fragment: Fragment, private val itemsCount: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                HomeFragment()
            }
            1 -> {
                FindOrdersFragment()
            }
            else -> {
                throw IllegalArgumentException("Invalid position passed.")
            }
        }
    }
}