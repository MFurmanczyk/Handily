package com.handily.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.handily.R
import com.handily.databinding.FragmentHomeTabsBinding

class FragmentHomeTabs : Fragment() {

    private lateinit var homePagerAdapter: HomePagerAdapter
    private lateinit var viewPager: ViewPager2

    private var _binding: FragmentHomeTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeTabsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupLoginPager()
    }

    private fun setupLoginPager() {
        homePagerAdapter = HomePagerAdapter(this, binding.tabsHome.tabCount)
        viewPager = binding.homePager
        viewPager.adapter = homePagerAdapter
        TabLayoutMediator(
            binding.tabsHome,
            binding.homePager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.client)
                }
                1 -> {
                    tab.text = getString(R.string.worker)
                }
            }
        }.attach()
    }
}