package com.handily.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.google.android.material.transition.MaterialContainerTransform
import com.handily.R
import com.handily.databinding.FragmentFixDetailsBinding


class FixDetailsFragment : Fragment() {

    private var _binding: FragmentFixDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        _binding = FragmentFixDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fixDetailsToolbar.inflateMenu(R.menu.fix_details_menu)

        binding.fixDetailsToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.fix_details_post -> {
                    //TODO: save fix request
                    val action = FixDetailsFragmentDirections.actionFixDetailsFragmentToHomeFragment()
                    findNavController().navigate(action)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }

    }

}