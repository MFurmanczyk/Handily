package com.handily.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.handily.R
import com.handily.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupLoginPager()
    }

    private fun setupLoginPager() {
        val viewPagerAdapter = SignInPagerAdapter(this, binding.tabsLogin.tabCount)
        binding.loginPager.adapter = viewPagerAdapter
        TabLayoutMediator(
            binding.tabsLogin,
            binding.loginPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.login)
                }
                1 -> {
                    tab.text = getString(R.string.register)
                }
            }
        }.attach()
        //binding.loginPager.isUserInputEnabled = false
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}