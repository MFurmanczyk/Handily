package com.handily.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.handily.R
import com.handily.databinding.FragmentHomeBinding
import com.handily.viewmodel.HandilyViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel : HandilyViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeTopAppBar.inflateMenu(R.menu.home_menu)
        binding.homeTopAppBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.sign_out -> {
                    viewModel.signOut()
                    return@setOnMenuItemClickListener true
                }
                else ->{
                    return@setOnMenuItemClickListener false
                }
            }
        }

    }


    private fun observeViewModel() {
        viewModel.authenticatedUser.observe(viewLifecycleOwner) {
            it?.let {
                binding.user = it
            }
        }
    }

}